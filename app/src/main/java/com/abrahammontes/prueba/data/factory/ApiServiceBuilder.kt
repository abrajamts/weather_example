package com.abrahammontes.prueba.data.factory

import com.abrahammontes.prueba.data.checks.exception.NetworkException
import com.abrahammontes.prueba.data.dummy.DummyCache
import com.google.gson.GsonBuilder
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier

class ApiServiceImpl(private var retrofit: Retrofit) : ApiService {

    override fun <T> create(service: Class<T>): T {
        return retrofit.create(service)
    }

    class Builder constructor(private val dummyCache: DummyCache) {
        protected val BASE_URL : String = "http://api.openweathermap.org/data"
        protected val API_KEY  : String = "e357d51a2410223144843e2e5a0e39ae"

        private val builder = Retrofit.Builder()

        fun baseUrl(baseUrl: String) : Builder {
            builder.baseUrl(baseUrl)
            return this
        }

        fun build() : ApiService {
            val retrofit = builder
                .client(makeOkHttpClient(makeLoggingInterceptor()))
                .addConverterFactory(GsonConverterFactory.create(makeGson()))
                .build()

            return ApiServiceImpl(retrofit)
        }

        private fun makeOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor) : OkHttpClient {
            val isDummy = false
            val doNotVerify = HostnameVerifier { _, _ -> true }

            return OkHttpClient.Builder()
                .hostnameVerifier(doNotVerify)
                .connectionSpecs(Collections.singletonList(ConnectionSpec
                    .Builder(ConnectionSpec.MODERN_TLS)
                    .allEnabledCipherSuites()
                    .allEnabledTlsVersions()
                    .build())
                )
                .addInterceptor { chain ->
                    val request = chain.request()
                    val response : Response = if (isDummy) {
                        try {
                            val json = dummyCache.getDummy(request.url.toUrl())
                            Thread.sleep(2000)
                            Response.Builder()
                                .request(request)
                                .protocol(Protocol.HTTP_2)
                                .message("OK")
                                .body(ResponseBody.create("application/json; charset=utf-8".toMediaTypeOrNull(), json))
                                .code(200)
                                .build()

                        } catch (e: Exception) {
                            throw NetworkException(e.localizedMessage.orEmpty(), e)
                        }

                    } else {
                        var newRequest : Request.Builder ? = null

                        if (request.method == "GET") {
                            val mURL = request.url.toUrl().toString() + "&appid=$API_KEY"
                            newRequest = request.newBuilder()
                                .url(mURL)

                        } else {
                            newRequest = request.newBuilder()
                                .url(BASE_URL + request.url.encodedPath + "&appid=$API_KEY")
                        }

                        try {
                            chain.proceed(newRequest.build())
                        } catch (e: Exception) {
                            throw NetworkException(e.localizedMessage.orEmpty(), e)
                        }
                    }

                    response.peekBody(Long.MAX_VALUE).string().let {
                        response
                    }
                }
                .connectionSpecs(Arrays.asList(ConnectionSpec.MODERN_TLS, ConnectionSpec.CLEARTEXT))
                .addInterceptor(httpLoggingInterceptor)
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES)
                .retryOnConnectionFailure(false)
                .connectionPool(ConnectionPool(1,5, TimeUnit.SECONDS))
                .build()
        }

        private fun makeLoggingInterceptor() : HttpLoggingInterceptor {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            //logging.level = HttpLoggingInterceptor.Level.NONE
            return logging
        }

        private fun makeGson() = GsonBuilder().create()
    }
}
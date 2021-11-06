package com.abrahammontes.prueba.common

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.abrahammontes.prueba.PruebaApplication
import com.abrahammontes.prueba.data.PrApiService
import com.abrahammontes.prueba.data.Result
import com.abrahammontes.prueba.data.checks.exception.NetworkException
import com.abrahammontes.prueba.helpers.GeolocationManager
import com.abrahammontes.prueba.models.WeatherLocationModel
import kotlinx.coroutines.*
import okhttp3.Dispatcher
import retrofit2.http.Query

class GeneralViewModel(app: Application, private val apiService: PrApiService) : BaseViewModel(app) {

    fun validateLocation(ctx: Context) : LiveData<Result<Boolean>> {
        var step = 0
        val result = MutableLiveData<Result<Boolean>>()
        launch(CoroutineExceptionHandler { _, e -> result.postError(e) })  {
            if (PruebaApplication().getInstance().geolocationManager()!!.checkStatus(ctx)) {

                if (PruebaApplication().getInstance().geolocationManager()!!.validGeolocation()) {
                    result.value = Result.Success(true)
                } else {
                    PruebaApplication().getInstance().geolocationManager()!!.toggleLocation(ctx)

                    while (step < 3) {
                        delay(1500L)
                        if (PruebaApplication().getInstance().geolocationManager()!!.validGeolocation()) {
                            result.value = Result.Success(true)
                        }
                        step++

                        if (step == 3 && result.value != Result.Success(true)) {
                            result.value = Result.Success(false)
                        }
                    }
                }
            } else {
                result.value = Result.Success(false)
            }
        }
        return result
    }

    fun getWeatherForLocation() : LiveData<Result<WeatherLocationModel>> {
        val result = MutableLiveData<Result<WeatherLocationModel>>()
        launch(CoroutineExceptionHandler { _, e -> result.postError(e) }) {
            result.value = withContext(Dispatchers.IO) {
                Result.Success(apiService.getWeatherForLocation(
                    GeolocationManager.latitude, GeolocationManager.longitud
                ))
            }
        }
        return result
    }

    private fun <T> MutableLiveData<Result<T>>.postError(e: Throwable) {
        value = when (e) {
            is NetworkException -> {
                Log.d("NetworkException", e.message, e.cause)
                Result.Error(e)
            }
            else -> {
                Log.d("Error", e.message, e.cause)
                Result.Error(e)
            }
        }
    }
}
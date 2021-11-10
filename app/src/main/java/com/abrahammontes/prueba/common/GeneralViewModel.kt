package com.abrahammontes.prueba.common

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.abrahammontes.prueba.PruebaApplication
import com.abrahammontes.prueba.data.PrApiService
import com.abrahammontes.prueba.data.Result
import com.abrahammontes.prueba.data.checks.exception.LocationException
import com.abrahammontes.prueba.data.checks.exception.NetworkException
import com.abrahammontes.prueba.helpers.GeolocationManager
import com.abrahammontes.prueba.models.WeatherLocationModel
import kotlinx.coroutines.*
import okhttp3.Dispatcher
import retrofit2.http.Query

class GeneralViewModel(app: Application, private val apiService: PrApiService) : BaseViewModel(app) {

    private val app : PruebaApplication = PruebaApplication().getInstance()

    fun getLocationForce(ctx: Context) : LiveData<Result<Pair<String, String>>> {
        var step = 0
        var done = false
        val result = MutableLiveData<Result<Pair<String, String>>>()
        launch(CoroutineExceptionHandler { _, e -> result.postError(e) }) {
            if (app.geolocationManager().checkLocationDevice(ctx)) {
                app.geolocationManager().toggleLocation(ctx)

                while (step < 100) {
                    delay(1500L)
                    if (!done) {
                        if (app.geolocationManager().validGeolocation()) {
                            done = true
                            result.value = Result.Success(Pair(GeolocationManager.longitud, GeolocationManager.latitude))
                        }
                        step++

                        if (step == 100 && !(result is Result.Success<*>)) {
                            done = true
                            result.value = Result.Error(LocationException("LOCATION_NOT_FOUND", "No se pudo obtener la ubicación del dispositivo"))
                        }
                    }
                }

            } else {
                result.value = Result.Error(LocationException("NOT_PERMISSION", "No se pudo obtener la ubicación del dispositivo debido a falta de permisos o sensor"))
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
            is LocationException -> {
                Log.d("LocationException", e.message, e.cause)
                Result.Error(e)
            }
            else -> {
                Log.d("Error", e.message, e.cause)
                Result.Error(e)
            }
        }
    }
}
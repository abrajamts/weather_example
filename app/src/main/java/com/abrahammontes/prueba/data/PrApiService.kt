package com.abrahammontes.prueba.data

import com.abrahammontes.prueba.helpers.GeolocationManager
import com.abrahammontes.prueba.models.WeatherLocationModel
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Query

interface PrApiService {

    @GET("data/2.5/weather")
    suspend fun getWeatherForLocation(@Query("lat") lat: String ? = GeolocationManager.latitude, @Query("lon") lon: String ? = GeolocationManager.longitud) : WeatherLocationModel

    //http://api.openweathermap.org/data/2.5/weather?lat=19.43260666666667&lon=-99.13320666666665&appid=e357d51a2410223144843e2e5a0e39ae
    //http://api.openweathermap.org/data/2.5/weather?lat=19.43260666666667&lon=-99.13320666666665&appid=e357d51a2410223144843e2e5a0e39ae
}
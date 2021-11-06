package com.abrahammontes.prueba

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.abrahammontes.prueba.common.GeneralViewModel
import com.abrahammontes.prueba.data.Result
import com.abrahammontes.prueba.models.WeatherLocationModel
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.java.KoinJavaComponent


class MainActivity : AppCompatActivity() {
    protected var viewModel = KoinJavaComponent.inject(GeneralViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val ctx = this

        if (PruebaApplication().getInstance().geolocationManager()!!.checkStatus(ctx)) {
            viewModel.value.validateLocation(this).observe(this, { resultGeolocation ->
                val result = resultGeolocation.getResultIfNotHandled()

                if (result is Result.Success<*>) {
                    if ((result as Result.Success<Boolean?>).data!!) {
                        showScreen()

                    } else
                        Toast.makeText(ctx, "No se pudo obtener tu ubicación, intenta nuevamente", Toast.LENGTH_LONG).show()

                }

            })
        }

    }

    private fun showScreen() {

        viewModel.value.getWeatherForLocation().observe(this, { result ->
            val result = result.getResultIfNotHandled()

            if (result is Result.Success<*>) {
                val res = result.data as WeatherLocationModel

                val gCel : String = (res.main.temp - 273.15).toString()
                val gCelFeels : String = (res.main.feels_like - 273.15).toString()
                val gCelSplit = gCel.split(".")
                val gCelFeelsSplit = gCelFeels.split(".")

                if (gCelSplit.size > 1) {
                    tvTemp.setText(gCelSplit.get(0) + "." + gCelSplit.get(1).substring(0, 1) + "º")
                } else {
                    tvTemp.setText(gCel + "º")
                }

                if (gCelFeelsSplit.size > 1) {
                    tvTempFeels.setText(gCelFeelsSplit.get(0) + "." + gCelFeelsSplit.get(1).substring(0,1) + "º")
                } else {
                    tvTempFeels.setText(gCelFeels + "º")
                }

                tvDesc.setText(res.weather.first().description)
                tvCity.setText(res.name)
            }
        })
    }

}
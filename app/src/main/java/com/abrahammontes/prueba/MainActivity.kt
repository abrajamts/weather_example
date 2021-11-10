package com.abrahammontes.prueba

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.abrahammontes.prueba.activities.base.BaseWeatherActivity
import com.abrahammontes.prueba.common.GeneralViewModel
import com.abrahammontes.prueba.data.Result
import com.abrahammontes.prueba.dialogs.DialogWarming
import com.abrahammontes.prueba.models.WeatherLocationModel
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.java.KoinJavaComponent


class MainActivity : BaseWeatherActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getInformation()
    }

    private fun getInformation() {
        showProgressDialog()
        viewModel.value.getWeatherForLocation().observe(this, { response ->
            val result = response.getResultIfNotHandled()

            if (result is Result.Success<*>) {

                Handler().postDelayed({
                    dismissProgressDialog()
                    putScreen(result.data as WeatherLocationModel)
                }, 1000)
            }

            if (result is Result.Error) {
                Handler().postDelayed({ dismissProgressDialog()
                                      }, 1000)
            }
        })
    }

    private fun putScreen(data: WeatherLocationModel) {

        val gCel : String = (data.main.temp - 273.15).toString()
        val gCelFeels : String = (data.main.feels_like - 273.15).toString()
        val gCelSplit = gCel.split(".")
        val gCelFeelsSplit = gCelFeels.split(".")

        tvLocation.setText(data.name)
        tvDescriptionWeather.setText(data.weather.first().description)
        if (gCelSplit.size > 1) {
            tvTemp.setText(gCelSplit.get(0) + "." + gCelSplit.get(1).substring(0, 1) + "º")
        } else {
            tvTemp.setText(gCel + "º")
        }

    }


}
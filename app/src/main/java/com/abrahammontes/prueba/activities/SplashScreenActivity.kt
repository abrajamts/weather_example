package com.abrahammontes.prueba.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.View
import android.widget.Toast
import com.abrahammontes.prueba.MainActivity
import com.abrahammontes.prueba.PruebaApplication
import com.abrahammontes.prueba.R
import com.abrahammontes.prueba.activities.base.BaseWeatherActivity
import com.abrahammontes.prueba.data.Result
import com.abrahammontes.prueba.dialogs.DialogCustom
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreenActivity : BaseWeatherActivity() {
    private var isRunningPermission: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        checkPermission()
    }

    override fun onResume() {
        super.onResume()
        if (!isRunningPermission) {
            checkPermission()
        }
    }

    fun checkPermission() {
        isRunningPermission = true
        if (PruebaApplication().getInstance().geolocationManager()!!.permissionEnabled()) {
            if (PruebaApplication().getInstance().geolocationManager()!!.sensorEnabled()) {

                tvMessage.setText("Obteniendo ubicación")
                viewModel.value.getLocationForce(this).observe(this, { resultGeo ->
                    val result = resultGeo.getResultIfNotHandled()
                    if (result is Result.Success<*>) {
                        PruebaApplication().getInstance().geolocationManager()!!.removeLocation()
                        tvMessage.setText("Estamos listos")
                        Handler().postDelayed({
                            val intent = Intent(this, MainActivity::class.java)
                            this.startActivity(intent)
                            finish()
                        }, 1500)
                    }

                    if (result is Result.Error) {
                        tvMessage.setText("No pudimos obtener tu ubicación. Intenta nuevamente.")
                        Handler().postDelayed({
                            Toast.makeText(this, "Error", Toast.LENGTH_LONG).show()
                        }, 1000)
                    }
                })

            } else {
                tvMessage.setText("Enciende la ubicación del dispositivo")
                DialogCustom(this, "Ubicación", "Para continuar enciende tu ubicación", "Configuración", View.OnClickListener {
                    isRunningPermission = false
                    this.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                }, R.drawable.ic_location_map).show()
            }
        } else {
            tvMessage.setText("Solicitando permisos")
            DialogCustom(this, "Permisos", "Para continuar debes permitir el acceso a tu localización", "Continuar", View.OnClickListener {
                PruebaApplication().getInstance().geolocationManager()!!.requestPermission(this)
                isRunningPermission = false
            }, R.drawable.ic_map_location).show()
        }
    }

    companion object {
        val SOLICITUD_PERMISO_ACCES_LOCATION = 1
    }
}
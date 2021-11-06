package com.abrahammontes.prueba.helpers

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.provider.Settings
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.abrahammontes.prueba.activities.SplashScreenActivity

class GeolocationManager(val context: Context) {
    private lateinit var locationManager : LocationManager
    private lateinit var mlocationListener: LocationListener
    private val TAG = "GeolocationManager"

    init {
        locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        Log.i(TAG, "$TAG initialization successful")
    }

    fun perssionEnabled() : Boolean {
        return ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    fun sensorEnabled() : Boolean {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    fun requestPermission(ctx: Context) {
        ActivityCompat.requestPermissions((ctx as Activity), arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), SplashScreenActivity.SOLICITUD_PERMISO_ACCES_LOCATION)
    }

    fun requestSensor(ctx: Context) {
        val builder = AlertDialog.Builder(ctx)
        builder.setTitle("Geolocalización")
            .setMessage("Para continuar por favor la ubicación de tu dispositivo")
            .setPositiveButton("Configuración", DialogInterface.OnClickListener { dialogInterface, i ->
                ctx.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            })
    }

    fun checkStatus(ctx: Context) : Boolean {
        if (perssionEnabled()) {
            if (sensorEnabled()) {
                return true
            } else {
                requestSensor(ctx)
            }
        } else {
            requestPermission(ctx)
        }
        return false
    }

    fun toggleLocation(ctx: Context) {
        if (checkStatus(ctx)) {
            val criteria = Criteria()
            criteria.accuracy = Criteria.ACCURACY_FINE
            criteria.isAltitudeRequired = false
            criteria.isBearingRequired = false
            criteria.isCostAllowed = true
            criteria.powerRequirement = Criteria.POWER_HIGH
            
            val provider = locationManager.getBestProvider(criteria, true)
            if (provider != null) {

                if (ActivityCompat.checkSelfPermission(ctx, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ctx, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return
                }
                
                val mTime = 1000.toLong()
                mlocationListener = object : LocationListener {
                    override fun onLocationChanged(location: Location) {
                        latitude = location.latitude.toString()
                        longitud = location.longitude.toString()

                        Log.i(TAG, "Latitude: ${location.getLatitude()} | Longitude: ${location.getLongitude()}")
                    }
                }
                
                locationManager.requestLocationUpdates(provider, mTime, 1f, mlocationListener)
            }
        }
    }

    fun validGeolocation() : Boolean {
        var lat = 0.0
        var long = 0.0
        try {
            lat = latitude.toDouble()
            long = longitud.toDouble()
        } finally {
            return lat != 0.0 || long != 0.0
        }
    }

    companion object {
        var latitude : String = "0.0"
        var longitud : String = "0.0"
    }


}
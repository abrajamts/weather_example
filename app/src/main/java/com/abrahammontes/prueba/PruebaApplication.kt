package com.abrahammontes.prueba

import android.app.Application
import com.abrahammontes.prueba.di.KoinHelper
import com.abrahammontes.prueba.helpers.GeolocationManager

class PruebaApplication : Application() {
    private var mGeolocation: GeolocationManager? = null

    override fun onCreate() {
        super.onCreate()
        ApplicationHolder.INSTANCE.setApplication(this)
        KoinHelper.start(applicationContext)
    }

    fun getInstance(): PruebaApplication {
        return ApplicationHolder.INSTANCE.getApplication()
    }

    override fun onTerminate() {
        KoinHelper.stop()
        super.onTerminate()
    }

    @Synchronized
    fun geolocationManager(): GeolocationManager? {
        if (mGeolocation == null) {
            mGeolocation = GeolocationManager(this.applicationContext)
        }
        return mGeolocation
        return null
    }
}
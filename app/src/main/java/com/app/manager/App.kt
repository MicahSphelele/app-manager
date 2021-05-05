package com.app.manager

import android.app.Application
import com.smn.deviceapps.DeviceAppsSDK

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        DeviceAppsSDK.configureSDK()
            .withContext(this)
            .apply()
    }
}
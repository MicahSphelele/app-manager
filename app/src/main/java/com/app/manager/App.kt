package com.app.manager

import android.app.Application
import com.sdk.miscsdk.MiscSDK

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        MiscSDK.configureSDK()
            .withContext(this)
            .apply()
    }
}
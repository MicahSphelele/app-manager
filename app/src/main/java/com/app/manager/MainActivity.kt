package com.app.manager

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.sdk.miscsdk.DeviceAppsSDK
import com.sdk.miscsdk.model.App
import com.sdk.miscsdk.observer.AppListener
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private lateinit var activity: Activity
    private lateinit var textV: TextView
    private lateinit var button: Button


    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        activity = this@MainActivity

        textV = findViewById(R.id.fullscreenContent)

        button = findViewById(R.id.button)

        CoroutineScope(Dispatchers.Main).launch {
            getApps(object : AppListener{
                override fun onLoad() {
                    CoroutineScope(Dispatchers.Main).launch {
                        textV.text = String.format("%s","Loading...")
                    }
                }

                override fun onError(message: String) {
                    CoroutineScope(Dispatchers.Main).launch {
                        textV.text = String.format("%s",message)
                    }

                }

                override fun onComplete(appsList: List<App>) {
                    CoroutineScope(Dispatchers.Main).launch {
                        textV.text = String.format("%s",appsList[0].toString())

                    }

                }
            })
        }

        var count = 0
        button.setOnClickListener {
            count++
            button.text = String.format("%s",count)
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private suspend fun getApps(appListener: AppListener) = withContext(Dispatchers.Default) {
        DeviceAppsSDK.getInstance().getDeviceApps(appListener)
    }
}
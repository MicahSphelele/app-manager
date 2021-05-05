package com.smn.deviceapps.observer

import com.smn.deviceapps.model.App

interface AppListener {
    fun onLoad()
    fun onError(message: String)
    fun onComplete(appsList: List<App>)
}
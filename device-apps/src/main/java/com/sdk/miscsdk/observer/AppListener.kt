package com.sdk.miscsdk.observer

import com.sdk.miscsdk.model.App

interface AppListener {
    fun onLoad()
    fun onError(message: String)
    fun onComplete(appsList: List<App>)
}
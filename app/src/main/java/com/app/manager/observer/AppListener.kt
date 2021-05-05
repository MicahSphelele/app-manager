package com.app.manager.observer

import com.app.manager.model.App

interface AppListener {

    fun onLoading()
    fun onError(message: String)
    fun onComplete(appsList: List<App>)
}
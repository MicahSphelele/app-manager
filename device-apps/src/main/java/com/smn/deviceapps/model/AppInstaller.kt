package com.smn.deviceapps.model

data class AppInstaller(val installerPackageName: String = "com.unknown.installer",
                        val installerAppName: String = "Unknown Installer") {

    override fun toString(): String {
        return "{installerPackage : $installerPackageName, installerAppName : $installerAppName }"
    }
}

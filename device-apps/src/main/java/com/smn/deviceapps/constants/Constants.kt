package com.smn.deviceapps.constants

import com.smn.deviceapps.model.AppInstaller

object Constants {

    const val UNKNOWN_INSTALLER_PACKAGE = "com.unknown.installer"
    const val UNKNOWN_INSTALLER_LABEL = "Unknown Installer"
    val DEFAULT_INSTALLER = AppInstaller(
        UNKNOWN_INSTALLER_PACKAGE,
        UNKNOWN_INSTALLER_LABEL)
}
package com.smn.deviceapps.model

import android.graphics.drawable.Drawable
import com.smn.deviceapps.constants.Constants
import com.smn.deviceapps.utility.Utils
import java.io.File
import java.util.*

data class DeviceApp(
    val packageName: String? = null,
    val appName: String? = null,
    val appIcon: Drawable? = null,
    val version: String? = null,
    val versionCode: Int? = null,
    val minimumSupportSdk: Int? = null,
    val maximumSupportSdk: Int? = null,
    val installerPackageName: String? = null,
    val firstInstallTime: Date? = null,
    val lastUpdateTime: Date? = null,
    val publicSourceDir: String? = null,
    val permissions: Array<String>? = null,
    val file: File? = null,
    val deviceAppInstaller: AppInstaller = Constants.DEFAULT_INSTALLER
){
    override fun toString(): String {

        return "{package : $packageName, appName : $appName, version : $version, versionCode : $versionCode, " +
                "minimumSupportSdk: $minimumSupportSdk, maximumSupportSdk : $maximumSupportSdk ," +
                "firstInstallTime : $firstInstallTime, lastUpdateTime : $lastUpdateTime, " +
                "publicSourceDir : $publicSourceDir, permissions : ${Arrays.toString(permissions)}," +
                " fileSize : ${Utils.getStringSizeLengthFile(file?.length()!!)} }\n\n"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DeviceApp

        if (packageName != other.packageName) return false
        if (appName != other.appName) return false
        if (appIcon != other.appIcon) return false
        if (version != other.version) return false
        if (versionCode != other.versionCode) return false
        if (minimumSupportSdk != other.minimumSupportSdk) return false
        if (maximumSupportSdk != other.maximumSupportSdk) return false
        if (firstInstallTime != other.firstInstallTime) return false
        if (lastUpdateTime != other.lastUpdateTime) return false
        if (publicSourceDir != other.publicSourceDir) return false
        if (!permissions.contentEquals(other.permissions)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = packageName?.hashCode() ?: 0
        result = 31 * result + (appName?.hashCode() ?: 0)
        result = 31 * result + (appIcon?.hashCode() ?: 0)
        result = 31 * result + (version?.hashCode() ?: 0)
        result = 31 * result + (versionCode ?: 0)
        result = 31 * result + (minimumSupportSdk ?: 0)
        result = 31 * result + (maximumSupportSdk ?: 0)
        result = 31 * result + (firstInstallTime?.hashCode() ?: 0)
        result = 31 * result + (lastUpdateTime?.hashCode() ?: 0)
        result = 31 * result + (publicSourceDir?.hashCode() ?: 0)
        result = 31 * result + permissions.contentHashCode()
        return result
    }
}

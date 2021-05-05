package com.app.manager

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.os.Build
import androidx.annotation.RequiresApi
import com.app.manager.model.App
import com.app.manager.observer.AppListener
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.File
import java.io.IOException
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.text.DecimalFormat
import java.util.*


object AppUtils {

    @SuppressLint("QueryPermissionsNeeded")
    @RequiresApi(Build.VERSION_CODES.R)
    fun getApps(activity: Activity, listener: AppListener)  {

        val apps = mutableListOf<App>()
        listener.onLoading()

        try {
            val pm = activity.packageManager

            for (pi in pm.getInstalledPackages(PackageManager.GET_PERMISSIONS)) {

                val appInfo = pi.applicationInfo

                val appIcon = pm.getApplicationIcon(appInfo.packageName)
                val versionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    pi.longVersionCode
                } else {
                    @Suppress("DEPRECATION")
                    pi.versionCode
                }

                val minimumSupportSdk = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    appInfo.minSdkVersion
                } else {
                   getMinSdkVersion(File(appInfo.sourceDir))
                }

                val app = App(
                    packageName = appInfo.packageName,
                    appName = pm.getApplicationLabel(appInfo).toString(),
                    appIcon = appIcon,
                    version = pi.versionName,
                    versionCode = versionCode.toInt(),
                    minimumSupportSdk = minimumSupportSdk,
                    maximumSupportSdk = appInfo.targetSdkVersion,
                    firstInstallTime = Date(pi.firstInstallTime),
                    lastUpdateTime = Date(pi.lastUpdateTime),
                    publicSourceDir = appInfo.publicSourceDir,
                    permissions = pi.requestedPermissions,
                    file = File(appInfo.publicSourceDir)
                )

                apps.add(app)

            }

            listener.onComplete(apps)

        } catch (ex: Exception) {
            ex.printStackTrace()
            ex.message?.let { listener.onError(it) }
        }

    }

    @Throws(
        ClassNotFoundException::class,
        IllegalAccessException::class,
        InstantiationException::class,
        NoSuchMethodException::class,
        InvocationTargetException::class,
        IOException::class,
        XmlPullParserException::class
    )
    fun getMinSdkVersion(apkFile: File): Int {
        val assetManagerClass = Class.forName("android.content.res.AssetManager")
        val assetManager = assetManagerClass.newInstance() as AssetManager
        val addAssetPath: Method =
            assetManager.javaClass.getMethod("addAssetPath", String::class.java)
        val cookie = addAssetPath.invoke(assetManager, apkFile.absolutePath) as Int
        val parser = assetManager.openXmlResourceParser(cookie, "AndroidManifest.xml")
        while (parser.next() != XmlPullParser.END_DOCUMENT) if (parser.eventType == XmlPullParser.START_TAG && parser.name == "uses-sdk")
            for (i in 0 until parser.attributeCount)
                if (parser.getAttributeNameResource(i) == android.R.attr.minSdkVersion
        ) //alternative, which works most of the times: "minSdkVersion".equals(parser.getAttributeName(i)))
            return parser.getAttributeIntValue(i, -1)
        return -1
    }

    fun getStringSizeLengthFile(size: Long): String {
        val df = DecimalFormat("0.00")
        val sizeKb = 1024.0f
        val sizeMb = sizeKb * sizeKb
        val sizeGb = sizeMb * sizeKb
        val sizeTerra = sizeGb * sizeKb
        return when {
            size < sizeMb -> df.format(size / sizeKb)
                .toString() + " KB"
            size < sizeGb -> df.format(size / sizeMb)
                .toString() + " MB"
            size < sizeTerra -> df.format(size / sizeGb)
                .toString() + " GB"
            else -> ""
        }
    }
}
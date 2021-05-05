package com.smn.deviceapps.utility;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.Drawable;
import android.os.Build;

import com.smn.deviceapps.model.DeviceApp;
import com.smn.deviceapps.observer.AppListener;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Utils {

    @SuppressLint("QueryPermissionsNeeded")
    public static void getDeviceApps(Context context, AppListener listener) {

        try {
            PackageManager pm = context.getPackageManager();

            List<DeviceApp> list = new ArrayList<>();

            listener.onLoad();

            for (PackageInfo pi : pm.getInstalledPackages(PackageManager.GET_PERMISSIONS)) {

                ApplicationInfo appInfo = pi.applicationInfo;
                Drawable appIcon = pm.getApplicationIcon(appInfo.packageName);

                int versionCode;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {

                    versionCode = (int) pi.getLongVersionCode();

                } else {

                    versionCode = pi.versionCode;

                }

                int minimumSupportSdk;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                    minimumSupportSdk = appInfo.minSdkVersion;

                } else {

                    minimumSupportSdk = getMinSdkVersion(new File(appInfo.sourceDir));

                }

                String installerPackageName = pm.getInstallerPackageName(appInfo.packageName);

                DeviceApp deviceApp = new DeviceApp(appInfo.packageName,
                        pm.getApplicationLabel(appInfo).toString(),
                        appIcon,
                        pi.versionName,
                        versionCode,
                        minimumSupportSdk,
                        appInfo.targetSdkVersion,
                        installerPackageName,
                        new Date(pi.firstInstallTime),
                        new Date(pi.lastUpdateTime),
                        appInfo.publicSourceDir,
                        pi.requestedPermissions,
                        new File(appInfo.publicSourceDir));

                list.add(deviceApp);
            }

            listener.onComplete(list);

        } catch (Exception ex) {
            ex.printStackTrace();
            listener.onError(ex.getMessage());
        }
    }

    public static String getStringSizeLengthFile(long size) {

        DecimalFormat df = new DecimalFormat("0.00");
        float sizeKb = 1024.0f;
        float sizeMb = sizeKb * sizeKb;
        float sizeGb = sizeMb * sizeKb;
       //float sizeTerra = sizeGb * sizeKb;
        String stringSize;

        if (size < sizeMb) {
            stringSize = df.format(size / sizeKb) + " KB";
        } else if (size < sizeGb) {
            stringSize = df.format(size / sizeMb) + " MB";
        } else {

            stringSize = df.format(size / sizeGb) + " GB";
        }

        return stringSize;
    }

    @SuppressWarnings({"rawtypes", "JavaReflectionMemberAccess"})
    public static int getMinSdkVersion(File apkFile) throws ClassNotFoundException, IllegalAccessException,
            InstantiationException, NoSuchMethodException,
            InvocationTargetException, IOException, XmlPullParserException {

        final Class assetManagerClass = Class.forName("android.content.res.AssetManager");
        final AssetManager assetManager = (AssetManager) assetManagerClass.newInstance();
        final Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
        final int cookie = (Integer) addAssetPath.invoke(assetManager, apkFile.getAbsolutePath());
        final XmlResourceParser parser = assetManager.openXmlResourceParser(cookie, "AndroidManifest.xml");
        while (parser.next() != XmlPullParser.END_DOCUMENT)
            if (parser.getEventType() == XmlPullParser.START_TAG && parser.getName().equals("uses-sdk"))
                for (int i = 0; i < parser.getAttributeCount(); ++i)
                    if (parser.getAttributeNameResource(i) == android.R.attr.minSdkVersion)//alternative, which works most of the times: "minSdkVersion".equals(parser.getAttributeName(i)))
                        return parser.getAttributeIntValue(i, -1);
        return -1;
    }

}

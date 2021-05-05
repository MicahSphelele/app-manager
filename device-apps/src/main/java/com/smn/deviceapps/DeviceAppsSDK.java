package com.smn.deviceapps;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.smn.deviceapps.exceptions.SDKAlreadyConfiguredException;
import com.smn.deviceapps.observer.AppListener;
import com.smn.deviceapps.utility.Utils;

import org.jetbrains.annotations.NotNull;

public class DeviceAppsSDK {

    @SuppressLint("StaticFieldLeak")
    private static DeviceAppsSDK instance = null;

    public static DeviceAppsSDK.Builder configureSDK() {
        if (instance != null) {
            throw new SDKAlreadyConfiguredException();
        }
        return new Builder();
    }

    @NotNull
    private final Context context;

    DeviceAppsSDK(@NotNull Context context){
        this.context = context;
    }

    public static DeviceAppsSDK getInstance() {
        return instance;
    }

    @NotNull
    public Context getContext() {
        return context;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public void getDeviceApps(AppListener listener){
        Utils.getDeviceApps(this.context,listener);
    }

    public static class Builder {

        Context context;

        Builder() {

        }

        public DeviceAppsSDK.Builder withContext(@NotNull Context context) {
            this.context = context;
            return this;
        }

        public void apply(){

            DeviceAppsSDK.instance = new DeviceAppsSDK(this.context);
        }
    }
}

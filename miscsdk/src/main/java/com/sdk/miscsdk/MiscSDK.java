package com.sdk.miscsdk;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.sdk.miscsdk.exceptions.SDKAlreadyConfiguredException;
import com.sdk.miscsdk.observer.AppListener;
import com.sdk.miscsdk.utility.Utils;

import org.jetbrains.annotations.NotNull;

public class MiscSDK {

    private static MiscSDK instance = null;

    public static MiscSDK.Builder configureSDK() {
        if (instance != null) {
            throw new SDKAlreadyConfiguredException();
        }
        return new Builder();
    }

    @NotNull
    private final Context context;

    MiscSDK(@NotNull Context context){
        this.context = context;
    }

    public static MiscSDK getInstance() {
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

        public MiscSDK.Builder withContext(@NotNull Context context) {
            this.context = context;
            return this;
        }

        public void apply(){

            MiscSDK.instance = new MiscSDK(this.context);
        }
    }
}

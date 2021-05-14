package com.smn.deviceapps;

import static org.mockito.Mockito.mock;

import android.content.Context;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;

public class DeviceAppsSDKTest {

    private DeviceAppsSDK sdk;

    @Before
    public void setUp() throws NoSuchFieldException, IllegalAccessException {

        Field instanceField = DeviceAppsSDK.class.getDeclaredField("instance");
        instanceField.setAccessible(true);
        instanceField.set(null, null);

        sdk = new DeviceAppsSDK(mock(Context.class));
    }

    @Test
    public void testContextNotNull() {

        Assert.assertNotNull(sdk.getContext());
    }

}
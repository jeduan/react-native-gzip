package com.react_native_gzip;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

public class RNGZipManager extends ReactContextBaseJavaModule {
    public RNGZipManager(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "RNGZipManager";
    }

    @ReactMethod
    public void gunzip(String fileName, String dest, Boolean force, Promise promise) {
        System.out.format("filename: %s\ndest: %s\nforce: %b", fileName, dest, force);
        promise.resolve("OK");
    }
}

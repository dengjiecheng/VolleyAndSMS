package com.example.volleytest;

import android.app.Application;
import android.content.res.Configuration;

import com.example.volleytest.Utils.L;

/**
 * *************************************
 *
 * @version V1.0
 *          <p>
 *          ***********************************
 * @Description: application操作类
 * @author: John.Lee
 * @date:2015-1-14
 */
public class MainApp extends Application {
    private String TAG = "MainApp";
    private static MainApp mInstance;//

    public static Application getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        L.d(TAG, "MainApp->onCreate");

    }

    @Override
    public void onTerminate() {
        // 程序终止的时候执行
        L.d(TAG, "MainApp->onTerminate");
        super.onTerminate();
    }

    @Override
    public void onLowMemory() {
        // 低内存的时候执行
        L.d(TAG, "MainApp->onLowMemory");
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        // 程序在内存清理的时候执行
        L.d(TAG, "MainApp->onTrimMemory");
        super.onTrimMemory(level);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        L.d(TAG, "MainApp->onConfigurationChanged");
        super.onConfigurationChanged(newConfig);
    }

}

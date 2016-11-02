package com.example.administrator.recycleviewdemo.app;

import android.app.Application;

import org.xutils.BuildConfig;
import org.xutils.x;
/**
 * Created by Administrator on 2016/10/26.
 */

public class GlobalApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG);
    }
}

package com.mxk.qianghongbao;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.mxk.qianghongbao.services.QHBService;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * Created by xiankuimeng on 16-10-25 10:07.
 */
public class MyApp extends Application {

    private static MyApp instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        CrashReport.initCrashReport(getApplicationContext(), "b41c882928", false);
        if (!isServiceRunning())
        startService(instance);
    }

    private boolean isServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("com.mxk.qianghongbao.services.QHBService".equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void startService(Context context) {
        Intent service = new Intent(context,QHBService.class);
        context.startService(service);
    }
}

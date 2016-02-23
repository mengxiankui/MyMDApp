package com.example.application;

import com.mxk.baseapplication.LBaseApplication;
import com.tencent.bugly.crashreport.CrashReport;


public class MyApplication extends LBaseApplication
{
    @Override
    public void onCreate() {
        super.onCreate();
        CrashReport.initCrashReport(getApplicationContext(), "注册时申请的APPID", false);
    }
}
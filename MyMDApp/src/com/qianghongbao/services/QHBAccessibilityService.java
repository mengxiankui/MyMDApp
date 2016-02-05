package com.qianghongbao.services;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.view.Display;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import com.example.mymdapp.MainActivity;
import com.example.util.Consts;
import com.example.util.TimeUtil;
import com.mxk.baseframe.util.log.Logger;
import com.qianghongbao.helper.JobHelper;
import com.qianghongbao.job.IAccessibilityJob;

import java.util.Iterator;
import java.util.List;

public class QHBAccessibilityService extends AccessibilityService
    implements IAccessibilityJob.IQHBCallBack
{
    private static final String LOG_TAG =
        QHBAccessibilityService.class.getSimpleName();

    private IAccessibilityJob accessibilityJob;

    private static QHBAccessibilityService service;

    private PowerManager pm;

    private PowerManager.WakeLock wl;


    @Override
    public void onCreate()
    {
        super.onCreate();
        accessibilityJob = JobHelper.getQHBJob();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        service = null;
    }

    @Override
    protected void onServiceConnected()
    {
        super.onServiceConnected();
        Logger.d(LOG_TAG, "onServiceConnected !");
        service = this;
        accessibilityJob.setService(service);
        Toast.makeText(QHBAccessibilityService.this,
            "抢红包服务已连接",
            Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onInterrupt()
    {
        Logger.d(LOG_TAG, "onInterrupt !");
        Toast.makeText(QHBAccessibilityService.this,
            "抢红包服务被打断",
            Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onUnbind(Intent intent)
    {
        Logger.d(LOG_TAG, "onUnbind !");
        service = null;
        Toast.makeText(QHBAccessibilityService.this,
            "抢红包服务已断开",
            Toast.LENGTH_SHORT).show();
        return super.onUnbind(intent);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event)
    {
        Logger.d(LOG_TAG, "onAccessibilityEvent !");
        accessibilityJob.onReceiveJob(event);
    }

    public static boolean isRunning()
    {
        if (null == service)
        {
            return false;
        }
        AccessibilityManager accessibilityManager =
            (AccessibilityManager)service
                .getSystemService(Context.ACCESSIBILITY_SERVICE);
        AccessibilityServiceInfo serviceInfo = service.getServiceInfo();
        List<AccessibilityServiceInfo> list =
            accessibilityManager.getEnabledAccessibilityServiceList(
                AccessibilityServiceInfo.FEEDBACK_GENERIC);
        boolean running = false;
        Iterator<AccessibilityServiceInfo> iterator = list.iterator();
        while (iterator.hasNext())
        {
            AccessibilityServiceInfo info = iterator.next();
            if (info.getId() == serviceInfo.getId())
            {
                running = true;
                break;
            }
        }
        return running;
    }

    @Override
    public boolean wakeUp(boolean wakeup)
    {
        Logger.d(LOG_TAG, "wakeUp = " + wakeup);
        if (wakeup)
        {
            Logger.d(LOG_TAG, "wakeUp!");
            //获取电源管理器对象
            pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
            boolean isScreenOn = pm.isScreenOn();

            if (isScreenOn)
            {
                return false;
            }

            //获取PowerManager.WakeLock对象，后面的参数|表示同时传入两个值，最后的是调试用的Tag
            wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP
                    | PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "bright");

            //点亮屏幕
            wl.acquire();
        } else {
            Logger.d(LOG_TAG, "lockUp!" );

            //释放wakeLock，关灯
            if (null != wl)
            {
                wl.release();
            }

        }
        return true;
    }

    @Override
    public AccessibilityNodeInfo getRoot() {
        return getRootInActiveWindow();
    }

    @Override
    public void addMoney(float money, String name)
    {
        Logger.d(LOG_TAG, "addMoney,money = " + money + ", name = " + name);

        ContentValues contentValues = new ContentValues();
        contentValues.put(Consts.WeixinQHBConst.MONEY, money);
        contentValues.put(Consts.WeixinQHBConst.NAME, name);
        contentValues.put(Consts.WeixinQHBConst.DATE,
            TimeUtil.getDBDateFormat());
        getContentResolver().insert(
            Consts.WeixinQHBConst.CONTENT_URI, contentValues);

    }

    @Override
    public void quitPage()
    {
        Logger.d(LOG_TAG, "quitPage!" );
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
//        Intent intent = new Intent(getBaseContext(), MainActivity.class);
//        getApplication().startActivity(intent);

    }

}

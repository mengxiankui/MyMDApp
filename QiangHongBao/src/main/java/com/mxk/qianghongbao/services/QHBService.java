package com.mxk.qianghongbao.services;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import com.mxk.qianghongbao.MainActivity;
import com.mxk.qianghongbao.MyApp;
import com.mxk.qianghongbao.R;
import com.mxk.qianghongbao.helper.JobHelper;
import com.mxk.qianghongbao.job.IAccessibilityJob;
import com.mxk.qianghongbao.utils.Consts;
import com.mxk.qianghongbao.utils.TimeUtil;

import java.util.Iterator;
import java.util.List;

public class QHBService extends AccessibilityService
    implements IAccessibilityJob.IQHBCallBack
{
    private static final String LOG_TAG =
        QHBService.class.getSimpleName();

    private static final int NOTIFICATION_ID = 0x111;

    private IAccessibilityJob accessibilityJob;

    private static QHBService service;

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
        stopForeground(true);
        super.onDestroy();
        service = null;
    }

    @Override
    protected void onServiceConnected()
    {
        super.onServiceConnected();
        Log.d(LOG_TAG, "onServiceConnected !");
        service = this;
        accessibilityJob.setService(service);
        Toast.makeText(QHBService.this,
            "抢红包服务已连接",
            Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onInterrupt()
    {
        Log.d(LOG_TAG, "onInterrupt !");
        Toast.makeText(QHBService.this,
            "抢红包服务被打断",
            Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onUnbind(Intent intent)
    {
        Log.d(LOG_TAG, "onUnbind !");
        service = null;
        Toast.makeText(QHBService.this,
            "抢红包服务已断开",
            Toast.LENGTH_SHORT).show();
        return super.onUnbind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        Notification.Builder builder = new Notification.Builder(this);
//        builder.setContentText("抢红包中");
//        builder.setContentTitle("抢红包在后台运行");
//        builder.setSmallIcon(R.drawable.ic_launcher);
//        builder.setTicker("新消息");
//        builder.setAutoCancel(true);
//        builder.setWhen(System.currentTimeMillis());
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), PendingIntent.FLAG_CANCEL_CURRENT);
//        builder.setContentIntent(pendingIntent);
//        Notification notification = builder.build();
//        startForeground(NOTIFICATION_ID, notification);
        return START_STICKY;
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event)
    {
        Log.d(LOG_TAG, "onAccessibilityEvent !");
        accessibilityJob.onReceiveJob(event);
    }

    @Override
    public boolean wakeUp(boolean wakeup)
    {
        Log.d(LOG_TAG, "wakeUp = " + wakeup);
        if (wakeup)
        {
            Log.d(LOG_TAG, "wakeUp!");
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
            Log.d(LOG_TAG, "lockUp!" );

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
        Log.d(LOG_TAG, "addMoney,money = " + money + ", name = " + name);

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
        Log.d(LOG_TAG, "quitPage!" );
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
//        Intent intent = new Intent(getBaseContext(), MainActivity.class);
//        getApplication().startActivity(intent);

    }

}

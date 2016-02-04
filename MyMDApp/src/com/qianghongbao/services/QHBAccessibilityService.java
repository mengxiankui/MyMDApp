package com.qianghongbao.services;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.widget.Toast;

import com.mxk.baseframe.util.log.Logger;
import com.qianghongbao.helper.JobHelper;
import com.qianghongbao.job.IAccessibilityJob;

import java.util.Iterator;
import java.util.List;

public class QHBAccessibilityService extends AccessibilityService {
    private static final String LOG_TAG = QHBAccessibilityService.class.getSimpleName();
    private IAccessibilityJob accessibilityJob;

    private static QHBAccessibilityService service;

    @Override
    public void onCreate() {
        super.onCreate();
        accessibilityJob = JobHelper.getQHBJob();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        service = null;
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        service = this;
        Toast.makeText(QHBAccessibilityService.this, "抢红包服务已连接", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onInterrupt() {
        Toast.makeText(QHBAccessibilityService.this, "抢红包服务已断开", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
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
                (AccessibilityManager) service.getSystemService(Context.ACCESSIBILITY_SERVICE);
        AccessibilityServiceInfo serviceInfo = service.getServiceInfo();
        List<AccessibilityServiceInfo> list =  accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC);
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

    public static void quitPage() {
        if (null != service)
        {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addCategory(Intent.CATEGORY_HOME);
            service.startActivity(intent);
        }

    }

    public static QHBAccessibilityService getService()
    {
        return service;
    }

}

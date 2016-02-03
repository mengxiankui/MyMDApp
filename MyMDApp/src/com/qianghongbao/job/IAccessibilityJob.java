package com.qianghongbao.job;

import android.view.accessibility.AccessibilityEvent;

import com.qianghongbao.services.QHBAccessibilityService;

/**
 * Created by xiankuimeng on 16-2-3.
 */
public abstract class IAccessibilityJob
{
    public abstract void onReceiveJob(AccessibilityEvent event);
}

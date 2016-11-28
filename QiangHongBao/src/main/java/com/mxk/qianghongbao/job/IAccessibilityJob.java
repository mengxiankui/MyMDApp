package com.mxk.qianghongbao.job;

import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

/**
 * Created by xiankuimeng on 16-2-3.
 */
public abstract class IAccessibilityJob
{

    protected  IQHBCallBack service;

    public IQHBCallBack getService() {
        return service;
    }

    public void setService(IQHBCallBack service) {
        this.service = service;
    }

    public abstract void onReceiveJob(AccessibilityEvent event);

    public interface IQHBCallBack
    {
        public boolean wakeUp(boolean wakeup);

        public AccessibilityNodeInfo getRoot();
        public void addMoney(float money, String name);

        public void quitPage();

    }


}

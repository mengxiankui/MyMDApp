package com.qianghongbao.job;

import android.app.Notification;
import android.app.PendingIntent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.mxk.baseframe.util.log.Logger;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by xiankuimeng on 16-2-3.
 */
public class QHBAccessibilityJob extends IAccessibilityJob
{
    private static final String LOG_TAG =
        QHBAccessibilityJob.class.getSimpleName();

    public static boolean isSuperMode()
    {
        return isSuperMode;
    }

    public static void setMode(boolean isSuperMode)
    {
        QHBAccessibilityJob.isSuperMode = isSuperMode;
    }

    private static boolean isSuperMode = true;

    private boolean isFirstChecked = false;

    private boolean isCanSucceed = false;

    private boolean isSucceed = false;

    /**
     * 红包消息的关键字
     */
    private static final String HONGBAO_TEXT_KEY = "[微信红包]";

    private void initFirstChecked()
    {

        isFirstChecked = false;

    }

    @Override
    public void onReceiveJob(AccessibilityEvent event)
    {
        Logger.d(LOG_TAG, "onReceiveJob !" + event.getClassName());
        //        initFirstChecked();
        int type = event.getEventType();
        if (type == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED)
        {
            List<CharSequence> texts = event.getText();
            if (!texts.isEmpty())
            {
                for (CharSequence t : texts)
                {
                    String text = String.valueOf(t);
                    if (text.contains(HONGBAO_TEXT_KEY))
                    {
                        openNotify(event);
                        break;
                    }
                }
            }
        }
        else if (type == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED)
        {
            openHongBao(event);
        }
    }

    /**
     * 打开通知栏消息
     */
    private void openNotify(AccessibilityEvent event)
    {
        Logger.d(LOG_TAG, "openNotify !" + event.getClassName());
        if (event.getParcelableData() == null
            || !(event.getParcelableData() instanceof Notification))
        {
            return;
        }

        //以下是精华，将微信的通知栏消息打开
        Notification notification = (Notification)event.getParcelableData();
        final PendingIntent pendingIntent = notification.contentIntent;

        try
        {
            isFirstChecked = true;
            isCanSucceed = false;
            isSucceed = false;
            if (null != service)
            {
                if (!service.wakeUp(true))
                {
                    Logger.d(LOG_TAG, "openNotify ! 0");
                    pendingIntent.send();
                }
            }
        }
        catch (Exception e)
        {
            initFirstChecked();
            isCanSucceed = false;
            isSucceed = false;
            e.printStackTrace();
        }
    }

    //            else
    //            {
    //                Timer timer = new Timer("openNotifyDelay");
    //                timer.schedule(new TimerTask() {
    //                    @Override
    //                    public void run() {
    //                        if (null != service)
    //                        {
    //                            Logger.d(LOG_TAG, "openNotify ! 1" );
    //                            try
    //                            {
    //                                isFirstChecked = true;
    //                                isCanSucceed = false;
    //                                isSucceed = false;
    //                                pendingIntent.send();
    //                            }
    //                            catch (Exception e)
    //                            {
    //                                initFirstChecked();
    //                                isCanSucceed = false;
    //                                isSucceed = false;
    //                                e.printStackTrace();
    //                            }
    //                        }
    //
    //                    }
    //                },5000);
    //            }




    private void openHongBao(AccessibilityEvent event)
    {
        Logger.d(LOG_TAG, "openHongBao !" + event.getClassName());
        if ("com.tencent.mm.ui.LauncherUI".equals(event.getClassName()))
        {
            //在聊天界面,去点中红包
            handleChatListHongBao();
        }
        else if ("com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyReceiveUI"
            .equals(event.getClassName()))
        {
            //点中了红包，下一步就是去拆红包
            handleLuckyMoneyReceive();
        }
        else if ("com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyDetailUI"
            .equals(event.getClassName()))
        {
            //拆完红包后看详细的纪录界面
            handleLuckyMoneyDetail();
            //nonething
        }
    }

    private void handleLuckyMoneyDetail()
    {

        if (isFirstChecked)
        {
            if (isCanSucceed)
            {
                isSucceed = true;
                float num = 0;
                String name = "";
                if (null != service)
                {
                    AccessibilityNodeInfo mNodeInfo = service.getRoot();
                    List<AccessibilityNodeInfo> findNodes =
                        mNodeInfo.findAccessibilityNodeInfosByViewId(
                            "com.tencent.mm:id/b4e");
                    Logger.d(LOG_TAG,
                        "handleLuckyMoneyDetail ,findNodes.size() = "
                            + findNodes.size());
                    //                for (AccessibilityNodeInfo info : findNodes)
                    //                {
                    //                    Logger.d(LOG_TAG,"handleLuckyMoneyDetail ,info = " +info.getText().toString());
                    //                }
                    if (findNodes.size() > 0)
                    {
                        num = Float
                            .valueOf(findNodes.get(0).getText().toString());
                        Logger.d(LOG_TAG,
                            "handleLuckyMoneyDetail ,money = " + num);
                    }

                    findNodes = mNodeInfo.findAccessibilityNodeInfosByViewId(
                        "com.tencent.mm:id/b4a");
                    Logger.d(LOG_TAG,
                        "handleLuckyMoneyDetail ,findNodes.size() = "
                            + findNodes.size());
                    //                for (AccessibilityNodeInfo info : findNodes)
                    //                {
                    //                    Logger.d(LOG_TAG,"handleLuckyMoneyDetail ,info = " +info.getText().toString());
                    //                }
                    if (findNodes.size() > 0)
                    {
                        name = findNodes.get(0).getText().toString();
                        Logger.d(LOG_TAG,
                            "handleLuckyMoneyDetail ,name = " + name);

                    }
                    if (null != service)
                    {
                        service.addMoney(num, name);
                    }
                }

            }
        }
        if (isSuperMode || isFirstChecked)
        {
            if (null != service)
            {
                AccessibilityNodeInfo mNodeInfo = service.getRoot();
                List<AccessibilityNodeInfo> findNodes =
                    mNodeInfo.findAccessibilityNodeInfosByViewId(
                        "com.tencent.mm:id/c4u");
                for (AccessibilityNodeInfo nodeInfo : findNodes)
                {
                    nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                }
            }
        }

        quitPage();

    }

    private void handleLuckyMoneyReceive()
    {
        if (null != service)
        {
            AccessibilityNodeInfo mNodeInfo = service.getRoot();
            List<AccessibilityNodeInfo> findNodes = mNodeInfo
                .findAccessibilityNodeInfosByViewId("com.tencent.mm:id/b43");

            if (findNodes.size() == 0)
            {
                findNodes = mNodeInfo.findAccessibilityNodeInfosByViewId(
                    "com.tencent.mm:id/b47");
                for (AccessibilityNodeInfo nodeInfo : findNodes)
                {
                    nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                }
                isSucceed = false;
                quitPage();
            }
            else
            {
                for (AccessibilityNodeInfo nodeInfo : findNodes)
                {
                    nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                }
                isCanSucceed = true;
            }
        }

    }

    private void handleChatListHongBao()
    {
        if (isFirstChecked)
        {
            Timer timer = new Timer();
            timer.schedule(new TimerTask()
            {
                @Override
                public void run()
                {
                    if (null != service)
                    {
                        AccessibilityNodeInfo mNodeInfo = service.getRoot();
                        List<AccessibilityNodeInfo> findNodes =
                            mNodeInfo.findAccessibilityNodeInfosByViewId(
                                "com.tencent.mm:id/b_");
                        Logger.d(LOG_TAG,
                            "handleChatListHongBao !findNodes.size() = "
                                + findNodes.size());
                        if (findNodes.size() > 0)
                        {
                            Logger.d(LOG_TAG, "find hongbao Node !");
                            final AccessibilityNodeInfo node =
                                findNodes.get(findNodes.size() - 1);
                            node.performAction(
                                AccessibilityNodeInfo.ACTION_CLICK);
                        }
                    }

                }
            }, 500);

        }

    }

    private void quitPage()
    {
        if (isFirstChecked)
        {
            if (null != service)
                service.quitPage();
            service.wakeUp(false);
        }
        isFirstChecked = false;

    }

}

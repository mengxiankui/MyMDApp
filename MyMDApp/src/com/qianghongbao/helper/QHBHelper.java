package com.qianghongbao.helper;

import com.qianghongbao.job.QHBAccessibilityJob;

/**
 * Created by xiankuimeng on 16-2-3.
 */
public class QHBHelper {

    public static void changeMode()
    {
        QHBAccessibilityJob.setMode(!QHBAccessibilityJob.isSuperMode());
    }
    public static boolean getMode()
    {
        return QHBAccessibilityJob.isSuperMode();
    }
}

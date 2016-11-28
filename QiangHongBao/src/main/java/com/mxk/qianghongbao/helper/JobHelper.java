package com.mxk.qianghongbao.helper;

import com.mxk.qianghongbao.job.IAccessibilityJob;
import com.mxk.qianghongbao.job.QHBAccessibilityJob;

/**
 * Created by xiankuimeng on 16-2-4.
 */
public class JobHelper {

    public static IAccessibilityJob getQHBJob()
    {
        return new QHBAccessibilityJob();
    }
}

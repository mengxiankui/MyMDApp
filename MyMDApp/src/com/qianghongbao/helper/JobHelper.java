package com.qianghongbao.helper;

import com.qianghongbao.job.IAccessibilityJob;
import com.qianghongbao.job.QHBAccessibilityJob;

/**
 * Created by xiankuimeng on 16-2-4.
 */
public class JobHelper {

    public static IAccessibilityJob getQHBJob()
    {
        return new QHBAccessibilityJob();
    }
}

package com.mxk.baseframe.common;


import java.io.File;

import android.content.Context;


public class CacheDirUtil
{

    public static File getDiskCacheDir(Context context, String uniqueName)
    {
        if (AndroidVersionCheckUtils.hasFroyo())
        {
            return ExternalOverFroyoUtils.getDiskCacheDir(context, uniqueName);
        }
        else
        {
            return ExternalUnderFroyoUtils.getDiskCacheDir(context, uniqueName);
        }
    }

}
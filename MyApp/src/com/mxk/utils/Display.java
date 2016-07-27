package com.mxk.utils;

import android.content.Context;


public class Display
{
    public static int getPixel(Context paramContext, float paramFloat)
    {
      return (int)(0.5F + paramFloat * paramContext.getResources().getDisplayMetrics().density);
    }
}
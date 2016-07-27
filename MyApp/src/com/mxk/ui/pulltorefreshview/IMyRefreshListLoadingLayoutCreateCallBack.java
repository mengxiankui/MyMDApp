package com.mxk.ui.pulltorefreshview;


import com.mxk.ui.pulltorefreshview.MyScrollViewBase.ScrollMode;

import android.content.Context;

public abstract interface IMyRefreshListLoadingLayoutCreateCallBack
{
  public abstract MyLoadingLayoutBase createLoadingLayout(Context paramContext, ScrollMode paramScrollMode);
}
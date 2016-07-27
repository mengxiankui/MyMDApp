package com.mxk.ui.pulltorefreshview;


import com.mxk.ui.pulltorefreshview.MyScrollViewBase.ScrollDirection;
import com.mxk.ui.pulltorefreshview.MyScrollViewBase.ScrollMode;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public abstract class MyLoadingLayoutBase extends FrameLayout
{
  protected ScrollDirection mScrollDirection;
  protected ScrollMode mScrollMode;

  public MyLoadingLayoutBase(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public MyLoadingLayoutBase(Context paramContext, ScrollDirection paramScrollDirection, ScrollMode paramScrollMode)
  {
    super(paramContext);
    this.mScrollDirection = paramScrollDirection;
    this.mScrollMode = paramScrollMode;
  }

  public abstract int getContentSize();

  public abstract int getTriggerSize();

  public abstract void hideAllSubViews();

  public abstract void loadFail();

  public abstract void loadFinish(String paramString);

  public abstract void loadSuc();

  public abstract void onPull(int paramInt);

  public abstract void pullToRefresh();

  public abstract void refreshFail();

  public abstract void refreshSuc();

  public abstract void refreshing();

  public abstract void releaseToRefresh();

  public abstract void reset();

  public abstract void setHeight(int paramInt);

  public abstract void setWidth(int paramInt);

  public abstract void showAllSubViews();
}
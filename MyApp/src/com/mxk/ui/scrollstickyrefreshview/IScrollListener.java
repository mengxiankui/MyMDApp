package com.mxk.ui.scrollstickyrefreshview;

import android.view.View;
import android.widget.AbsListView;

public abstract interface IScrollListener
{
  public abstract void onScroll(View paramView, int paramInt1, int paramInt2, int paramInt3);

  public abstract void onScrollStateChanged(AbsListView paramAbsListView, int paramInt);
}
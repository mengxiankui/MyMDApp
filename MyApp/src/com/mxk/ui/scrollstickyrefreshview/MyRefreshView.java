package com.mxk.ui.scrollstickyrefreshview;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;


public abstract class MyRefreshView extends MyRefreshScrollViewBase<LinearLayout>
{
    private ListView listView;
    private ScrollView scrollView;
    private LinearLayout header;
    private LinearLayout body;

    public MyRefreshView(Context paramContext, AttributeSet paramAttributeSet)
    {
        super(paramContext, paramAttributeSet);
    }

    public MyRefreshView(Context paramContext)
    {
        super(paramContext, ScrollDirection.SCROLL_DIRECTION_VERTICAL);
    }

    protected LinearLayout createScrollContentView(Context paramContext)
    {
        LinearLayout locallayout = new LinearLayout(paramContext);
        locallayout.setOrientation(LinearLayout.VERTICAL);
        header = createContentHeaderView(paramContext);
        if (header != null)
        {
            locallayout.addView(header);
        }
        body = createContentBodyView(paramContext);
        if (body != null)
        {
            locallayout.addView(body);
//            body.setOnTouchListener(new OnTouchListener()
//            {
//
//                @Override
//                public boolean onTouch(View v, MotionEvent event)
//                {
//                    // TODO Auto-generated method stub
//                    return true;
//                }
//            });
        }
        
        return locallayout;
    }
    
    protected abstract LinearLayout createContentHeaderView(Context paramContext);
    
    protected abstract LinearLayout createContentBodyView(Context paramContext);
    

    protected void onLoadFinish(boolean bSuccess)
    {
        int scrollto;
        if (mHeaderLayout != null)
        {
            scrollto = -mHeaderLayout.getContentSize();

            if (bSuccess)
            {
                mHeaderLayout.refreshSuc();
            }
            else
            {
                mHeaderLayout.refreshFail();
            }

            contentViewScrollTo(scrollto);
            setState(RefreshState.RESET);
//            mHeaderLayout.postDelayed(new Runnable()
//            {
//
//                @Override
//                public void run()
//                {
//                    // TODO Auto-generated method stub
//                    setState(RefreshState.RESET);
//                }
//            }, 900);
        }

    }

    protected void onRefreshing()
    {
        int scrollto = 0;
        if (mHeaderLayout != null)
        {
            mHeaderLayout.refreshing();
            scrollto = -mHeaderLayout.getContentSize();
        }

        mLayoutVisibilityChangesEnabled = false;
        //        contentViewScrollTo(scrollto);
        smoothScrollTo(scrollto, new SmoothScrollRunnableListener());

    }

    protected void onReset()
    {
        if (mHeaderLayout != null)
        {
            mHeaderLayout.reset();
            contentViewScrollTo(0);

        }
        super.onReset();
    }

    public void reset()
    {
        if (mRefreshState == RefreshState.REFRESH_LOAD_FINISH)
            setState(MyRefreshScrollViewBase.RefreshState.RESET);
    }

    @Override
    protected boolean isReadyForScrollHeader()
    {
        // TODO Auto-generated method stub
        if (null == header)
        {
            return false;
        }
        if (mScrollContentView.getScrollY() != header.getBottom())
        {
            return false;
        }
        if (null != listView)
        {
            ListAdapter localListAdapter = listView.getAdapter();
            if ((localListAdapter == null) || (localListAdapter.isEmpty() == true))
                return true;
            if (listView.getFirstVisiblePosition() <= 0)
            {
                View localView = listView.getChildAt(0);
                if (localView != null)
                    return localView.getTop() >= 0;
            }
        }
        else if (null != scrollView) 
        {
            if (scrollView.getScrollY() == 0)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return true;
        }
        return false;
    }
    
    @Override
    protected boolean isDuringScrollHeader()
    {
        // TODO Auto-generated method stub
        if (null == header)
        {
            return false;
        }
        if (mScrollContentView.getScrollY() >= 0
            && mScrollContentView.getScrollY() < header.getBottom())
        {
            return true;
        }
        return false;
    }

    protected boolean isReadyForScrollStart()
    {
        if (mScrollContentView.getScrollY() == 0)
        {
            if (null != listView)
            {
                ListAdapter localListAdapter = listView.getAdapter();
                if ((localListAdapter == null) || (localListAdapter.isEmpty() == true))
                    return true;
                if (listView.getFirstVisiblePosition() <= 0)
                {
                    View localView = listView.getChildAt(0);
                    if (localView != null)
                        return localView.getTop() >= 0;
                }
            }
            else if (null != scrollView) 
            {
                if (scrollView.getScrollY() == 0)
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
            else
            {
                return true; 
            }
        }
        return false;
    }

    @Override
    protected int getHeaderHeight()
    {
        // TODO Auto-generated method stub
        return header.getHeight();
    }

    protected MyLoadingLayoutBase createLoadingLayout(Context paramContext)
    {

        return new RefreshListLoading(paramContext);
    }

    @Override
    protected boolean isContentFullScreen()
    {
        // TODO Auto-generated method stub
        return false;
    }

    public LinearLayout getHeader()
    {
        return header;
    }

    public LinearLayout getViewpager()
    {
        return body;
    }

    public void setListView(ListView listView)
    {
        this.listView = listView;
    }
    
    public void setScrollView(ScrollView scrollView)
    {
        this.scrollView = scrollView;
    }
}
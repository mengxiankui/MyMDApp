package com.mxk.ui.pulltorefreshview;



import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;


public class MyRefreshListView extends MyRefreshScrollViewBase<ListView>
{
    protected IMyRefreshListLoadingLayoutCreateCallBack mCreateLoadingLayoutCallBack = null;
    protected MyLoadingLayoutBase mFooterLoadingView;
    protected MyLoadingLayoutBase mHeaderLoadingView;
    protected ListAdapter mRawAdapter;

    public MyRefreshListView(Context paramContext, AttributeSet paramAttributeSet)
    {
        super(paramContext, paramAttributeSet);
    }

    public MyRefreshListView(Context paramContext, ScrollMode paramScrollMode)
    {
        super(paramContext, ScrollDirection.SCROLL_DIRECTION_VERTICAL, paramScrollMode);
    }

    public void addFooterView(View paramView)
    {
        if ((mScrollMode == ScrollMode.PULL_FROM_START)
            || (mScrollMode == ScrollMode.NONE))
            ((ListView) mScrollContentView).addFooterView(paramView);
    }

    public void addHeaderView(View paramView)
    {
        if ((mScrollMode == ScrollMode.PULL_FROM_END) || (mScrollMode == ScrollMode.NONE))
            ((ListView) mScrollContentView).addHeaderView(paramView);
    }

    protected MyLoadingLayoutBase buildListViewHeadOrFootView(Context paramContext,
            ListView paramListView, ScrollMode paramScrollMode)
    {
        FrameLayout localFrameLayout = new FrameLayout(getContext());
        FrameLayout.LayoutParams localLayoutParams = new FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT,
            1);
        MyLoadingLayoutBase localMyLoadingLayoutBase = createLoadingLayout(paramContext,
            paramScrollMode);
        localMyLoadingLayoutBase.setVisibility(View.GONE);
        localFrameLayout.addView(localMyLoadingLayoutBase, localLayoutParams);
        if (paramScrollMode == MyScrollViewBase.ScrollMode.PULL_FROM_START)
        {
            paramListView.addHeaderView(localFrameLayout, null, false);
            return localMyLoadingLayoutBase;
        }
        paramListView.addFooterView(localFrameLayout, null, false);
        return localMyLoadingLayoutBase;
    }

    protected MyLoadingLayoutBase createLoadingLayout(Context paramContext,
            ScrollMode paramScrollMode)
    {
        IMyRefreshListLoadingLayoutCreateCallBack localIMyRefreshListLoadingLayoutCreateCallBack = mCreateLoadingLayoutCallBack;
        MyLoadingLayoutBase localMyLoadingLayoutBase = null;
        if (localIMyRefreshListLoadingLayoutCreateCallBack != null)
            localMyLoadingLayoutBase = mCreateLoadingLayoutCallBack.createLoadingLayout(
                paramContext, paramScrollMode);
        if (localMyLoadingLayoutBase != null)
            return localMyLoadingLayoutBase;
        return new RefreshListLoading(paramContext,
            ScrollDirection.SCROLL_DIRECTION_VERTICAL, paramScrollMode);
    }

    protected ListView createScrollContentView(Context paramContext)
    {
        ListView localListView = new ListView(paramContext);
        if (mScrollMode == MyScrollViewBase.ScrollMode.PULL_FROM_START || mScrollMode == MyScrollViewBase.ScrollMode.BOTH)
        {
            mHeaderLoadingView = buildListViewHeadOrFootView(paramContext, localListView,
                MyScrollViewBase.ScrollMode.PULL_FROM_START);
        }
//        else if (mScrollMode == MyScrollViewBase.ScrollMode.PULL_FROM_END)
//        {
//            mFooterLoadingView = buildListViewHeadOrFootView(paramContext, localListView,
//                MyScrollViewBase.ScrollMode.PULL_FROM_END);
//        }
//        else if (mScrollMode == MyScrollViewBase.ScrollMode.BOTH)
//        {
//            mHeaderLoadingView = buildListViewHeadOrFootView(paramContext, localListView,
//                MyScrollViewBase.ScrollMode.PULL_FROM_START);
//            mFooterLoadingView = buildListViewHeadOrFootView(paramContext, localListView,
//                MyScrollViewBase.ScrollMode.PULL_FROM_END);
//        }

        return localListView;
    }

    public ListAdapter getAdapter()
    {
        return ((ListView) mScrollContentView).getAdapter();
    }

    public int getFirstVisiblePosition()
    {
        if (mScrollContentView != null)
            return ((ListView) mScrollContentView).getFirstVisiblePosition();
        return 0;
    }

    public ListView getListView()
    {
        return (ListView) mScrollContentView;
    }

    public View getListViewChildAt(int paramInt)
    {
        if (mScrollContentView != null)
            return ((ListView) mScrollContentView).getChildAt(paramInt);
        return null;
    }

    public ListAdapter getRawAdapter()
    {
        return mRawAdapter;
    }

    protected boolean isContentFullScreen()
    {
        return false;
    }

    protected boolean isReadyForScrollEnd()
    {
        ListAdapter localListAdapter = ((ListView) mScrollContentView).getAdapter();
        if ((localListAdapter == null) || (localListAdapter.isEmpty() == true))
            return true;
        int lastPosition = ((ListView) mScrollContentView).getCount() - 1;
        int lastVisiblePosition = ((ListView) mScrollContentView)
                .getLastVisiblePosition();
        if (lastVisiblePosition >= lastPosition - 1)
        {
            int childCount = lastVisiblePosition
                - ((ListView) mScrollContentView).getFirstVisiblePosition();
            View localView = ((ListView) mScrollContentView).getChildAt(childCount);
            if (localView != null)
                return localView.getBottom() <= ((ListView) mScrollContentView)
                        .getBottom();
        }
        return false;
    }

    protected boolean isReadyForScrollStart()
    {
        ListAdapter localListAdapter = ((ListView) mScrollContentView).getAdapter();
        if ((localListAdapter == null) || (localListAdapter.isEmpty() == true))
            return true;
        if (((ListView) mScrollContentView).getFirstVisiblePosition() <= 1)
        {
            View localView = ((ListView) mScrollContentView).getChildAt(0);
            if (localView != null)
                return localView.getTop() >= ((ListView) mScrollContentView).getTop();
        }
        return false;
    }

    protected void onLoadFinish(boolean bSuccess)
    {
        int scrollto;
        if ((mCurScrollState == ScrollState.ScrollState_FromStart)
            && (mHeaderLayout != null) && (mHeaderLoadingView != null))
        {
            scrollto = -mHeaderLayout.getContentSize();
            if (Math.abs(((ListView) mScrollContentView).getFirstVisiblePosition()) <= 1)
            {
                if ((mHeaderLoadingView != null)
                    && (mHeaderLoadingView.getVisibility() == View.VISIBLE))
                {
                    mHeaderLoadingView.setVisibility(View.GONE);
                    if (bSuccess)
                    {
                        mHeaderLayout.refreshSuc();
                    }
                    else
                    {
                        mHeaderLayout.refreshFail();
                    }
                    
                    ((ListView) mScrollContentView).setSelection(0);
                    contentViewScrollTo(scrollto);
                    setState(RefreshState.RESET);
//                    mHeaderLayout.postDelayed(new Runnable()
//                    {
//                        
//                        @Override
//                        public void run()
//                        {
//                            // TODO Auto-generated method stub
//                            setState(RefreshState.RESET);
//                        }
//                    }, 900);
                }
            }
        }
        else if ((mCurScrollState == ScrollState.ScrollState_FromEnd)
            && (mFooterLayout != null) && (mFooterLoadingView != null))
        {
            if (mFooterLayout != null)
            {
                mFooterLayout.reset();
                mFooterLayout.hideAllSubViews();
            }
            if (mFooterLoadingView != null)
            {
                mFooterLoadingView.setVisibility(View.VISIBLE);
                int i;
                if (getRawAdapter() == null)
                {
                    i = getRawAdapter().getCount();
                }
                else
                {
                    i = 0;
                }
                mFooterLoadingView.loadFinish("1111");
                mLayoutVisibilityChangesEnabled = false;
                smoothScrollTo(0);
            }
        }

    }

    protected void onRefreshing()
    {
        ListAdapter localListAdapter = ((ListView) mScrollContentView).getAdapter();
        if ((localListAdapter == null) || (localListAdapter.isEmpty()))
        {
            super.onRefreshing();
            return;
        }
        int scrollto;
        MyLoadingLayoutBase loadingLayout;
        MyLoadingLayoutBase loadingView;
        MyLoadingLayoutBase loadingViewOther;
        int selectPosition;
        if ((mCurScrollState == ScrollState.ScrollState_FromStart)
            && (mHeaderLayout != null) && (mHeaderLoadingView != null))
        {
            loadingLayout = mHeaderLayout;
            loadingView = mHeaderLoadingView;
            loadingViewOther = mFooterLoadingView;
            selectPosition = 0;
            scrollto = getScrollY() + mHeaderLayout.getContentSize();
        }
        else if ((mCurScrollState == ScrollState.ScrollState_FromEnd)
            && (mFooterLayout != null) && (mFooterLoadingView != null))
        {
            loadingLayout = mFooterLayout;
            loadingView = mFooterLoadingView;
            loadingViewOther = mHeaderLoadingView;
            selectPosition = -1 + ((ListView) mScrollContentView).getCount();
            scrollto = getScrollY() - mFooterLayout.getContentSize();
        }
        else
        {
            scrollto = 0;
            selectPosition = 0;
            loadingViewOther = null;
            loadingView = null;
            loadingLayout = null;
        }
        if (loadingLayout != null)
        {
            loadingLayout.reset();
            loadingLayout.hideAllSubViews();
        }
        if (loadingView != null)
        {
            loadingView.setVisibility(View.VISIBLE);
            loadingView.refreshing();
        }
        if (loadingViewOther != null)
            loadingViewOther.setVisibility(View.GONE);

        mLayoutVisibilityChangesEnabled = false;
//        contentViewScrollTo(scrollto);
        ((ListView) mScrollContentView).setSelection(selectPosition);
        smoothScrollTo(0, new SmoothScrollRunnableListener());

    }

    protected void onReset()
    {
        int scrollto = 0;
        int selectPosition = 0;
        Object loadingView = null;
        Object loadingLayout = null;
        Object loadingViewOther = null;
        if ((mCurScrollState == ScrollState.ScrollState_FromStart)
            && (mHeaderLayout != null) && (mHeaderLoadingView != null))
        {
            if (Math.abs(((ListView) mScrollContentView).getFirstVisiblePosition()) <= 1)
            {
                loadingView = mHeaderLoadingView;
                loadingLayout = mHeaderLayout;
                loadingViewOther = mFooterLoadingView;
                scrollto = -mHeaderLayout.getContentSize();
                selectPosition = 0;
            }
        }
        else if ((mCurScrollState == MyScrollViewBase.ScrollState.ScrollState_FromEnd)
            && (mFooterLayout != null) && (mFooterLoadingView != null))
        {
            int lastPosition = ((ListView) mScrollContentView).getCount() - 1;
            if (Math.abs(((ListView) mScrollContentView).getLastVisiblePosition()
                - lastPosition) <= 1)
            {
                loadingView = mFooterLoadingView;
                loadingLayout = mFooterLayout;
                loadingViewOther = mHeaderLoadingView;
                scrollto = mFooterLayout.getContentSize();
                selectPosition = lastPosition;

            }
        }
        if ((loadingView != null)
            && (((MyLoadingLayoutBase) loadingView).getVisibility() == View.VISIBLE))
        {
            ((MyLoadingLayoutBase) loadingView).setVisibility(View.GONE);
            ((MyLoadingLayoutBase) loadingLayout).showAllSubViews();

            ((ListView) mScrollContentView).setSelection(selectPosition);
            contentViewScrollTo(scrollto);

        }
        if (loadingViewOther != null)
        {
            ((MyLoadingLayoutBase) loadingViewOther).setVisibility(View.VISIBLE);
        }
        super.onReset();
    }

    public int pointToPosition(int x, int y)
    {
        if (mScrollContentView != null)
        {
            return ((ListView) mScrollContentView).pointToPosition(x, y);
        }
        else
        {
            return 0;
        }
    }

    public void reset()
    {
        if (mRefreshState == RefreshState.REFRESH_LOAD_FINISH)
            setState(MyRefreshScrollViewBase.RefreshState.RESET);
    }

    public void setAdapter(ListAdapter paramListAdapter)
    {
        mRawAdapter = paramListAdapter;
        ((ListView) mScrollContentView).setAdapter(paramListAdapter);
    }

    public void setCacheColorHint(int paramInt)
    {
        ((ListView) mScrollContentView).setCacheColorHint(0);
    }

    public void setCreateLoadingLayoutCallBack(
            IMyRefreshListLoadingLayoutCreateCallBack paramIMyRefreshListLoadingLayoutCreateCallBack)
    {
        mCreateLoadingLayoutCallBack = paramIMyRefreshListLoadingLayoutCreateCallBack;
    }

    public void setDivider(Drawable paramDrawable)
    {
        ((ListView) mScrollContentView).setDivider(paramDrawable);
    }
    
    public void setDividerHeight(int height)
    {
        ((ListView) mScrollContentView).setDividerHeight(height);
    }

    public void setOnItemClickListener(
            AdapterView.OnItemClickListener paramOnItemClickListener)
    {
        ((ListView) mScrollContentView).setOnItemClickListener(paramOnItemClickListener);
    }

    public void setOnItemSelectedListener(
            AdapterView.OnItemSelectedListener paramOnItemSelectedListener)
    {
        ((ListView) mScrollContentView)
                .setOnItemSelectedListener(paramOnItemSelectedListener);
    }

    public void setOnScrollListener(AbsListView.OnScrollListener paramOnScrollListener)
    {
        if (mScrollContentView != null)
            ((ListView) mScrollContentView).setOnScrollListener(paramOnScrollListener);
    }

    public void setOverscrollFooter(Drawable paramDrawable)
    {
        if (mScrollContentView != null)
        {
            try
            {
                ((ListView) mScrollContentView).getClass()
                        .getMethod("setOverscrollFooter", new Class[]{Drawable.class})
                        .invoke(mScrollContentView, new Object[]{paramDrawable});
                return;
            }
            catch (Exception localException)
            {
                localException.printStackTrace();
            }
        }
    }

    public void setSelection(int paramInt)
    {
        ((ListView) mScrollContentView).setSelection(paramInt);
    }

    public void setSelector(Drawable paramDrawable)
    {
        if ((mScrollContentView != null) && (paramDrawable != null))
            ((ListView) mScrollContentView).setSelector(paramDrawable);
    }

    public void setVerticalScrollBarEnabled(boolean paramBoolean)
    {
        ((ListView) mScrollContentView).setVerticalScrollBarEnabled(paramBoolean);
    }
}
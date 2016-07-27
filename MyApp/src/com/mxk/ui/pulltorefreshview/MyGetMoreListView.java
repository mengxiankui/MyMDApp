package com.mxk.ui.pulltorefreshview;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;


public class MyGetMoreListView extends MyRefreshListView
    implements
        AbsListView.OnScrollListener
{
//    private final String TAG = "MyGetMoreListView";
    private boolean end = false;
    protected RefreshState mGetMoreRefreshState = RefreshState.RESET;
    private IScrollListener mIScrollListener;
    protected int mScrollState = 0;

    public MyGetMoreListView(Context paramContext)
    {
        super(paramContext, ScrollMode.NONE);
    }

    public MyGetMoreListView(Context paramContext, AttributeSet paramAttributeSet)
    {
        super(paramContext, paramAttributeSet);
    }

    public void addClickLoadMore()
    {
        mFooterLoadingView.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                if ((mRefreshListViewListener != null)
                    && (mGetMoreRefreshState == RefreshState.RESET))
                    mRefreshListViewListener
                            .onMyRefreshListViewRefresh(ScrollState.ScrollState_FromEnd);
            }
        });
    }

    public void addFooterView(MyLoadingLayoutBase paramTXLoadingLayoutBase)
    {
        if (paramTXLoadingLayoutBase == null)
            return;
        if (mFooterLoadingView != null)
            ((ListView) mScrollContentView).removeFooterView(mFooterLoadingView);
        mFooterLoadingView = paramTXLoadingLayoutBase;
        ((ListView) mScrollContentView).addFooterView(mFooterLoadingView);
        mFooterLoadingView.setVisibility(View.VISIBLE);
        updateFootViewState();
    }

    public void addHeaderView(View paramView)
    {
        ((ListView) mScrollContentView).addHeaderView(paramView);
    }

    protected ListView createScrollContentView(Context paramContext)
    {
        ListView localListView = super.createScrollContentView(paramContext);
        if (mScrollMode == ScrollMode.PULL_FROM_END || mScrollMode == ScrollMode.BOTH)
        {
            mFooterLoadingView = createLoadingLayout(paramContext,
                ScrollMode.PULL_FROM_END);
            mFooterLoadingView.setVisibility(View.VISIBLE);
            localListView.addFooterView(mFooterLoadingView);
        }
        localListView.setOnScrollListener(this);
        return localListView;
    }

    public int getHeaderViewsCount()
    {
        return ((ListView) mScrollContentView).getHeaderViewsCount();
    }

    public boolean isScrollStateIdle()
    {
        return mScrollState == 0;
    }

    public void onNextPageComplete(boolean bIsSuccess)
    {
        if (mGetMoreRefreshState == RefreshState.REFRESHING)
        {
//            if (bIsSuccess == true)
//            {
                mGetMoreRefreshState = RefreshState.RESET;
//            }
//            else
//            {
//                mGetMoreRefreshState = RefreshState.REFRESH_LOAD_FINISH;
//            }
                updateFootViewState(bIsSuccess);
        }
//        else
//        {
//            if (mGetMoreRefreshState == RefreshState.REFRESH_LOAD_FINISH)
//                mGetMoreRefreshState = RefreshState.RESET;
//            else if ((mGetMoreRefreshState == RefreshState.RESET) && (!bNeedReset))
//                mGetMoreRefreshState = RefreshState.REFRESH_LOAD_FINISH;
//        }
        
    }

    public void onScroll(AbsListView paramAbsListView, int paramInt1, int paramInt2,
            int paramInt3)
    {
        if (mScrollContentView == null)
            return;
        if (mIScrollListener != null)
        {
            mIScrollListener.onScroll(paramAbsListView, paramInt1, paramInt2, paramInt3);
        }
        else
        {
            end = isReadyForScrollEnd();
        }

    }

    public void onScrollStateChanged(AbsListView paramAbsListView, int paramInt)
    {
        mScrollState = paramInt;
        if (mIScrollListener != null)
            mIScrollListener.onScrollStateChanged(paramAbsListView, paramInt);
        if ((paramInt == 0) && (end) && (mGetMoreRefreshState == RefreshState.RESET))
        {
            if (mRefreshListViewListener != null)
                mRefreshListViewListener
                        .onMyRefreshListViewRefresh(ScrollState.ScrollState_FromEnd);
            mGetMoreRefreshState = RefreshState.REFRESHING;
            updateFootViewState();
        }
    }

    public void removeHeaderView(View paramView)
    {
        ((ListView) mScrollContentView).removeHeaderView(paramView);
    }

    public void reset()
    {
        mGetMoreRefreshState = RefreshState.RESET;
        mScrollState = 0;
    }

    public void setIScrollerListener(IScrollListener paramIScrollListener)
    {
        mIScrollListener = paramIScrollListener;
    }

    protected void updateFootViewState()
    {
        updateFootViewState(true);
    }

    protected void updateFootViewState(boolean bIsSuccess)
    {
        if (mFooterLoadingView == null)
        {
            return;
        }
        switch (mGetMoreRefreshState.ordinal())
        {

            case 0:
                if (bIsSuccess)
                {
                    mFooterLoadingView.loadSuc();
                }
                else
                {
                    mFooterLoadingView.loadFail();
                }
                break;
            case 4 :
//                ListAdapter localListAdapter = getRawAdapter();
//                int i = 0;
//                if (localListAdapter != null)
//                    i = getRawAdapter().getCount();
                mFooterLoadingView.loadFinish("1111");
                break;
            case 3 :
                mFooterLoadingView.refreshing();
                break;
            default :
                break;
        }

    }

    protected void updateUIFroMode()
    {
        mFooterLayout = null;
        super.updateUIFroMode();
        refreshLoadingLayoutSize();
    }
}
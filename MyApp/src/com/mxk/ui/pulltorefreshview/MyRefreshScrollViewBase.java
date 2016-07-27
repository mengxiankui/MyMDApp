package com.mxk.ui.pulltorefreshview;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


public abstract class MyRefreshScrollViewBase<T extends View> extends MyScrollViewBase<T>
{
    protected MyLoadingLayoutBase mFooterLayout;
    protected MyLoadingLayoutBase mHeaderLayout;
    protected boolean mLayoutVisibilityChangesEnabled = true;
    protected IMyRefreshListViewListener mRefreshListViewListener = null;
    protected RefreshState mRefreshState = RefreshState.RESET;

    public enum RefreshState
    {
        RESET, PULL_TO_REFRESH, RELEASE_TO_REFRESH, REFRESHING, REFRESH_LOAD_FINISH;
    }

    public MyRefreshScrollViewBase(Context paramContext, AttributeSet paramAttributeSet)
    {
        super(paramContext, paramAttributeSet);
    }

    public MyRefreshScrollViewBase(Context paramContext,
            ScrollDirection paramScrollDirection, ScrollMode paramScrollMode)
    {
        super(paramContext, paramScrollDirection, paramScrollMode);
    }

    protected final void addViewInternal(View child, int index,
            ViewGroup.LayoutParams paramLayoutParams)
    {
        super.addViewInternal(child, index, paramLayoutParams);
    }

    protected void contentViewScrollTo(int paramInt)
    {
        super.contentViewScrollTo(paramInt);
        if (mLayoutVisibilityChangesEnabled)
        {
            if (mHeaderLayout != null)
            {
                if (paramInt < 0)
                {
                    mHeaderLayout.setVisibility(View.VISIBLE);
                }
                else
                {
                    mHeaderLayout.setVisibility(View.INVISIBLE);
                }

            }
            if (mFooterLayout != null)
            {
                if (paramInt > 0)
                {
                    mFooterLayout.setVisibility(View.VISIBLE);
                }
                else
                {
                    mFooterLayout.setVisibility(View.INVISIBLE);
                }
            }
        }

    }

    protected abstract MyLoadingLayoutBase createLoadingLayout(Context paramContext,
            ScrollMode paramScrollMode);

    protected LinearLayout.LayoutParams getLoadingLayoutLayoutParams()
    {
        if (mScrollDirection == ScrollDirection.SCROLL_DIRECTION_HORIZONTAL)
        {
            return new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.MATCH_PARENT);
        }
        else
        {
            return new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        }

    }

    protected void initView(Context paramContext)
    {
        if (mScrollMode == ScrollMode.PULL_FROM_START)
            mHeaderLayout = createLoadingLayout(paramContext,
                MyScrollViewBase.ScrollMode.PULL_FROM_START);
        else if (mScrollMode == ScrollMode.PULL_FROM_END)
        {
            mFooterLayout = createLoadingLayout(paramContext, ScrollMode.PULL_FROM_END);
        }
        else if (mScrollMode == ScrollMode.BOTH)
        {
            mHeaderLayout = createLoadingLayout(paramContext, ScrollMode.PULL_FROM_START);
            mFooterLayout = createLoadingLayout(paramContext, ScrollMode.PULL_FROM_END);
        }
        super.initView(paramContext);
        updateUIFroMode();
    }

    protected void onLoadFinish(boolean bSuccess)
    {

        onReset();

    }

    protected void onPullToRefresh()
    {
        if ((mCurScrollState == ScrollState.ScrollState_FromStart)
            && (mHeaderLayout != null))
        {
            mHeaderLayout.setVisibility(View.VISIBLE);
            mHeaderLayout.pullToRefresh();
        }
        if ((mCurScrollState == ScrollState.ScrollState_FromEnd)
            && (mFooterLayout != null))
        {
            mFooterLayout.setVisibility(View.VISIBLE);
            mFooterLayout.pullToRefresh();
        }
    }

    public void onRefreshComplete(boolean bIsSuccess)
    {
        if (mRefreshState == RefreshState.REFRESHING)
        {
//            if (!bIsSuccess)
//            {
                setState(RefreshState.REFRESH_LOAD_FINISH);
                onLoadFinish(bIsSuccess);
//            }
//            else
//            {
//                setState(RefreshState.RESET);
//            }
        }
//        else if (mRefreshState == RefreshState.REFRESH_LOAD_FINISH)
//        {
//            setState(RefreshState.RESET);
//        }
    }

    protected void onRefreshing()
    {
        if (((mScrollMode == MyScrollViewBase.ScrollMode.PULL_FROM_START) || (mScrollMode == MyScrollViewBase.ScrollMode.BOTH))
            && (mHeaderLayout != null))
        {
            mHeaderLayout.refreshing();
        }
        else if (((mScrollMode == MyScrollViewBase.ScrollMode.PULL_FROM_END) || (mScrollMode == MyScrollViewBase.ScrollMode.BOTH))
            && (mFooterLayout != null))
        {
            mFooterLayout.refreshing();
        }
        SmoothScrollRunnableListener localListener = new SmoothScrollRunnableListener();
        if ((mCurScrollState == ScrollState.ScrollState_FromStart)
            && (mHeaderLayout != null))
        {
            smoothScrollTo(-mHeaderLayout.getContentSize(), localListener);
        }
        else if ((mCurScrollState == ScrollState.ScrollState_FromEnd)
            && (mFooterLayout != null))
        {
            smoothScrollTo(mFooterLayout.getContentSize(), localListener);
        }

    }

    protected void onReleaseToRefresh()
    {
        if ((mCurScrollState == ScrollState.ScrollState_FromStart)
            && (mHeaderLayout != null))
        {
            mHeaderLayout.releaseToRefresh();
        }
        else if ((mCurScrollState == ScrollState.ScrollState_FromEnd)
            && (mFooterLayout != null))
        {
            mFooterLayout.releaseToRefresh();
        }
    }

    protected void onReset()
    {
        mIsBeingDragged = false;
        mLayoutVisibilityChangesEnabled = true;
        SmoothScrollRunnableListener localListener = new SmoothScrollRunnableListener();
        smoothScrollTo(0, localListener);
    }

    @Override
    protected void onScrollViewSizeChange(int w, int h)
    {
        refreshLoadingLayoutSize();
        super.onScrollViewSizeChange(w, h);
    }

    @Override
    protected boolean onTouchEventCancelAndUp()
    {
        if (mIsBeingDragged == true)
        {
            mIsBeingDragged = false;
            if (mRefreshState == RefreshState.RELEASE_TO_REFRESH)
            {
                setState(RefreshState.REFRESHING);
                setVerticalFadingEdgeEnabled(false);
                return true;
            }
            if (mRefreshState == RefreshState.REFRESHING)
            {
                smoothScrollTo(0);
                return true;
            }
            if (mRefreshState == RefreshState.REFRESH_LOAD_FINISH)
            {
                smoothScrollTo(0);
                return true;
            }
            setState(RefreshState.RESET);
            return true;
        }
        return false;
    }

    protected final void refreshLoadingLayoutSize()
    {
        int maxOffset = (int) (1.2F * getMaximumScrollOffset());
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();
        int left;
        int right;
        int bottom;
        int top;
        if (mScrollDirection == ScrollDirection.SCROLL_DIRECTION_HORIZONTAL)
        {
            if (((mScrollMode == ScrollMode.PULL_FROM_START) || (mScrollMode == ScrollMode.BOTH))
                && (mHeaderLayout != null))
            {
                mHeaderLayout.setWidth(maxOffset);
                left = -maxOffset;
            }
            else
            {
                left = 0;
            }
            if (((mScrollMode == ScrollMode.PULL_FROM_END) || (mScrollMode == ScrollMode.BOTH))
                && (mFooterLayout != null))
            {
                mFooterLayout.setWidth(maxOffset);
                right = -maxOffset;

            }
            else
            {
                right = 0;
            }
            top = paddingTop;
            bottom = paddingBottom;

        }
        else
        {

            if (((mScrollMode == ScrollMode.PULL_FROM_START) || (mScrollMode == ScrollMode.BOTH))
                && (mHeaderLayout != null))
            {
                mHeaderLayout.setHeight(maxOffset);
                top = -maxOffset;
            }
            else
            {
                top = 0;
            }

            if (((mScrollMode == ScrollMode.PULL_FROM_END) || (mScrollMode == ScrollMode.BOTH))
                && (mFooterLayout != null))
            {
                mFooterLayout.setWidth(maxOffset);
                bottom = -maxOffset;

            }
            else
            {
                bottom = 0;
            }
            left = paddingLeft;
            right = paddingRight;

        }

        setPadding(left, top, right, bottom);
    }

    protected int scrollMoveEvent()
    {
        int scrollLength = super.scrollMoveEvent();
        int triggerSize = 0;
        if ((scrollLength == 0) || (mRefreshState == RefreshState.REFRESHING))
        {
            return scrollLength;
        }
        else if ((mCurScrollState == ScrollState.ScrollState_FromStart)
            && (mHeaderLayout != null))
        {
            triggerSize = mHeaderLayout.getTriggerSize();
            mHeaderLayout.onPull(scrollLength);
        }
        else if ((mCurScrollState == ScrollState.ScrollState_FromEnd)
            && (mFooterLayout != null))
        {
            triggerSize = mFooterLayout.getContentSize();
            mFooterLayout.onPull(scrollLength);

        }
        if (mRefreshState == RefreshState.PULL_TO_REFRESH
            && triggerSize < Math.abs(scrollLength))
        {
            setState(MyRefreshScrollViewBase.RefreshState.RELEASE_TO_REFRESH);
        }
        else if (mRefreshState != RefreshState.PULL_TO_REFRESH
            && triggerSize > Math.abs(scrollLength))
        {
            setState(RefreshState.PULL_TO_REFRESH);
        }
        return scrollLength;
    }

    public void setRefreshListViewListener(
            IMyRefreshListViewListener paramIMyRefreshListViewListener)
    {
        mRefreshListViewListener = paramIMyRefreshListViewListener;
    }

    protected void setState(RefreshState paramRefreshState)
    {
        mRefreshState = paramRefreshState;
        switch (paramRefreshState.ordinal())
        {
            case 0 :
                onReset();
                break;
            case 1 :
                onPullToRefresh();
                break;
            case 2 :
                onReleaseToRefresh();
                break;
            case 3 :
                onRefreshing();
                break;
            case 4 :
//                onLoadFinish();
                break;
            default :
                break;
        }

    }

    protected void updateUIFroMode()
    {
        LinearLayout.LayoutParams localLayoutParams = getLoadingLayoutLayoutParams();
        if (mHeaderLayout != null)
        {
            if (mHeaderLayout.getParent() == this)
                removeView(mHeaderLayout);
            addViewInternal(mHeaderLayout, 0, localLayoutParams);
        }
        if (mFooterLayout != null)
        {
            if (mFooterLayout.getParent() == this)
                removeView(mFooterLayout);
            addViewInternal(mFooterLayout, -1, localLayoutParams);
        }
        refreshLoadingLayoutSize();
    }

    class SmoothScrollRunnableListener implements ISmoothScrollRunnableListener
    {

        public void onSmoothScrollFinished()
        {
            if (mRefreshState == RefreshState.REFRESHING)
            {
                if (mRefreshListViewListener != null)
                    mRefreshListViewListener.onMyRefreshListViewRefresh(mCurScrollState);
            }
        }
    }
}
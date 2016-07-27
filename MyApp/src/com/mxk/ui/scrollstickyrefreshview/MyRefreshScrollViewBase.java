package com.mxk.ui.scrollstickyrefreshview;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


public abstract class MyRefreshScrollViewBase<T extends View> extends MyScrollViewBase<T>
{
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
            ScrollDirection paramScrollDirection)
    {
        super(paramContext, paramScrollDirection);
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
        }

    }

    protected abstract MyLoadingLayoutBase createLoadingLayout(Context paramContext);

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
        mHeaderLayout = createLoadingLayout(paramContext);
        super.initView(paramContext);
        updateUIFroMode();
    }

    protected void onLoadFinish(boolean bSuccess)
    {

        onReset();

    }

    protected void onPullToRefresh()
    {
        if (mHeaderLayout != null)
        {
            mHeaderLayout.setVisibility(View.VISIBLE);
            mHeaderLayout.pullToRefresh();
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
        if (mHeaderLayout != null)
        {
            mHeaderLayout.refreshing();
        }
        SmoothScrollRunnableListener localListener = new SmoothScrollRunnableListener();
        if (mHeaderLayout != null)
        {
            smoothScrollTo(-mHeaderLayout.getContentSize(), localListener);
        }

    }

    protected void onReleaseToRefresh()
    {
        if (mHeaderLayout != null)
        {
            mHeaderLayout.releaseToRefresh();
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
            if (mDragState == DragState.DRAGSTART)
            {
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
            else if (mDragState == DragState.DRAGHEADER) 
            {
                return true;
            }
            
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
            if (mHeaderLayout != null)
            {
                mHeaderLayout.setWidth(maxOffset);
                left = -maxOffset;
            }
            else
            {
                left = 0;
            }
            
            right = 0;
            top = paddingTop;
            bottom = paddingBottom;

        }
        else
        {

            if (mHeaderLayout != null)
            {
                mHeaderLayout.setHeight(maxOffset);
                top = -maxOffset;
            }
            else
            {
                top = 0;
            }
            
            bottom = 0;
            left = paddingLeft;
            right = paddingRight;

        }

        setPadding(left, top, right, bottom);
    }

    protected int scrollMoveEvent()
    {
        int scrollLength = super.scrollMoveEvent();
        if (mDragState == DragState.DRAGHEADER)
        {
            return scrollLength;
        }
        int triggerSize = 0;
        if ((scrollLength == 0) || (mRefreshState == RefreshState.REFRESHING))
        {
            return scrollLength;
        }
        else if (mHeaderLayout != null)
        {
            triggerSize = mHeaderLayout.getTriggerSize();
            mHeaderLayout.onPull(scrollLength);
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
        refreshLoadingLayoutSize();
    }

    class SmoothScrollRunnableListener implements ISmoothScrollRunnableListener
    {

        public void onSmoothScrollFinished()
        {
            if (mRefreshState == RefreshState.REFRESHING)
            {
                if (mRefreshListViewListener != null)
                    mRefreshListViewListener.onMyRefreshListViewRefresh();
            }
        }
    }
}
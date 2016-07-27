package com.mxk.ui.pulltorefreshview;


import com.mxk.myapp.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PointF;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;



//import com.tencent.android.qqdownloader.b;

public abstract class MyScrollViewBase<T extends View> extends LinearLayout
{
    public static final float FRICTION = 2.0F;
    public static final int SMOOTH_SCROLL_DURATION_MS = 200;
    protected FrameLayout mContentViewWrapper;
    protected ScrollState mCurScrollState = ScrollState.ScrollState_Initial;
    private MyScrollViewBase<T>.SmoothScrollRunnable mCurrentSmoothScrollRunnable = null;
    protected PointF mInitialMotionPointF = new PointF(0.0F, 0.0F);
    protected boolean mIsBeingDragged = false;
    protected PointF mLastMotionPointF = new PointF(0.0F, 0.0F);
    private Interpolator mScrollAnimationInterpolator = new DecelerateInterpolator();
    protected T mScrollContentView;
    protected ScrollDirection mScrollDirection = ScrollDirection.SCROLL_DIRECTION_VERTICAL;
    protected ScrollMode mScrollMode = ScrollMode.BOTH;
    private View mTipsView = null;
    private int mTouchSlop = 0;

    public enum ScrollState
    {
        ScrollState_Initial, ScrollState_FromStart, ScrollState_FromEnd;
    }

    public enum ScrollDirection
    {
        SCROLL_DIRECTION_VERTICAL, SCROLL_DIRECTION_HORIZONTAL;

        static ScrollDirection getDirection(int direction)
        {
            switch (direction)
            {
                case 0 :
                    return SCROLL_DIRECTION_VERTICAL;
                case 1 :
                    return SCROLL_DIRECTION_HORIZONTAL;
                default :
                    return SCROLL_DIRECTION_VERTICAL;
            }
        }
    }

    public enum ScrollMode
    {
        PULL_FROM_START, PULL_FROM_END, BOTH, NONE, NOSCROLL;

        static ScrollMode mapIntToValue(int scrollMode)
        {
            switch (scrollMode)
            {

                case 0 :
                    return PULL_FROM_START;
                case 1 :
                    return PULL_FROM_END;
                case 2 :
                    return BOTH;
                case 3 :
                    return NONE;
                case 4 :
                    return NOSCROLL;
                default :
                    return BOTH;
            }

        }
    }

    public MyScrollViewBase(Context paramContext, AttributeSet paramAttributeSet)
    {
        super(paramContext, paramAttributeSet);
        TypedArray localTypedArray = paramContext.obtainStyledAttributes(
            paramAttributeSet, R.styleable.MyScrollViewBase);
        if (localTypedArray != null)
        {
            this.mScrollDirection = ScrollDirection.getDirection(localTypedArray.getInt(
                R.styleable.MyScrollViewBase_scrolldirection, 0));
            this.mScrollMode = ScrollMode.mapIntToValue(localTypedArray.getInt(
                R.styleable.MyScrollViewBase_scrollmode, 2));
            localTypedArray.recycle();
        }
        initView(paramContext);
    }

    public MyScrollViewBase(Context paramContext, ScrollDirection paramScrollDirection,
            ScrollMode paramScrollMode)
    {
        super(paramContext);
        this.mScrollDirection = paramScrollDirection;
        this.mScrollMode = paramScrollMode;
        initView(paramContext);
    }

    private void addScrollContentView(Context paramContext, T paramT)
    {
        if (paramT == null)
            return;
        super.addView(paramT, -1, new LinearLayout.LayoutParams(
            LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    public void addView(View paramView, int paramInt,
            ViewGroup.LayoutParams paramLayoutParams)
    {
        if ((paramView != null) && (this.mScrollContentView != null)
            && ((this.mScrollContentView instanceof ViewGroup)))
            ((ViewGroup) this.mScrollContentView).addView(paramView, paramInt,
                paramLayoutParams);
    }

    protected void addViewInternal(View child, int index,
            ViewGroup.LayoutParams paramLayoutParams)
    {
        super.addView(child, index, paramLayoutParams);
    }

    protected void contentViewScrollTo(int paramInt)
    {
        int i = getMaximumScrollOffset();
        int j = Math.min(i, Math.max(-i, paramInt));
        if (this.mScrollDirection == ScrollDirection.SCROLL_DIRECTION_HORIZONTAL)
        {
            scrollTo(j, 0);
        }
        else
        {
            scrollTo(0, j);
        }

    }

    protected abstract T createScrollContentView(Context paramContext);

    protected int getMaximumScrollOffset()
    {
        if (this.mScrollDirection == ScrollDirection.SCROLL_DIRECTION_HORIZONTAL)
        {
            return Math.round(getWidth() / 2.0F);
        }
        else
        {
            return Math.round(getHeight() / 2.0F);
        }

    }

    protected int getSmoothScrollDuration()
    {
        return 200;
    }

    protected void initView(Context paramContext)
    {
        if (this.mScrollDirection == ScrollDirection.SCROLL_DIRECTION_HORIZONTAL)
        {
            setOrientation(LinearLayout.HORIZONTAL);
        }
        else
        {
            setOrientation(LinearLayout.VERTICAL);
        }

        setGravity(Gravity.CENTER);
        mTouchSlop = ViewConfiguration.get(paramContext).getScaledTouchSlop();
        mScrollContentView = createScrollContentView(paramContext);
        addScrollContentView(paramContext, this.mScrollContentView);
    }

    protected abstract boolean isContentFullScreen();

    protected boolean isReadyForScroll()
    {
        if (isReadyForScrollStart() || isReadyForScrollEnd())
        {
            return true;
        }
        return false;
    }

    protected abstract boolean isReadyForScrollEnd();

    protected abstract boolean isReadyForScrollStart();

    @Override
    public final boolean onInterceptTouchEvent(MotionEvent paramMotionEvent)
    {
        int action = paramMotionEvent.getAction();
        if ((action == MotionEvent.ACTION_CANCEL) || (action == MotionEvent.ACTION_UP))
        {
            this.mIsBeingDragged = false;
            return false;
        }
        if ((this.mIsBeingDragged == true) && (action != MotionEvent.ACTION_DOWN))
            return true;

        switch (action)
        {
            case MotionEvent.ACTION_MOVE :
                if (isReadyForScroll() == true)
                {
                    float currentX = paramMotionEvent.getX();
                    float currentY = paramMotionEvent.getY();
                    float deltaDirection;
                    float deltaOther;
                    if (mScrollDirection == ScrollDirection.SCROLL_DIRECTION_HORIZONTAL)
                    {
                        deltaDirection = currentX - this.mLastMotionPointF.x;
                        deltaOther = currentY - this.mLastMotionPointF.y;
                    }
                    else
                    {
                        deltaDirection = currentY - this.mLastMotionPointF.y;
                        deltaOther = currentX - this.mLastMotionPointF.x;
                    }

                    float absDiffDirection = Math.abs(deltaDirection);
                    if ((absDiffDirection <= this.mTouchSlop)
                        || (absDiffDirection <= Math.abs(deltaOther)))
                    {
                        break;
                    }
                    else if ((deltaDirection >= 1.0F) && (isReadyForScrollStart()))
                    {
                        this.mLastMotionPointF.x = currentX;
                        this.mLastMotionPointF.y = currentY;
                        this.mIsBeingDragged = true;
                        this.mCurScrollState = ScrollState.ScrollState_FromStart;
                    }
                    else if ((deltaDirection <= -1.0F) && isReadyForScrollEnd())
                    {
                        this.mLastMotionPointF.x = currentX;
                        this.mLastMotionPointF.y = currentY;
                        this.mIsBeingDragged = true;
                        this.mCurScrollState = ScrollState.ScrollState_FromEnd;
                    }
                }
                break;
            case MotionEvent.ACTION_DOWN :
                if (isReadyForScroll() == true)
                {
                    mLastMotionPointF.x = paramMotionEvent.getX();
                    mInitialMotionPointF.x = paramMotionEvent.getX();
                    mLastMotionPointF.y = paramMotionEvent.getY();
                    mInitialMotionPointF.y = paramMotionEvent.getY();
                    mIsBeingDragged = false;
                }
                break;
            default :
                break;
        }
        return this.mIsBeingDragged;

    }

    protected void onScrollViewSizeChange(int w, int h)
    {
        refreshScrollContentViewSize(w, h);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
        onScrollViewSizeChange(w, h);
        post(new Runnable()
        {

            @Override
            public void run()
            {
                // TODO Auto-generated method stub
                requestLayout();
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent paramMotionEvent)
    {
        if ((paramMotionEvent.getAction() == MotionEvent.ACTION_DOWN))
        {
            return false;
        }
        else if (paramMotionEvent.getAction() == MotionEvent.ACTION_CANCEL
            || paramMotionEvent.getAction() == MotionEvent.ACTION_UP)
        {
            return onTouchEventCancelAndUp();
        }
        else
        {
            if (mIsBeingDragged)
            {
                this.mLastMotionPointF.x = paramMotionEvent.getX();
                this.mLastMotionPointF.y = paramMotionEvent.getY();
                if (this.mScrollMode != ScrollMode.NOSCROLL)
                    scrollMoveEvent();
                return true;

            }
            else
            {
                if (isReadyForScroll())
                {
                    mLastMotionPointF.x = paramMotionEvent.getX();
                    mInitialMotionPointF.x = paramMotionEvent.getX();
                    mLastMotionPointF.y = paramMotionEvent.getY();
                    mInitialMotionPointF.y = paramMotionEvent.getY();
                    return true;
                }
            }
        }
        return false;

    }

    protected boolean onTouchEventCancelAndUp()
    {
        if (this.mIsBeingDragged == true)
        {
            this.mIsBeingDragged = false;
            smoothScrollTo(0);
            return true;
        }
        return false;
    }

    protected final void refreshScrollContentViewSize(int w, int h)
    {
        if (this.mContentViewWrapper == null)
            return;

        LinearLayout.LayoutParams localLayoutParams = (LinearLayout.LayoutParams) this.mContentViewWrapper
                .getLayoutParams();
        localLayoutParams.width = w;
        localLayoutParams.height = h;
        mContentViewWrapper.requestLayout();

    }

    protected int scrollMoveEvent()
    {
        float from;
        float to;
        if (this.mScrollDirection == ScrollDirection.SCROLL_DIRECTION_HORIZONTAL)
        {
            from = mInitialMotionPointF.x;
            to = mLastMotionPointF.x;
        }
        else
        {
            from = mInitialMotionPointF.y;
            to = mLastMotionPointF.y;
        }
        int scrollLength;
        if (mCurScrollState == ScrollState.ScrollState_FromStart)
        {
            scrollLength = Math.round(Math.min(from - to, 0.0F) / 2.0F);
        }
        else
        {
            scrollLength = Math.round(Math.max(from - to, 0.0F) / 2.0F);
        }
        contentViewScrollTo(scrollLength);
        return scrollLength;
    }

    public void setTipsView(View paramView)
    {
        if (paramView == null)
            return;
        removeAllViews();
        super.addView(paramView, -1, new LinearLayout.LayoutParams(
            LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    /**
     * 平滑滚动
     * 
     * @param newScrollValue 滚动的值
     */
    public final void smoothScrollTo(int newScrollValue)
    {
        smoothScrollTo(newScrollValue, getSmoothScrollDuration(), 0L, null);
    }

    /**
     * 平滑滚动
     * 
     * @param newScrollValue 滚动的值
     * @param duration 滚动时候
     * @param delayMillis 延迟时间，0代表不延迟
     * @param ISmoothScrollRunnableListener 滑动监听
     */
    public final void smoothScrollTo(int newScrollValue, long duration, long delayMillis,
            ISmoothScrollRunnableListener paramISmoothScrollRunnableListener)
    {
        if (mCurrentSmoothScrollRunnable != null)
            mCurrentSmoothScrollRunnable.stop();
        int scrollValue;
        if (mScrollDirection == ScrollDirection.SCROLL_DIRECTION_HORIZONTAL)
        {
            scrollValue = getScrollX();
        }
        else
        {
            scrollValue = getScrollY();
        }

        if (scrollValue != newScrollValue)
        {
            mCurrentSmoothScrollRunnable = new SmoothScrollRunnable(scrollValue,
                newScrollValue, duration, paramISmoothScrollRunnableListener);
            if (delayMillis <= 0L)
            {
                post(mCurrentSmoothScrollRunnable);
            }
            else
            {
                postDelayed(mCurrentSmoothScrollRunnable, delayMillis);
            }
        }
    }

    /**
     * 平滑滚动
     * 
     * @param newScrollValue 滚动的值
     * @param ISmoothScrollRunnableListener 滑动监听
     */
    public final void smoothScrollTo(int newScrollValue,
            ISmoothScrollRunnableListener paramISmoothScrollRunnableListener)
    {
        smoothScrollTo(newScrollValue, getSmoothScrollDuration(), 0L,
            paramISmoothScrollRunnableListener);
    }

    public abstract interface ISmoothScrollRunnableListener
    {
        public abstract void onSmoothScrollFinished();
    }

    public class SmoothScrollRunnable implements Runnable
    {
        /**动画效果*/
        private final Interpolator mInterpolator;
        /**结束Y*/
        private final int mScrollToY;
        /**开始Y*/
        private final int mScrollFromY;
        /**滑动时间*/
        private final long mDuration;
        /**是否继续运行*/
        private boolean mContinueRunning = true;
        /**开始时刻*/
        private long mStartTime = -1;
        /**当前Y*/
        private int mCurrentY = -1;

        private ISmoothScrollRunnableListener listener;

        public SmoothScrollRunnable(int fromY, int toY, long duration,
                ISmoothScrollRunnableListener paramISmoothScrollRunnableListener)
        {
            mScrollFromY = fromY;
            mScrollToY = toY;
            mDuration = duration;
            listener = paramISmoothScrollRunnableListener;
            mInterpolator = new DecelerateInterpolator();
        }

        public void run()
        {
            if (mStartTime == -1L)
            {
                mStartTime = System.currentTimeMillis();
            }
            else
            {

                long scrollDuration = Math.max(
                    Math.min(1000L * (System.currentTimeMillis() - mStartTime)
                        / mDuration, 1000L), 0L);
                int scrolllength = Math.round((mScrollFromY - mScrollToY)
                    * mInterpolator
                            .getInterpolation((float) scrollDuration / 1000.0F));
                mCurrentY = (mScrollFromY - scrolllength);
                contentViewScrollTo(mCurrentY);
            }
            if ((mContinueRunning == true) && (mScrollToY != mCurrentY))
            {
                ViewCompat.postOnAnimation(MyScrollViewBase.this, this);
            }
            else if (listener != null)
            {
                listener.onSmoothScrollFinished();
            }
        }

        public void stop()
        {
            mContinueRunning = false;
            removeCallbacks(this);
            if (listener != null)
            {
                ISmoothScrollRunnableListener localISmoothScrollRunnableListener = listener;
                listener = null;
                localISmoothScrollRunnableListener.onSmoothScrollFinished();
            }
        }
    }

    public void recycleData()
    {
        // TODO Auto-generated method stub

    }
}
package com.mxk.ui.scrollstickyrefreshview;


import com.mxk.myapp.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
//import com.tencent.assistant.manager.cp;
//import com.tencent.assistant.utils.de;


public class RefreshListLoading extends MyLoadingLayoutBase
{
    private View mContainer;
    private ProgressBar loadingProgress;
    private ImageView refreshImage;
    private ImageView resultImage;
    private TextView refreshText;
    private TextView smallText;
    private RelativeLayout rlContent;
    private RotateAnimation animAntiClock;
    private RotateAnimation animClockWise;
    private CharSequence pullToRefresh;
    private CharSequence releaseToRefresh;
    private CharSequence refreshing;
    //    private CharSequence m;
    private CharSequence refreshSuc;
    private CharSequence refreshFail;
    private String p;

    //    private boolean isRefresh = false;

    public RefreshListLoading(Context paramContext, AttributeSet paramAttributeSet)
    {
        super(paramContext, paramAttributeSet);
        init(paramContext);
        setAnimation();
    }

    public RefreshListLoading(Context paramContext)
    {
        super(paramContext);
        init(paramContext);
        setAnimation();
    }

    private void setAnimation()
    {
        animAntiClock = new RotateAnimation(0.0F, -180.0F, 1, 0.5F, 1, 0.5F);
        animAntiClock.setInterpolator(new LinearInterpolator());
        animAntiClock.setDuration(100L);
        animAntiClock.setFillAfter(true);
        animClockWise = new RotateAnimation(-180.0F, 0.0F, 1, 0.5F, 1, 0.5F);
        animClockWise.setInterpolator(new LinearInterpolator());
        animClockWise.setDuration(100L);
        animClockWise.setFillAfter(true);
    }

    private void init(Context paramContext)
    {
        FrameLayout.LayoutParams localLayoutParams;

        pullToRefresh = paramContext
                .getString(R.string.refresh_list_loading_pull_from_start);
        refreshing = paramContext
                .getString(R.string.refresh_list_loading_refreshing_from_start);

        releaseToRefresh = paramContext
                .getString(R.string.refresh_list_loading_releaseToRefresh);
        refreshFail = paramContext.getString(R.string.refresh_fail);
        //            m = paramContext.getString(2131361866);
        refreshSuc = paramContext.getString(R.string.refresh_suc);
        LayoutInflater.from(paramContext).inflate(R.layout.refresh_list_loading, this);
        rlContent = ((RelativeLayout) findViewById(R.id.refresh_list_loading_content));
        mContainer = findViewById(R.id.left_ly);
        loadingProgress = ((ProgressBar) findViewById(R.id.refresh_list_loading_progress));
        refreshImage = ((ImageView) findViewById(R.id.refresh_image));
        resultImage = ((ImageView) findViewById(R.id.result_img));
        refreshText = ((TextView) findViewById(R.id.refresh_text));
        smallText = ((TextView) findViewById(R.id.small_txt));

        localLayoutParams = (FrameLayout.LayoutParams) rlContent.getLayoutParams();

//        localLayoutParams.height = Display.getPixel(getContext(), 60.0F);
        localLayoutParams.gravity = Gravity.BOTTOM;
        refreshImage.setVisibility(View.VISIBLE);

        reset();
    }

    public int getContentSize()
    {
        return rlContent.getHeight();
    }

    public int getTriggerSize()
    {
        return getResources().getDimensionPixelSize(R.dimen.pull_to_refresh_trigger_size);
    }

    public void hideAllSubViews()
    {
        if (refreshImage.getVisibility() == View.VISIBLE)
            refreshImage.setVisibility(View.INVISIBLE);
        if (resultImage.getVisibility() == View.VISIBLE)
            resultImage.setVisibility(View.INVISIBLE);
        if (refreshText.getVisibility() == View.VISIBLE)
            refreshText.setVisibility(View.INVISIBLE);
        if (smallText.getVisibility() == View.VISIBLE)
            smallText.setVisibility(View.INVISIBLE);
        if (loadingProgress.getVisibility() == View.VISIBLE)
            loadingProgress.setVisibility(View.INVISIBLE);
    }

    public void loadFail()
    {
        refreshImage.setVisibility(View.GONE);
        loadingProgress.setVisibility(View.GONE);
        resultImage.setImageResource(R.drawable.shibai);
        resultImage.setVisibility(View.VISIBLE);
        refreshText.setVisibility(View.VISIBLE);
        refreshText.setText(refreshFail);
        smallText.setVisibility(View.GONE);
    }

    public void loadFinish(String paramString)
    {
        refreshImage.setVisibility(View.GONE);
        resultImage.setVisibility(View.VISIBLE);
        try
        {
            resultImage.setImageResource(R.drawable.right);
            refreshText.setText(paramString);
            smallText.setVisibility(View.GONE);
            loadingProgress.setVisibility(View.GONE);
            if (!TextUtils.isEmpty(paramString))
            {
                mContainer.setVisibility(View.VISIBLE);
                refreshText.setVisibility(View.VISIBLE);
            }
        }
        catch (Throwable localThrowable)
        {
            mContainer.setVisibility(View.GONE);
            refreshText.setVisibility(View.GONE);
        }
    }

    public void loadSuc()
    {
        reset();
    }

    public void onPull(int paramInt)
    {
    }

    public void pullToRefresh()
    {
        refreshImage.setVisibility(View.VISIBLE);
        refreshImage.clearAnimation();
        refreshText.setVisibility(View.VISIBLE);
        refreshText.setText(pullToRefresh);
        smallText.setVisibility(View.GONE);
        loadingProgress.setVisibility(View.GONE);
        resultImage.setVisibility(View.GONE);
    }

    public void refreshFail()
    {
        refreshImage.setVisibility(View.GONE);
        loadingProgress.setVisibility(View.GONE);
        //        if (isRefresh)
        //        {
        //            resultImage.setImageResource(R.drawable.shibai_white);
        //        }
        //        else
        //        {
        resultImage.setImageResource(R.drawable.shibai);
        //        }

        resultImage.setVisibility(View.VISIBLE);
        refreshText.setVisibility(View.VISIBLE);
        refreshText.setText(refreshFail);
        smallText.setVisibility(View.GONE);

    }

    //
    //    public void refreshLoadingResource()
    //    {
    //        isRefresh = true;
    //        refreshImage.setImageResource(R.drawable.refresh_arrow);
    //        Drawable localDrawable = getContext().getResources().getDrawable(
    //            R.drawable.small_morefunction_installion);
    //        localDrawable.setBounds(0, Display.getPixel(getContext(), 16.0F), 0,
    //            Display.getPixel(getContext(), 16.0F));
    //        loadingProgress.setIndeterminateDrawable(localDrawable);
    //        refreshText.setTextColor(getContext().getResources().getColor(
    //            R.color.pull_refresh_txt_color));
    //        smallText.setTextColor(getContext().getResources().getColor(
    //            R.color.pull_refresh_txt_small_color));
    //    }

    public void refreshSuc()
    {
        refreshImage.setVisibility(View.GONE);
        loadingProgress.setVisibility(View.GONE);
        //        if (isRefresh)
        //        {
        resultImage.setImageResource(R.drawable.right);
        //        }
        //        else
        //        {
        //            resultImage.setImageResource(R.drawable.right_white);
        //        }

        resultImage.setVisibility(View.VISIBLE);
        refreshText.setVisibility(View.VISIBLE);
        refreshText.setText(refreshSuc);
        smallText.setVisibility(View.GONE);
    }

    public void refreshing()
    {
        refreshImage.clearAnimation();
        refreshImage.setVisibility(View.GONE);
        resultImage.setVisibility(View.GONE);
        refreshText.setText(refreshing);
        smallText.setVisibility(View.GONE);
        mContainer.setVisibility(View.VISIBLE);
        loadingProgress.setVisibility(View.VISIBLE);
    }

    public void releaseToRefresh()
    {
        refreshImage.clearAnimation();
        refreshImage.startAnimation(animAntiClock);
        refreshText.setVisibility(View.VISIBLE);
        refreshText.setText(releaseToRefresh);
        smallText.setVisibility(View.GONE);
    }

    public void reset()
    {
        refreshImage.setVisibility(View.VISIBLE);
        mContainer.setVisibility(View.VISIBLE);

        refreshImage.clearAnimation();
        refreshText.setVisibility(View.VISIBLE);
        refreshText.setText(pullToRefresh);
        loadingProgress.setVisibility(View.GONE);
        resultImage.setVisibility(View.GONE);

    }

    //    public void resetLoadingResource()
    //    {
    //        isRefresh = false;
    //        refreshImage.setImageResource(R.drawable.refresh_arrow_white);
    //        Drawable localDrawable = getContext().getResources().getDrawable(
    //            R.drawable.small_morefunction_installion_white);
    //        localDrawable.setBounds(0, Display.getPixel(getContext(), 16.0F), 0,
    //            Display.getPixel(getContext(), 16.0F));
    //        loadingProgress.setIndeterminateDrawable(localDrawable);
    //        refreshText.setTextColor(getContext().getResources().getColor(
    //            R.color.pull_refresh_txt_color));
    //        smallText.setTextColor(getContext().getResources().getColor(
    //            R.color.pull_refresh_txt_small_color));
    //    }

    public void setHeight(int paramInt)
    {
        getLayoutParams().height = paramInt;
        requestLayout();
    }

    public void setRefreshTimeKey(String paramString)
    {
        p = paramString;
    }

    public void setWidth(int paramInt)
    {
        getLayoutParams().width = paramInt;
        requestLayout();
    }

    public void showAllSubViews()
    {
        if (refreshImage.getVisibility() == View.INVISIBLE)
            refreshImage.setVisibility(View.VISIBLE);
        if (resultImage.getVisibility() == View.INVISIBLE)
            resultImage.setVisibility(View.VISIBLE);
        if (refreshText.getVisibility() == View.INVISIBLE)
            refreshText.setVisibility(View.VISIBLE);
        if ((smallText.getVisibility() != View.INVISIBLE)
            || (loadingProgress.getVisibility() == View.INVISIBLE))
            loadingProgress.setVisibility(View.VISIBLE);
    }
}
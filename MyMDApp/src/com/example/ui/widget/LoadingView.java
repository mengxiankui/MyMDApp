package com.example.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by wuheng on 14-9-9.
 */
public class LoadingView extends View
{
    private final static String TAG = LoadingView.class.getSimpleName();
    private Paint mPaint;
    private int mAngle;
    private float mRadius;
    private int mSize = 2;

    public LoadingView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setStrokeWidth(4);
    }

    @Override
    protected void onDraw (Canvas canvas)
    {
        super.onDraw(canvas);
        Log.d(TAG,"getVisibility() = "+getVisibility());
        if (getVisibility() != VISIBLE)
        {
            return;
        }
        canvas.translate(getWidth() / 2f - mRadius, getHeight() / 2f - mRadius);
        canvas.rotate(mAngle, mRadius, mRadius);
        for (int i = 0; i < 12; i++)
        {
            mPaint.setAlpha((int) Math.max(255 * 0.1f, 255 * (9 - i) / 10f));
            canvas.drawCircle(mRadius, Math.max(3 * mSize, 10 * mSize - i * mSize), Math.max(2 * mSize - 1, 7 * mSize - 4 - i),
                    mPaint);
            canvas.rotate(-30, mRadius, mRadius);
        }
        mAngle += 30;
        postInvalidateDelayed(80);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRadius = Math.min(w, h) / 2f;
    }

    @Override
    public void setVisibility(int visibility) {
        Log.d(TAG,"setVisibility = "+visibility);
        super.setVisibility(visibility);
    }

    public void setSize(int size)
    {
        this.mSize = size;
        invalidate();
    }
}

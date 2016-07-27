package com.example.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import com.example.mymdapp.R;

/**
 * Created by wuheng on 14-9-9.
 */
public class DotLoadingView extends View {
    private final static String TAG = DotLoadingView.class.getSimpleName();
    private final int FRAMES = 27;
    private final float CIRCLEANGLE = 360F;
    private Paint mPaint;
    private int color;
    private int duration;
    private float accelerate;
    private boolean startAnimate = false;

    private int durationInterval;
    private int currentFrame = 0;

    private float fInterpolatorMax;

    private float mDotRadius;

    private float mAngle = 0.0F;
    private float mRadius;
    AccelerateInterpolator accelerateInterpolator;
    private Handler handler;


    public DotLoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DotLoadingView);
        color = a.getColor(R.styleable.DotLoadingView_dot_color, Color.WHITE);
        duration = a.getInt(R.styleable.DotLoadingView_circle_duration, 1000);
        accelerate = a.getFloat(R.styleable.DotLoadingView_acceleration, 1.3F);
        a.recycle();
        durationInterval = duration / (FRAMES - 1);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(color);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setStrokeWidth(1);
        accelerateInterpolator = new AccelerateInterpolator(accelerate);
        fInterpolatorMax = accelerateInterpolator.getInterpolation(FRAMES/2) *2;
        handler = new Handler()
        {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                postInvalidate();
            }
        };
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.translate(getWidth() / 2f, getHeight() / 2f);
        //first ball
        canvas.save();
        mAngle = getAngle(currentFrame);
        canvas.rotate(mAngle, 0, 0);
        mPaint.setAlpha(255);
        canvas.drawCircle(mRadius-mDotRadius, 0, mDotRadius,
                mPaint);
        canvas.restore();

        //second ball
        canvas.save();
        mAngle = getAngle(currentFrame - 1);
        canvas.rotate(mAngle, 0, 0);
        mPaint.setAlpha(255 * 3 / 5);
        canvas.drawCircle(mRadius-mDotRadius, 0, mDotRadius * 0.83F,
                mPaint);
        canvas.restore();

        //third ball
        canvas.save();
        mAngle = getAngle(currentFrame - 2);
        canvas.rotate(mAngle, 0, 0);
        mPaint.setAlpha(255 / 2);
        canvas.drawCircle(mRadius-mDotRadius, 0, mDotRadius * 0.615F,
                mPaint);
        canvas.restore();

        //fourth ball
        canvas.save();
        mAngle = getAngle(currentFrame - 3);
        canvas.rotate(mAngle, 0, 0);
        mPaint.setAlpha(255 * 3 / 10);
        canvas.drawCircle(mRadius-mDotRadius, 0, mDotRadius * 0.615F,
                mPaint);
        canvas.restore();

        canvas.restore();
        currentFrame += 1;
        currentFrame %= FRAMES;
        if (startAnimate)
            handlerInvalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        float minWidth = Math.min(w, h);
        mDotRadius = minWidth * 0.064F;
        mRadius = minWidth / 2;
        startAnimate();
    }

    private void startAnimate() {
        startAnimate = true;
        handlerInvalidate();
    }

    private float getAngle(int frame) {
        if (frame < 0)
            frame += FRAMES;
        if (frame<FRAMES/2)
        {
            return accelerateInterpolator.getInterpolation(frame)/fInterpolatorMax * CIRCLEANGLE -CIRCLEANGLE/4;
        }
        else
        {
            return (1 - accelerateInterpolator.getInterpolation(FRAMES -frame)/fInterpolatorMax ) * CIRCLEANGLE -CIRCLEANGLE/4;
        }
    }

    public void setDotColor(int color) {
        this.color = color;
        mPaint.setColor(color);
        resetAnimation();
    }

    public void setDuration(int duration) {
        this.duration = duration;
        durationInterval = duration / (FRAMES - 1);
        resetAnimation();
    }

    public void setAccelerate(float accelerate) {
        this.accelerate = accelerate;
        accelerateInterpolator = new AccelerateInterpolator(accelerate);
        fInterpolatorMax = accelerateInterpolator.getInterpolation(FRAMES/2) *2;
        resetAnimation();
    }

    private void resetAnimation() {
        currentFrame = 0;
    }

    public int getColor() {
        return color;
    }

    public int getDuration() {
        return duration;
    }

    public float getAccelerate() {
        return accelerate;
    }

    private void handlerInvalidate()
    {
        if (handler.hasMessages(0))
            handler.removeMessages(0);
        handler.sendEmptyMessageDelayed(0,durationInterval);
    }
}

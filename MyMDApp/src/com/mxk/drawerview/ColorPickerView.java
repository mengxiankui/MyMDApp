package com.mxk.drawerview;


import com.example.mymdapp.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;


public class ColorPickerView extends View
{
    private Shader mValShader, mSatShader, mHueShader, mAlphaShader;
    private ComposeShader mSatValShader;
    private int miBorderWidth, miInnerBorderWidth, rectHeight;
    private RectF mSatValRect, mHueRect, mAlphaRect;
    private Paint borderPaint, innerborderPaint, SatValPaint, HuePaint, AlphaPaint,
            mSatValTrackerPaint, mHueTrackerPaint;
    private int miBorderColor, miInnerBorderColor;
    private int mHue = 0, mSat = 0, mVal = 0;
    private static final int PALETTE_CIRCLE_TRACKER_RADIUS = 5,
            RECTANGLE_TRACKER_OFFSET = 2;

    private AlphaPatternDrawable mAlphaPattern;

    private float cur_x, cur_y;

    public ColorPickerView(Context context)
    {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public ColorPickerView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }

    public ColorPickerView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ColorPickerView);
        miBorderWidth = (int) a.getDimension(R.styleable.ColorPickerView_border_width, 1);
        if (miBorderWidth <= 0)
        {
            miBorderWidth = 1;
        }
        miInnerBorderWidth = (int) a.getDimension(
            R.styleable.ColorPickerView_inner_border_width, 1);
        if (miInnerBorderWidth <= 0)
        {
            miInnerBorderWidth = 1;
        }
        miBorderColor = (int) a.getDimension(R.styleable.ColorPickerView_border_color,
            0xFFFFFF);
        if (miBorderColor <= 0)
        {
            miBorderColor = 0xFFFFFF;
        }
        miInnerBorderColor = (int) a.getDimension(
            R.styleable.ColorPickerView_inner_border_color, 0xFFFFFF);
        if (miInnerBorderColor <= 0)
        {
            miInnerBorderColor = 0xFFFFFF;
        }
        rectHeight = (int) a.getDimension(R.styleable.ColorPickerView_rectHeight, 2);
        borderPaint = new Paint();
        borderPaint.setColor(miBorderColor);
        borderPaint.setStrokeWidth(miBorderWidth);
        innerborderPaint = new Paint();
        innerborderPaint.setColor(miInnerBorderColor);
        innerborderPaint.setStrokeWidth(miInnerBorderWidth);
        SatValPaint = new Paint();
        HuePaint = new Paint();
        AlphaPaint = new Paint();
        mSatValTrackerPaint = new Paint();
        mHueTrackerPaint = new Paint();

        mAlphaPattern = new AlphaPatternDrawable(10);
        // TODO Auto-generated constructor stub
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {

        // TODO Auto-generated method stub 
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN :
            case MotionEvent.ACTION_MOVE :
            {
                cur_x = x;
                cur_y = y;
                invalidate();
                break;
            }
        }
        return true;
    }

    public void setHSVColor(int Hue, int Sat, int Val)
    {
        mHue = Hue;
        mSat = Sat;
        mVal = Val;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        // TODO Auto-generated method stub
        if (null == mSatValRect)
        {
            mSatValRect = new RectF(getPaddingLeft(), getPaddingTop(), (getWidth()
                - getPaddingLeft() - getPaddingRight()) * 3 / 4, (getHeight()
                - getPaddingTop() - getPaddingBottom()) * 3 / 4);
        }
        if (null == mHueRect)
        {
            mHueRect = new RectF(
                (getWidth() - getPaddingLeft() - getPaddingRight()) * 13 / 16,
                getPaddingTop(), getWidth() - getPaddingRight(), (getHeight()
                    - getPaddingTop() - getPaddingBottom()) * 3 / 4);
        }
        if (null == mAlphaRect)
        {
            mAlphaRect = new RectF(getPaddingLeft(),
                (getHeight() - getPaddingTop() - getPaddingBottom()) * 13 / 16,
                getWidth() - getPaddingRight(), getHeight() - getPaddingBottom());
        }
        //明度线性渲染器
        if (null == mValShader)
        {
            mValShader = new LinearGradient(mSatValRect.left, mSatValRect.top,
                mSatValRect.left, mSatValRect.bottom, 0xff000000, 0xffffffff,
                TileMode.CLAMP);
        }
        //HSV转化为RGB
        int rgb = Color.HSVToColor(new float[]{mHue, 1f, 1f});
        //饱和线性渲染器
        if (null == mSatShader)
        {
            mSatShader = new LinearGradient(mSatValRect.left, mSatValRect.top,
                mSatValRect.right, mSatValRect.top, 0xffffffff, rgb, TileMode.CLAMP);
        }
        if (null == mSatValShader)
        {
            mSatValShader = new ComposeShader(mValShader, mSatShader,
                PorterDuff.Mode.MULTIPLY);
            SatValPaint.setShader(mSatValShader);

        }
        canvas.drawRect(0, 0, getWidth(), getHeight(), borderPaint);
        canvas.drawRect(mSatValRect.left, mSatValRect.top, mSatValRect.right,
            mSatValRect.bottom, innerborderPaint);
        canvas.drawRect(mSatValRect.left + miInnerBorderWidth, mSatValRect.top
            + miInnerBorderWidth, mSatValRect.right - miInnerBorderWidth,
            mSatValRect.bottom - miInnerBorderWidth, SatValPaint);
        //初始化选择圆块的位置
        Point p = satValToPoint(mSat, mVal);
        //绘制黑色内圆
        mSatValTrackerPaint.setColor(0xff000000);
        canvas.drawCircle(p.x, p.y, PALETTE_CIRCLE_TRACKER_RADIUS - 1f,
            mSatValTrackerPaint);
        //绘制外圆
        mSatValTrackerPaint.setColor(0xffdddddd);
        canvas.drawCircle(p.x, p.y, PALETTE_CIRCLE_TRACKER_RADIUS, mSatValTrackerPaint);

        if (null == mHueShader)
        {
            mHueShader = new LinearGradient(mHueRect.left, mHueRect.top, mHueRect.left,
                mHueRect.bottom, getRGBValues(), null, TileMode.CLAMP);
            HuePaint.setShader(mHueShader);
        }
        canvas.drawRect(mHueRect, innerborderPaint);
        canvas.drawRect(mHueRect.left + miInnerBorderWidth, mHueRect.top
            + miInnerBorderWidth, mHueRect.right - miInnerBorderWidth, mHueRect.bottom
            - miInnerBorderWidth, HuePaint);

        //初始化色相选择器选择条位置
        Point p1 = hueToPoint(mHue);

        RectF r = new RectF();
        r.left = mHueRect.left - RECTANGLE_TRACKER_OFFSET;
        r.right = mHueRect.right + RECTANGLE_TRACKER_OFFSET;
        r.top = p1.y - rectHeight;
        r.bottom = p1.y + rectHeight;
        mHueTrackerPaint.setColor(Color.BLACK);
        //绘制选择条
        canvas.drawRoundRect(r, 2, 2, mHueTrackerPaint);

        mAlphaPattern.draw(canvas);
        float[] hsv = new float[]{mHue, mSat, mVal};//hsv数组
        int color = Color.HSVToColor(hsv);
        int acolor = Color.HSVToColor(0, hsv);

        if (null == mAlphaShader)
        {
            mAlphaShader = new LinearGradient(mAlphaRect.left, mAlphaRect.top,
                mAlphaRect.right, mAlphaRect.top, color, acolor, TileMode.CLAMP);
            AlphaPaint.setShader(mAlphaShader);
        }
        canvas.drawRect(mAlphaRect, innerborderPaint);
        canvas.drawRect(mAlphaRect.left + miInnerBorderWidth, mAlphaRect.top
            + miInnerBorderWidth, mAlphaRect.right - miInnerBorderWidth,
            mAlphaRect.bottom - miInnerBorderWidth, AlphaPaint);

    }

    private Point hueToPoint(int mHue2)
    {
        // TODO Auto-generated method stub
        return null;
    }

    private Point satValToPoint(int mSat2, int mVal2)
    {
        // TODO Auto-generated method stub
        return null;
    }

    private int[] getRGBValues()
    {
        int[] ihue = new int[361];
        for (int i = 0; i < ihue.length; i++)
        {
            ihue[i] = Color.HSVToColor(new float[]{i, 1f, 1f});
        }
        return ihue;

    }

    public interface IColorChange
    {
        public void onColorChange(int color);
    }

}

package com.mxk.drawerview;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


public class DrawerView extends View
{
    private Bitmap mBitmap;
    private Canvas mcanvas;
    private Paint paint;
    private int color;

    private float cur_x, cur_y;

    public DrawerView(Context context)
    {
        super(context);
        
        // TODO Auto-generated constructor stub
    }

    public DrawerView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }

    public DrawerView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        SharedPreferences sp = context.getSharedPreferences("mxk_Preferences", 0);
        color = sp.getInt("Paint_Color", Color.RED);
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
            {
                cur_x = x;
                cur_y = y;
                break;
            }

            case MotionEvent.ACTION_MOVE :
            {
                mcanvas.drawLine(cur_x, cur_y, x, y, paint);
                cur_x = x;
                cur_y = y;
                invalidate();
                break;
            }
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        // TODO Auto-generated method stub
        if (null == mBitmap)
        {
            mBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Config.ARGB_8888);
            mcanvas = new Canvas(mBitmap);
            mcanvas.drawColor(Color.GRAY);
            paint = new Paint();
            paint.setColor(color);
            paint.setStrokeWidth(8);
            paint.setStrokeMiter(2);
        }
        canvas.drawBitmap(mBitmap, getMatrix(), null);

    }

    public Bitmap getBitMap()
    {
        return mBitmap;
    }

    public void setPaintColor(int color)
    {
        paint.setColor(color); 

    }

}

package com.mxk.drawerview;


import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;


public class AlphaPatternDrawable extends Drawable
{
    private int mRectangleSize = 10;

    private Paint mPaintWhite = new Paint();
    private Paint mPaintGray = new Paint();

    private int numRectanglesHorizontal;
    private int numRectanglesVertical;
    /** 
         * Bitmap in which the pattern will be cahched. 
         */
    private Bitmap mBitmap;

    public AlphaPatternDrawable(int RectangleSize)
    {
        super();
        mRectangleSize = RectangleSize;
        mPaintWhite.setColor(0xffffffff);
        mPaintGray.setColor(0xffcbcbcb);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void draw(Canvas canvas)
    {
        // TODO Auto-generated method stub
        canvas.drawBitmap(mBitmap, null, getBounds(), null);
    }

    @Override
    public void setAlpha(int alpha)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void setColorFilter(ColorFilter cf)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public int getOpacity()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    protected void onBoundsChange(Rect bounds)
    {
        // TODO Auto-generated method stub
        super.onBoundsChange(bounds);
        int height = bounds.height();
        int width = bounds.width();

        numRectanglesHorizontal = (int) Math.ceil((width / mRectangleSize));
        numRectanglesVertical = (int) Math.ceil(height / mRectangleSize);
        generatePatternBitmap();
    }

    /** 
         * This will generate a bitmap with the pattern 
         * as big as the rectangle we were allow to draw on. 
         * We do this to chache the bitmap so we don't need to 
        * recreate it each time draw() is called since it 
         * takes a few milliseconds. 
         */
    private void generatePatternBitmap()
    {

        if (getBounds().width() <= 0 || getBounds().height() <= 0)
        {
            return;
        }

        mBitmap = Bitmap.createBitmap(getBounds().width(), getBounds().height(),
            Config.ARGB_8888);
        Canvas canvas = new Canvas(mBitmap);

        Rect r = new Rect();
        boolean verticalStartWhite = true;
        for (int i = 0; i <= numRectanglesVertical; i++)
        {

            boolean isWhite = verticalStartWhite;
            for (int j = 0; j <= numRectanglesHorizontal; j++)
            {

                r.top = i * mRectangleSize;
                r.left = j * mRectangleSize;
                r.bottom = r.top + mRectangleSize;
                r.right = r.left + mRectangleSize;

                canvas.drawRect(r, isWhite ? mPaintWhite : mPaintGray);

                isWhite = !isWhite;
            }

            verticalStartWhite = !verticalStartWhite;

        }

    }

}
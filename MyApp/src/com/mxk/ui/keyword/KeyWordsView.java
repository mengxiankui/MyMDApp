package com.mxk.ui.keyword;


import java.util.ArrayList;
import java.util.Iterator;

import android.R.color;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class KeyWordsView extends ViewGroup
{

    private ArrayList<Integer> rowHeights = new ArrayList<Integer>();
    private ArrayList<ArrayList<View>> rowViews = new ArrayList<ArrayList<View>>();

    public KeyWordsView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }

    public KeyWordsView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public KeyWordsView(Context context)
    {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public void addViews(ArrayList<String> keys)
    {
        removeAllViews();
        if (null != keys)
        {
            Iterator<String> iterator = keys.iterator();
            while (iterator.hasNext())
                addView((String) iterator.next());
        }
        postInvalidate();
    }

    public void addView(String key)
    {
        TextView textView = new TextView(getContext());
        textView.setText(key);
        textView.setTextSize(18f);
        textView.setTextColor(Color.BLACK);
        textView.setBackgroundColor(Color.DKGRAY);
        textView.setGravity(Gravity.CENTER);
        MarginLayoutParams layoutParams = new MarginLayoutParams(
            LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(5, 2, 5, 2);
        textView.setLayoutParams(layoutParams);
        addView(textView);
        
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {
        // TODO Auto-generated method stub
        rowHeights.clear();
        rowViews.clear();
        int width = 0;
        int height = 0;
        int parentWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        ArrayList<View> arrayList;
        final int count = getChildCount();
        arrayList = new ArrayList<View>();
        for (int i = 0; i < count; i++)
        {
            final View child = getChildAt(i);
            
            if (child == null)
            {

            }
            else if (child.getVisibility() != GONE)
            {
                final int childWidth = child.getMeasuredWidth();
                final int childHeight = child.getMeasuredHeight();
                MarginLayoutParams layoutParams = (MarginLayoutParams) child
                        .getLayoutParams();

                if (width + childWidth + layoutParams.leftMargin
                    + layoutParams.rightMargin > parentWidth)
                {
                    rowViews.add(arrayList);
                    rowHeights.add(height);
                    arrayList = new ArrayList<View>();
                    width = 0;
                    height = 0;
                }
                width += childWidth + layoutParams.leftMargin + layoutParams.rightMargin;
                height = Math.max(height, childHeight + layoutParams.topMargin
                    + layoutParams.bottomMargin);
                arrayList.add(child);

            }
        }
        int top = 0;
        int bottom = getPaddingTop();
        for (int i = 0; i < rowViews.size(); i++)
        {
            top = bottom;
            bottom = top+ rowHeights.get(i);
            int left = 0;
            int right = getPaddingLeft();
            for (int j = 0; j < rowViews.get(i).size(); j++)
            {
                View child = rowViews.get(i).get(j);
                MarginLayoutParams layoutParams = (MarginLayoutParams) child
                        .getLayoutParams();
                left = right + layoutParams.leftMargin;
                right = left + child.getMeasuredWidth();
                rowViews.get(i)
                        .get(j)
                        .layout(left, top + layoutParams.topMargin, right,
                            bottom - layoutParams.bottomMargin);
                right += layoutParams.rightMargin;
            }

        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(widthMeasureSpec);

        int realWidth = 0;
        int realHeight = 0;
        int rowWidth = 0;
        int childWidth = 0;
        int childHeight = 0;
        int count = getChildCount();
        for (int i = 0; i < count; i++)
        {
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            MarginLayoutParams marginLayoutParams = (MarginLayoutParams) child
                    .getLayoutParams();
            childWidth = child.getMeasuredWidth() + marginLayoutParams.leftMargin
                + marginLayoutParams.rightMargin;
            childHeight = child.getMeasuredHeight() + marginLayoutParams.topMargin
                + marginLayoutParams.bottomMargin;
            if (realWidth == 0)
            {
                realWidth = childWidth;
            }

            if (realHeight == 0)
            {
                realHeight += childHeight;
            }

            if (rowWidth + childWidth > widthSize)
            {
                if (rowWidth > 0)
                {
                    realHeight += childHeight;
                    rowWidth = childWidth;

                }
                else
                {
                    rowWidth = widthSize;
                }
            }
            else
            {
                rowWidth += childWidth;
            }
            realWidth = realWidth > rowWidth ? realWidth : rowWidth;
        }

        if (widthMode == MeasureSpec.EXACTLY)
        {
            realWidth = widthSize;
        }

        if (heightMode == MeasureSpec.EXACTLY)
        {
            realHeight = heightSize;
        }
        setMeasuredDimension(realWidth, realHeight);
    }
}
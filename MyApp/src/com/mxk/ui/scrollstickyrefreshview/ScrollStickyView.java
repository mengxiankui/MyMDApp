package com.mxk.ui.scrollstickyrefreshview;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;


public abstract class ScrollStickyView extends MyRefreshView 
{
    
    public ScrollStickyView(Context paramContext)
    {
        super(paramContext);
//        init();
    }

    public ScrollStickyView(Context paramContext, AttributeSet paramAttributeSet)
    {
        super(paramContext, paramAttributeSet);
//        init();
    }
//    public void init()
//    {
//        setRefreshListViewListener(this);
//    }

    public void recycleData()
    {
        super.recycleData();
    }

    public void refreshData()
    {
        
    }

    @Override
    protected abstract LinearLayout createContentHeaderView(Context paramContext);
//    {
//        FrameLayout headerview = (FrameLayout) ((LayoutInflater) paramContext
//                .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
//            R.layout.scroll_header, null);
//        LinearLayout header = (LinearLayout) headerview.findViewById(R.id.header);
//        headerview.removeView(header);
//        return header;
//    }

    @Override
    protected abstract LinearLayout createContentBodyView(Context paramContext);
//    {
//        FrameLayout pagerview = (FrameLayout) ((LayoutInflater) paramContext
//                .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
//            R.layout.scroll_viewpager, null);
//        LinearLayout viewpager = (LinearLayout) pagerview.findViewById(R.id.pager_llayout);
//        pagerview.removeView(viewpager);
//        return viewpager;
//    }
}
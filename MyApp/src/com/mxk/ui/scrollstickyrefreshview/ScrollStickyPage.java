package com.mxk.ui.scrollstickyrefreshview;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;
import android.widget.RelativeLayout;



public abstract class ScrollStickyPage extends RelativeLayout
{
    private ScrollStickyView view;
    private Context mContext;

    public ScrollStickyPage(Context paramContext)
    {
        super(paramContext);
        initView(paramContext);
        this.mContext = paramContext;
    }

    public ScrollStickyPage(Context paramContext, AttributeSet paramAttributeSet)
    {
        super(paramContext, paramAttributeSet);
        initView(paramContext);
        this.mContext = paramContext;
    }

    public ScrollStickyPage(Context paramContext, AttributeSet paramAttributeSet,
            int paramInt)
    {
        super(paramContext, paramAttributeSet, paramInt);
        initView(paramContext);
        this.mContext = paramContext;
    }
    
    public void setInnerListView(ListView listView)
    {
        view.setListView(listView);
    }

    protected abstract void initView(Context paramContext);
//    {
//        LayoutInflater.from(paramContext).inflate(R.layout.scroll_listpage, this);
//        this.view = ((ScrollStickyView) findViewById(R.id.applist));
//        ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
//        viewTreeObserver.addOnGlobalLayoutListener(new OnGlobalLayoutListener()
//        {
//            @Override
//            public void onGlobalLayout()
//            {
//                view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//                getListView().getHeader().setLayoutParams(
//                    new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 400));
//                getListView().getViewpager().setLayoutParams(
//                    new LinearLayout.LayoutParams(view.getWidth(), view.getHeight()));
////                listView.setLayoutParams(new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
//                //            = layout(getListView().getHeader().getLeft(), getListView().getHeader().getTop(),
//                //                listView.getWidth() - listView.getPaddingRight()
//                //                    - listView.getPaddingLeft() + getListView().getHeader().getLeft(), 10000 + getListView().getHeader().getTop());
//                //            getListView().getHeader().invalidate();
//                //            getListView().getViewpager().layout(getListView().getViewpager().getLeft(), getListView().getViewpager().getTop(),
//                //                listView.getWidth() - listView.getPaddingRight()
//                //                    - listView.getPaddingLeft() + getListView().getViewpager().getLeft(), listView
//                //                        .getHeight()
//                //                    - listView.getPaddingBottom()
//                //                    - listView.getPaddingTop() + getListView().getViewpager().getTop());
//                //            listView.invalidate();
//            }
//        });
//    }

    public ScrollStickyView getContentView()
    {
        return this.view;
    }
    
    public void setContentView(ScrollStickyView view)
    {
        this.view = view;
    }

    public void recycleData()
    {
        this.view.recycleData();
    }

    public void refreshData()
    {
        this.view.refreshData();
    }
    
    public void onLoadFinish(boolean success)
    {
        this.view.onLoadFinish(success);
    }

}
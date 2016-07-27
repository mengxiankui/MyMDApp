package com.mxk.ui.pulltorefreshview;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;


public abstract class PullToRefreshListPage extends RelativeLayout
{
    private PullToRefreshListView listView;
    private Context mContext;

    public PullToRefreshListPage(Context paramContext)
    {
        super(paramContext);
        initView(paramContext);
        this.mContext = paramContext;
    }

    public PullToRefreshListPage(Context paramContext, AttributeSet paramAttributeSet)
    {
        super(paramContext, paramAttributeSet);
        initView(paramContext);
        this.mContext = paramContext;
    }

    public PullToRefreshListPage(Context paramContext, AttributeSet paramAttributeSet,
            int paramInt)
    {
        super(paramContext, paramAttributeSet, paramInt);
        initView(paramContext);
        this.mContext = paramContext;
    }

    protected abstract void initView(Context paramContext);

    //  {
    //    LayoutInflater.from(paramContext).inflate(R.layout.category_listpage, this);
    //    this.listView = ((PullToRefreshListView)findViewById(R.id.applist));
    ////    this.listView.setVisibility(View.GONE);
    //    this.listView.setDivider(null);
    //    this.listView.setSelector(new ColorDrawable(0));
    //    this.listView.setCacheColorHint(0);
    //  }

    public boolean isScrollStateIdle()
    {
        return this.listView.isScrollStateIdle();
    }

    public void onPause()
    {
        this.listView.onPause();
    }

    public void onResume()
    {
        this.listView.onResume();
    }

    public void recycleData()
    {
        this.listView.recycleData();
    }

    public void refreshData()
    {
        this.listView.refreshData();
    }

    public void setAdapter(BaseAdapter paramBaseAdapter)
    {
        this.listView.setAdapter(paramBaseAdapter);
    }

    public void setListView(PullToRefreshListView listView)
    {
        this.listView = listView;
    }

    public PullToRefreshListView getListView()
    {
        return this.listView;
    }
}
package com.mxk.ui.pulltorefreshview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.BaseAdapter;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.List;

public class PullToRefreshListView extends MyGetMoreListView
{
//  private l callBack = new an(this);
//  private by engine = null;
  private BaseAdapter mAdapter;

  public PullToRefreshListView(Context paramContext)
  {
    super(paramContext);
//    engine.register(callBack);
    initAdapter();
    setDivider(null);
  }

  public PullToRefreshListView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
//    engine.register(callBack);
    initAdapter();
    setDivider(null);
  }

  public PullToRefreshListView(Context paramContext, MyScrollViewBase.ScrollMode paramScrollMode)
  {
    super(paramContext);
//    engine.register(callBack);
    initAdapter();
    setDivider(null);
  }


  public ListView getListView()
  {
    return (ListView)mScrollContentView;
  }

  public void initAdapter()
  {
    ListAdapter localListAdapter = ((ListView)mScrollContentView).getAdapter();
    if ((localListAdapter instanceof HeaderViewListAdapter))
    {
      mAdapter = ((BaseAdapter)((HeaderViewListAdapter)localListAdapter).getWrappedAdapter());
    }
    else
    {
        mAdapter = ((BaseAdapter)((ListView)mScrollContentView).getAdapter());
    }
//    setRefreshListViewListener(this);
  }

  public void onPause()
  {
  }

  public void onResume()
  {
  }

  public void recycleData()
  {
    super.recycleData();
//    engine.unregister(callBack);
  }

  public void refreshData()
  {
    if (mAdapter == null)
      initAdapter();
    if ((mAdapter != null) && (mAdapter.getCount() > 0))
    {
        mAdapter.notifyDataSetChanged();
//        onRefreshComplete(true);
        
    }
//    engine.b();
  }

//@Override
//public void onMyRefreshListViewRefresh(ScrollState paramScrollState)
//{
//    // TODO Auto-generated method stub
//    getHandler().postDelayed(new Runnable()
//    {
//        
//        @Override
//        public void run()
//        {
//            // TODO Auto-generated method stub
//            onNextPageComplete(true);
//        }
//    }, 2000);
//}
}
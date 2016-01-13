package com.example.activities;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import butterknife.Bind;
import butterknife.ButterKnife;

import com.example.adapter.MyRecyclerAdapter;
import com.example.adapter.MyStaggergridRecyclerAdapter;
import com.example.mymdapp.R;
import com.mxk.baseapplication.LBaseActivity;

public class RecyclerViewActivity extends LBaseActivity {

	@Bind(R.id.recyclerview)
	RecyclerView recyclerView;

	LinearLayoutManager linearlayoutManager;
	GridLayoutManager gridlayoutManager;
	StaggeredGridLayoutManager staggeredGridLayoutManager;

	private ArrayList<String> list = new ArrayList<>();
	MyRecyclerAdapter adapter;
	MyStaggergridRecyclerAdapter staggergridRecyclerAdapter;

	@Override
	public int getContentView() {
		// TODO Auto-generated method stub
		return R.layout.activity_recyclerview;
	}

	@Override
	public void setToolBar(Toolbar mToolbar) {
		// TODO Auto-generated method stub
		mToolbar.setTitle("RecyclerView");
	}

	@Override
	public void onAfterOnCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		ButterKnife.bind(this);
		setupToolBar();
		setupRecyclerView();
	}

	private void setupRecyclerView() {
		// TODO Auto-generated method stub
		linearlayoutManager = new LinearLayoutManager(this,
				OrientationHelper.HORIZONTAL, false);
		gridlayoutManager = new GridLayoutManager(this, 4);
		staggeredGridLayoutManager = new StaggeredGridLayoutManager(4, OrientationHelper.VERTICAL);
		recyclerView.setLayoutManager(linearlayoutManager);
		
		recyclerView.setItemAnimator(new DefaultItemAnimator());
		for (int i = 0; i < 10; i++) {
			list.add(String.valueOf(i));
		}
		adapter = new MyRecyclerAdapter(list);
		staggergridRecyclerAdapter = new MyStaggergridRecyclerAdapter(list);
		recyclerView.setAdapter(adapter);
		recyclerView.setLayoutAnimation(getAnimationController());

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.recyclerview, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.plus:
			//面向抽象编程啊啊啊啊啊啊啊啊啊啊
//			。。
			adapter.addRandomData();
			break;
		case R.id.minus:
			adapter.removeRandomData();
			break;
		case R.id.horizontal:
			linearlayoutManager.setOrientation(OrientationHelper.HORIZONTAL);
			recyclerView.setLayoutAnimation(getAnimationController());
			recyclerView.setLayoutManager(linearlayoutManager);
			recyclerView.setAdapter(adapter);
			break;
		case R.id.vertical:
			linearlayoutManager.setOrientation(OrientationHelper.VERTICAL);
			recyclerView.setLayoutAnimation(getAnimationController());
			recyclerView.setLayoutManager(linearlayoutManager);
			recyclerView.setAdapter(adapter);
			break;
		case R.id.grid:
			recyclerView.setLayoutAnimation(getAnimationController());
			recyclerView.setLayoutManager(gridlayoutManager);
			recyclerView.setAdapter(adapter);
			
			break;

		case R.id.staggeredgrid_h:
			staggeredGridLayoutManager.setOrientation(OrientationHelper.HORIZONTAL);
			staggeredGridLayoutManager.setSpanCount(8);
			recyclerView.setLayoutAnimation(getAnimationController());
			recyclerView.setLayoutManager(staggeredGridLayoutManager);
			recyclerView.setAdapter(adapter);
			
			break;
		case R.id.staggeredgrid_v:
			staggeredGridLayoutManager.setOrientation(OrientationHelper.VERTICAL);
			staggeredGridLayoutManager.setSpanCount(4);
			recyclerView.setLayoutAnimation(getAnimationController());
			recyclerView.setLayoutManager(staggeredGridLayoutManager);
			recyclerView.setAdapter(staggergridRecyclerAdapter);
			
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void setupToolBar() {
		// TODO Auto-generated method stub
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getToolBar().setNavigationOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
	}

	/**
	 * Layout动画
	 * 
	 * @return
	 */
	protected LayoutAnimationController getAnimationController() {
		int duration = 100;
		AnimationSet set = new AnimationSet(true);

		Animation animation = new AlphaAnimation(0.0f, 1.0f);
		animation.setInterpolator(new DecelerateInterpolator());
		animation.setDuration(duration);
		set.addAnimation(animation);

//		animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
//				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
//				0.5f, Animation.RELATIVE_TO_SELF, 0.0f);
//		animation.setDuration(duration);
//		set.addAnimation(animation);

		LayoutAnimationController controller = new LayoutAnimationController(
				set, 0.5f);
		controller.setOrder(LayoutAnimationController.ORDER_NORMAL);
		return controller;
	}
}

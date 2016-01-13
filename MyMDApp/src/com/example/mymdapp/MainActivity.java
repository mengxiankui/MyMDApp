package com.example.mymdapp;

import butterknife.Bind;
import butterknife.ButterKnife;

import com.example.adapter.SlideMenuAdapter;
import com.example.util.MemuActivityManager;
import com.mxk.baseapplication.LBaseActivity;
import com.mxk.baseframe.util.toast.frenchtoast.FrenchToast;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.IntentCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

public class MainActivity extends LBaseActivity {

	@Bind(R.id.drawer)
	DrawerLayout mDrawerLayout;
	@Bind(R.id.recyclerview)
	RecyclerView mRecyclerView;
	@Bind(R.id.pager)
	LinearLayout pager;
	@Bind(R.id.navdrawer)
	LinearLayout navDrawer;

	private ActionBarDrawerToggle mDrawerToggle;
	private SlideMenuAdapter adapter;
	private OnItemClickListener itemClickListener;

	@Override
	public int getContentView() {
		// TODO Auto-generated method stub
		return R.layout.activity_main_dr;
	}

	@Override
	public void onAfterOnCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		ButterKnife.bind(this);
		setupToolBar();
		setupDrawer();

	}

	private void setupToolBar() {
		// TODO Auto-generated method stub
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, getToolBar(), R.string.drawer_open, R.string.drawer_close);
		mDrawerToggle.syncState();
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		
	}

	private void setupDrawer() {
		// TODO Auto-generated method stub
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);

		mRecyclerView.setHasFixedSize(true);
		LinearLayoutManager layoutManager = new LinearLayoutManager(this);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		mRecyclerView.setLayoutManager(layoutManager);

		adapter = new SlideMenuAdapter(getResources().getStringArray(
				R.array.menu_name));
		mRecyclerView.setAdapter(adapter);

		itemClickListener = new OnItemClickListener();
		adapter.setOnItemClickListener(itemClickListener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void setToolBar(Toolbar mToolbar) {
		// TODO Auto-generated method stub
		mToolbar.setTitle(R.string.app_name);
	}

	private class OnItemClickListener
			implements
			com.mxk.baseframe.baseadapter.BaseRecylerViewAdapter.OnItemClickListener {

		@Override
		public void onItemClick(View v, int position) {
			// TODO Auto-generated method stub
			FrenchToast.with(MainActivity.this).shortLength()
					.showText(adapter.getData(position));
			Activity a = MemuActivityManager.getActivityAtPosition(position, MainActivity.this);
			if (null != a) {
				Intent i = new Intent(MainActivity.this, a.getClass());
				startActivity(i);
			}
		}

	}

}

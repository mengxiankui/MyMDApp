package com.example.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.mymdapp.R;
import com.gc.materialdesign.views.Switch;
import com.gc.materialdesign.views.Switch.OnCheckListener;
import com.mxk.baseapplication.LBaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class QHBActivity extends LBaseActivity {

	@Bind(R.id.switch1)
	Switch switch1;
	@Bind(R.id.switch2)
	Switch switch2;
	@Bind(R.id.switch3)
	Switch switch3;
	
	@Bind(R.id.switch4)
	Switch switch4;
	@Bind(R.id.switch5)
	Switch switch5;
	@Bind(R.id.switch6)
	Switch switch6;
	

	@Override
	public int getContentView() {
		// TODO Auto-generated method stub
		return R.layout.activity_toolbar;
	}

	@Override
	public void setToolBar(Toolbar mToolbar) {
		// TODO Auto-generated method stub
//		mToolbar.setTitle("ToolBar测试");
//		mToolbar.setSubtitle("subtitle测试");
	}

	@Override
	public void onAfterOnCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		ButterKnife.bind(this);
		switch1.setOncheckListener(new OnCheckListener() {

			@Override
			public void onCheck(boolean check) {
				// TODO Auto-generated method stub
				getSupportActionBar().setDisplayHomeAsUpEnabled(check);
			}
		});
		switch2.setOncheckListener(new OnCheckListener() {

			@Override
			public void onCheck(boolean check) {
				// TODO Auto-generated method stub
				if (check) {
					getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_ab_up_ltr);
				}
				else
				{
					getSupportActionBar().setHomeAsUpIndicator(null);
				}
				
			}
		});
		switch3.setOncheckListener(new OnCheckListener() {

			@Override
			public void onCheck(boolean check) {
				// TODO Auto-generated method stub
				if (check) {
					getToolBar().setNavigationIcon(R.drawable.ic_launcher);
				}
				else
				{
					getToolBar().setNavigationIcon(null);
				}
			}
		});
		
		switch4.setOncheckListener(new OnCheckListener() {

			@Override
			public void onCheck(boolean check) {
				// TODO Auto-generated method stub
				if (check) {
					getToolBar().setLogo(R.drawable.ic_launcher);
				}
				else
				{
					getToolBar().setLogo(null);
				}
			}
		});
		
		switch5.setOncheckListener(new OnCheckListener() {

			@Override
			public void onCheck(boolean check) {
				// TODO Auto-generated method stub
				if (check) {
					getToolBar().setTitle("ToolBar测试");
				}
				else
				{
					getToolBar().setTitle(null);
				}
				
			}
		});
		switch6.setOncheckListener(new OnCheckListener() {

			@Override
			public void onCheck(boolean check) {
				// TODO Auto-generated method stub
				if (check) {
					getToolBar().setSubtitle("Subtitle测试");
				}
				else
				{
					getToolBar().setSubtitle(null);
				}
				
			}
		});
		
		getToolBar().setNavigationOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
	}

}

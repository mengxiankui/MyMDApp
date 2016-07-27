package com.example.activities;

import android.app.AlertDialog;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentUris;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mymdapp.R;
import com.example.util.Consts.DiaryConst;
import com.example.util.TimeUtil;
import com.mxk.baseapplication.LBaseActivity;
import com.tencent.qcload.playersdk.ui.VideoRootFrame;
import com.tencent.qcload.playersdk.util.VideoInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TencentVodActivity extends LBaseActivity{

	@Bind(R.id.player)
    VideoRootFrame player;

	@Override
	public int getContentView() {
		// TODO Auto-generated method stub
		return R.layout.activity_tencent_vod;
	}

	@Override
	public void setToolBar(Toolbar mToolbar) {
		// TODO Auto-generated method stub
		mToolbar.setTitle("腾讯点播测试");
	}

	@Override
	public void onAfterOnCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		ButterKnife.bind(this);
		setupToolBar();
		init();
		initAction();
	}

	private void initAction() {
		// TODO Auto-generated method stub
	}

	private void init() {
		// TODO Auto-generated method stub

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

    @Override
    protected void onResume() {
        super.onResume();
        //调用播放器的播放方法
        List<VideoInfo> videos = new ArrayList<VideoInfo>();
        VideoInfo v1 = new VideoInfo();
        v1.description = " 标清 ";
        v1.type =VideoInfo.VideoType.MP4;
        v1.url = "http://200021397.vod.myqcloud.com/200021397_d5c191c03d0511e6b606adf84c4a3aac.f20.mp4";
        videos.add(v1);
//        VideoInfo v2 = new VideoInfo();
//        v2.description = " 高清 ";
//        v2.type =VideoInfo.VideoType.MP4;
//        v2.url = "http://4500.vod.myqcloud.com/4500_d754e448e74c11e4ad9e37e079c2b389. f0.mp4?vkey=77F279B72A3788656E0A14837DA6C89AA57D5CA46FBAD14A81FE3B63FE2DE92 C5668CBD27304071B&ocid=12345";
//        videos.add(v2);
        player.play(videos);
    }
}


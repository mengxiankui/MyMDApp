package com.example.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.mymdapp.R;
import com.example.ui.widget.DotLoadingView;
import com.mxk.baseapplication.LBaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ProgressActivity extends LBaseActivity {

	@Bind(R.id.loading_view)
	DotLoadingView loadingView;

	@Bind(R.id.accelerate_txt)
	TextView txtAccelerate;

	@Bind(R.id.accelerate_bar)
	SeekBar seekBarAccelerate;

	@Bind(R.id.duration_txt)
	TextView txtDuration;

	@Bind(R.id.duration_bar)
	SeekBar seekBarDuration;

	@Bind(R.id.color_edit)
	EditText editColor;

	@Bind(R.id.color_confirm_btn)
	Button btnColor;


	@Override
	public int getContentView() {
		return R.layout.activity_progress;
	}

	@Override
	public void setToolBar(Toolbar mToolbar) {
		mToolbar.setTitle("自定义progressbar");
	}

	@Override
	public void onAfterOnCreate(Bundle savedInstanceState) {
		ButterKnife.bind(this);
		setupToolBar();
		init();
		bindWidgets();
	}

	private void bindWidgets() {
		seekBarAccelerate.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				txtAccelerate.setText(String.valueOf(progress/10F));
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				txtAccelerate.setText(String.valueOf(seekBar.getProgress()/10F));
				loadingView.setAccelerate(seekBar.getProgress()/10F);
			}
		});

		seekBarDuration.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				txtDuration.setText(String.valueOf(progress));
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				txtDuration.setText(String.valueOf(seekBar.getProgress()));
				loadingView.setDuration(seekBar.getProgress());
			}
		});
		btnColor.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String strColor = editColor.getText().toString();
				if (strColor.length() != 6 && strColor.length() != 8)
				{
					return;
				}
				if (!strColor.startsWith("#"))
				{
					strColor = "#" + strColor;
				}
				int color= Color.parseColor(strColor);
				loadingView.setDotColor(color);
			}
		});
	}

	private void init() {
		// TODO Auto-generated method stub
		txtAccelerate.setText(String.valueOf(loadingView.getAccelerate()));
		seekBarAccelerate.setProgress((int)(loadingView.getAccelerate()*10));
		txtDuration.setText(String.valueOf(loadingView.getDuration()));
		seekBarDuration.setProgress(loadingView.getDuration());
		editColor.setText(String.valueOf(Integer.toHexString(loadingView.getColor())));
	}

	private void setupToolBar() {
		// TODO Auto-generated method stub
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getToolBar().setNavigationOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
	}
}

package com.example.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import butterknife.Bind;
import butterknife.ButterKnife;

import com.example.fragments.TuLingChatFragment;
import com.example.mymdapp.R;
import com.mxk.baseapplication.LBaseActivity;

public class TuLingLauncherActivity extends LBaseActivity {

	@Bind(R.id.edit)
	EditText editText;
	@Bind(R.id.btn_hanzi)
	Button btnHanzi;
	@Bind(R.id.btn_meizi)
	Button btnMeizi;

	@Bind(R.id.ll_lock)
	LinearLayout llLock;

	@Override
	public int getContentView() {
		// TODO Auto-generated method stub
		return R.layout.activity_tuling_launcher;
	}

	@Override
	public void setToolBar(Toolbar mToolbar) {
		// TODO Auto-generated method stub
		mToolbar.setTitle("图灵聊天机器人");
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
		btnHanzi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (editText.getText().toString().equals("19870114")) {
					TuLingChatFragment chatFragment = new TuLingChatFragment();
					Bundle bundle = new Bundle();
					bundle.putBoolean("IsHanZi", true);
					chatFragment.setArguments(bundle);
					FragmentManager fragmentManager = getSupportFragmentManager();
					fragmentManager
							.beginTransaction()
							.add(R.id.container, chatFragment,
									"TuLingChatFragment").commit();
					editText.setText("");
					InputMethodManager inputManager = (InputMethodManager) editText
							.getContext().getSystemService(
									Context.INPUT_METHOD_SERVICE);

					inputManager.hideSoftInputFromWindow(
							editText.getApplicationWindowToken(), 0);
				} else {
					showAlertDialog();

				}

			}
		});
		btnMeizi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (editText.getText().toString().equals("19880308")) {
					TuLingChatFragment chatFragment = new TuLingChatFragment();
					Bundle bundle = new Bundle();
					bundle.putBoolean("IsHanZi", false);
					chatFragment.setArguments(bundle);
					FragmentManager fragmentManager = getSupportFragmentManager();
					fragmentManager
							.beginTransaction()
							.add(R.id.container, chatFragment,
									"TuLingChatFragment").commit();
					editText.setText("");
					InputMethodManager inputManager = (InputMethodManager) editText
							.getContext().getSystemService(
									Context.INPUT_METHOD_SERVICE);

					inputManager.hideSoftInputFromWindow(
							editText.getApplicationWindowToken(), 0);
				} else {
					showAlertDialog();

				}
			}
		});
	}

	protected void showAlertDialog() {
		// TODO Auto-generated method stub
		new AlertDialog.Builder(this).setMessage(R.string.alert_wrong_birthday)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

					}
				}).create().show();
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

}

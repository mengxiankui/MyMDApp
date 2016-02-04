package com.example.activities;

import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import butterknife.Bind;
import butterknife.ButterKnife;

import com.example.mymdapp.R;
import com.example.util.Consts.DiaryConst;
import com.mxk.baseapplication.LBaseActivity;

public class DiaryActivity extends LBaseActivity {

	@Bind(R.id.edittext_title)
	EditText etxtTitle;
	@Bind(R.id.edittext_content)
	EditText etxtContent;
	@Bind(R.id.btn_ok)
	Button btnOk;
	@Bind(R.id.btn_cancel)
	Button btnCancel;

	private String strDiaryType;

	@Override
	public int getContentView() {
		// TODO Auto-generated method stub
		return R.layout.activity_diary;
	}

	@Override
	public void setToolBar(Toolbar mToolbar) {
		// TODO Auto-generated method stub
		Bundle bundle = getIntent().getExtras();
		strDiaryType = bundle.getString(DiaryConst.DIARY_TYPE);
		if (strDiaryType.equals(DiaryConst.TYPE_NEW)) {
			getToolBar().setTitle("新建日记");
		} else {
			getToolBar().setTitle(bundle.getString(DiaryConst.TITLE, ""));
		}
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
		btnOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (TextUtils.isEmpty(etxtTitle.getText().toString())
						|| TextUtils.isEmpty(etxtContent.getText().toString())) {
					new AlertDialog.Builder(DiaryActivity.this)
							.setMessage(R.string.alert_ok)
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											// TODO Auto-generated method stub
											saveDiary();
										}
									})
							.setNegativeButton("取消",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											// TODO Auto-generated method stub

										}
									}).create().show();
				} else {
					saveDiary();
				}

			}
		});
		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new AlertDialog.Builder(DiaryActivity.this)
						.setMessage(R.string.alert_cancel)
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										finish();
									}
								})
						.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub

									}
								}).create().show();
			}
		});
	}

	private void init() {
		// TODO Auto-generated method stub
		Bundle bundle = getIntent().getExtras();
		etxtTitle.setText(bundle.getString(DiaryConst.TITLE, ""));
		etxtContent.setText(bundle.getString(DiaryConst.CONTENT, ""));
	}

	protected void saveDiary() {
		// TODO Auto-generated method stub
		ContentValues values = new ContentValues();
		values.put(DiaryConst.TITLE, etxtTitle.getText().toString());
		values.put(DiaryConst.CONTENT, etxtContent.getText().toString());
		values.put(DiaryConst.DATE, System.currentTimeMillis());
		if (strDiaryType.equals(DiaryConst.TYPE_NEW)) {
			getContentResolver().insert(DiaryConst.CONTENT_URI, values);
		} else {
			getContentResolver().delete(
					ContentUris.withAppendedId(DiaryConst.CONTENT_URI, Long
							.parseLong(getIntent().getExtras().getString(DiaryConst.TID))),
					null, null);
			getContentResolver().insert(DiaryConst.CONTENT_URI,
					values);
		}
		finish();
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

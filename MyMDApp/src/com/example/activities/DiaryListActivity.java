package com.example.activities;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
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
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemLongClickListener;
import butterknife.Bind;
import butterknife.ButterKnife;

import com.example.mymdapp.R;
import com.example.util.Consts.DiaryConst;
import com.example.util.TimeUtil;
import com.mxk.baseapplication.LBaseActivity;

public class DiaryListActivity extends LBaseActivity implements LoaderCallbacks<Cursor>{

	@Bind(R.id.diary_list)
	ListView listDiary;
	@Bind(R.id.btn_create_new)
    Button btnCreate;

    MyCursorAdapter mAdapter;

	@Override
	public int getContentView() {
		// TODO Auto-generated method stub
		return R.layout.activity_diary_list;
	}

	@Override
	public void setToolBar(Toolbar mToolbar) {
		// TODO Auto-generated method stub
		mToolbar.setTitle("日记本");
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
		listDiary.setOnItemLongClickListener(new OnItemLongClickListener()
        {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                    int position, long id)
            {
                // TODO Auto-generated method stub
            	Intent intent = new Intent(DiaryListActivity.this, DiaryActivity.class);
            	Bundle bundle = new Bundle();
                Cursor cursor = (Cursor) parent.getAdapter().getItem(position);
                bundle.putString(DiaryConst.TID,
                    cursor.getString(cursor.getColumnIndex(DiaryConst.TID)));
                bundle.putString(DiaryConst.TITLE,
                    cursor.getString(cursor.getColumnIndex(DiaryConst.TITLE)));
                bundle.putString(DiaryConst.CONTENT,
                    cursor.getString(cursor.getColumnIndex(DiaryConst.CONTENT)));
                bundle.putString(DiaryConst.DIARY_TYPE, DiaryConst.TYPE_EDIT);
            	intent.putExtras(bundle);
                startActivity(intent);
                return true;
            }
        });
        btnCreate.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
            	Intent intent = new Intent(DiaryListActivity.this, DiaryActivity.class);
            	Bundle bundle = new Bundle();
            	bundle.putString(DiaryConst.DIARY_TYPE, DiaryConst.TYPE_NEW);
            	intent.putExtras(bundle);
                startActivity(intent);

            }
        });
	}

	private void init() {
		// TODO Auto-generated method stub
		mAdapter = new MyCursorAdapter(this, null);
        listDiary.setAdapter(mAdapter);
        getLoaderManager().initLoader(0, null, this);

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

	public class MyCursorAdapter extends CursorAdapter
    {
        public MyCursorAdapter(Context context, Cursor c)
        {
            super(context, c);
            // TODO Auto-generated constructor stub
        }

        public MyCursorAdapter(Context context, Cursor c, boolean autoRequery)
        {
            super(context, c, autoRequery);
            // TODO Auto-generated constructor stub
        }

        public MyCursorAdapter(Context context, Cursor c, int flags)
        {
            super(context, c, flags);
            // TODO Auto-generated constructor stub
        }

        @Override
        public View newView(Context context, final Cursor cursor, ViewGroup parent)
        {
            // TODO Auto-generated method stub
            View view = LayoutInflater.from(DiaryListActivity.this).inflate(
                R.layout.diary_listitem, null);
            ViewHolder holder = new ViewHolder();
            holder.txtTitle = (TextView) view.findViewById(R.id.title);
            holder.txtDate = (TextView) view.findViewById(R.id.date);
            holder.btnDelete = (Button) view.findViewById(R.id.delete);
            view.setTag(holder);
            bindView(view, context, cursor);
            return view;
        }

        @Override
        public void bindView(View view, Context context, final Cursor cursor)
        {
            // TODO Auto-generated method stub
            ViewHolder holder = (ViewHolder) view.getTag();
            String strTitle = cursor.getString(cursor.getColumnIndex(DiaryConst.TITLE));
            if (TextUtils.isEmpty(strTitle))
            {
                holder.txtTitle.setText("未命名");
            }
            else
            {
                holder.txtTitle.setText(strTitle);
            }
            Long lmilliseconds = cursor.getLong(cursor.getColumnIndex(DiaryConst.DATE));
            holder.txtDate.setText(TimeUtil.getDateFormat(lmilliseconds));
            final long lId = cursor.getLong(cursor.getColumnIndex(DiaryConst.TID));
            holder.btnDelete.setOnClickListener(new OnClickListener()
            {

                @Override
                public void onClick(View v)
                {
                    // TODO Auto-generated method stub
                    new AlertDialog.Builder(DiaryListActivity.this)
                            .setMessage(R.string.alert_delete)
                            .setPositiveButton("确定",
                                new DialogInterface.OnClickListener()
                                {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        // TODO Auto-generated method stub
                                    	DiaryListActivity.this.getContentResolver().delete(
                                            ContentUris.withAppendedId(
                                                DiaryConst.CONTENT_URI, lId),
                                            null, null);
                                    }
                                })
                            .setNegativeButton("取消",
                                new DialogInterface.OnClickListener()
                                {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        // TODO Auto-generated method stub

                                    }
                                }).create().show();
                }
            });
        }

    }

    public class ViewHolder
    {
        private TextView txtTitle;
        private TextView txtDate;
        private Button btnDelete;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args)
    {
        // TODO Auto-generated method stub
        return new CursorLoader(this, DiaryConst.CONTENT_URI, null, null, null,
            DiaryConst.ORDER_DATE_DESC);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data)
    {
        // TODO Auto-generated method stub
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {
        // TODO Auto-generated method stub
        mAdapter.swapCursor(null);
    }

}


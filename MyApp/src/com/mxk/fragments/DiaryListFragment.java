package com.mxk.fragments;


import com.mxk.myapp.MyBaseFragment;
import com.mxk.myapp.R;
import com.mxk.utils.Consts;
import com.mxk.utils.TimeUtil;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentUris;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
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


public class DiaryListFragment extends MyBaseFragment implements LoaderCallbacks<Cursor>
{
    private ListView listDiary;
    private Button btnCreate;
    MyCursorAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.diary_list_fragment, container, false);
        listDiary = (ListView) rootView.findViewById(R.id.diary_list);
        btnCreate = (Button) rootView.findViewById(R.id.btn_create_new);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        mAdapter = new MyCursorAdapter(getActivity(), null);
        listDiary.setAdapter(mAdapter);
        getLoaderManager().initLoader(0, null, this);
        listDiary.setOnItemLongClickListener(new OnItemLongClickListener()
        {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                    int position, long id)
            {
                // TODO Auto-generated method stub
                DiaryFragment diaryFragment = new DiaryFragment();
                Bundle bundle = new Bundle();
                Cursor cursor = (Cursor) parent.getAdapter().getItem(position);
                bundle.putString(Consts.TID,
                    cursor.getString(cursor.getColumnIndex(Consts.TID)));
                bundle.putString(Consts.TITLE,
                    cursor.getString(cursor.getColumnIndex(Consts.TITLE)));
                bundle.putString(Consts.CONTENT,
                    cursor.getString(cursor.getColumnIndex(Consts.CONTENT)));
                bundle.putString(Consts.DIARY_TYPE, Consts.TYPE_EDIT);
                diaryFragment.setArguments(bundle);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.hide(DiaryListFragment.this);
                ft.add(getId(), diaryFragment, "DiaryFragment");
                ft.addToBackStack(null);
                ft.commit();
                return false;
            }
        });
        btnCreate.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                DiaryFragment diaryFragment = new DiaryFragment();
                Bundle bundle = new Bundle();
                bundle.putString(Consts.DIARY_TYPE, Consts.TYPE_NEW);
                diaryFragment.setArguments(bundle);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.hide(DiaryListFragment.this);
                ft.add(getId(), diaryFragment, "DiaryFragment");
                ft.addToBackStack(null);
                ft.commit();
            }
        });

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args)
    {
        // TODO Auto-generated method stub
        return new CursorLoader(getActivity(), Consts.CONTENT_URI, null, null, null,
            Consts.ORDER_DATE_DESC);
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
            View view = LayoutInflater.from(getActivity()).inflate(
                R.layout.diary_listitem_fragment, null);
            ViewHolder holder = new ViewHolder();
            holder.txtTitle = (TextView) view.findViewById(R.id.title);
            holder.txtDate = (TextView) view.findViewById(R.id.date);
            holder.btnDelete = (Button) view.findViewById(R.id.delete);
            view.setTag(holder);
            String strTitle = cursor.getString(cursor.getColumnIndex(Consts.TITLE));
            if (TextUtils.isEmpty(strTitle))
            {
                holder.txtTitle.setText("未命名");
            }
            else
            {
                holder.txtTitle.setText(strTitle);
            }
            Long lmilliseconds = cursor.getLong(cursor.getColumnIndex(Consts.DATE));
            holder.txtDate.setText(TimeUtil.getDateFormat(lmilliseconds));
            final long lId = cursor.getLong(cursor.getColumnIndex(Consts.TID));
            holder.btnDelete.setOnClickListener(new OnClickListener()
            {

                @Override
                public void onClick(View v)
                {
                    // TODO Auto-generated method stub
                    new AlertDialog.Builder(getActivity())
                            .setMessage(R.string.alert_delete)
                            .setPositiveButton("确定",
                                new DialogInterface.OnClickListener()
                                {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        // TODO Auto-generated method stub
                                        getActivity().getContentResolver().delete(
                                            ContentUris.withAppendedId(
                                                Consts.CONTENT_URI, lId),
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
            return view;
        }

        @Override
        public void bindView(View view, Context context, final Cursor cursor)
        {
            // TODO Auto-generated method stub
            ViewHolder holder = (ViewHolder) view.getTag();
            String strTitle = cursor.getString(cursor.getColumnIndex(Consts.TITLE));
            if (TextUtils.isEmpty(strTitle))
            {
                holder.txtTitle.setText("未命名");
            }
            else
            {
                holder.txtTitle.setText(strTitle);
            }
            Long lmilliseconds = cursor.getLong(cursor.getColumnIndex(Consts.DATE));
            holder.txtDate.setText(TimeUtil.getDateFormat(lmilliseconds));
            final long lId = cursor.getLong(cursor.getColumnIndex(Consts.TID));
            holder.btnDelete.setOnClickListener(new OnClickListener()
            {

                @Override
                public void onClick(View v)
                {
                    // TODO Auto-generated method stub
                    new AlertDialog.Builder(getActivity())
                            .setMessage(R.string.alert_delete)
                            .setPositiveButton("确定",
                                new DialogInterface.OnClickListener()
                                {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        // TODO Auto-generated method stub
                                        getActivity().getContentResolver().delete(
                                            ContentUris.withAppendedId(
                                                Consts.CONTENT_URI, lId),
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

}
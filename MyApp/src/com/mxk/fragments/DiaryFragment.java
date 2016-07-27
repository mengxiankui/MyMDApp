package com.mxk.fragments;


import com.mxk.myapp.MyBaseFragment;
import com.mxk.myapp.R;
import com.mxk.utils.Consts;

import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


public class DiaryFragment extends MyBaseFragment
{
    private EditText etxtTitle, etxtContent;
    private Button btnOk, btnCancel;

    private String strDiaryType;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        strDiaryType = getArguments().getString(Consts.DIARY_TYPE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.diary_fragment, container, false);
        etxtTitle = (EditText) rootView.findViewById(R.id.edittext_title);
        etxtContent = (EditText) rootView.findViewById(R.id.edittext_content);
        btnOk = (Button) rootView.findViewById(R.id.btn_ok);
        btnCancel = (Button) rootView.findViewById(R.id.btn_cancel);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        etxtTitle.setText(getArguments().getString(Consts.TITLE, ""));
        etxtContent.setText(getArguments().getString(Consts.CONTENT, ""));
        btnOk.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                if (TextUtils.isEmpty(etxtTitle.getText().toString())
                    || TextUtils.isEmpty(etxtContent.getText().toString()))
                {
                    new AlertDialog.Builder(getActivity())
                            .setMessage(R.string.alert_ok)
                            .setPositiveButton("确定",
                                new DialogInterface.OnClickListener()
                                {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        // TODO Auto-generated method stub
                                        saveDiary();
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
                else
                {
                    saveDiary();
                }

            }
        });
        btnCancel.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                new AlertDialog.Builder(getActivity()).setMessage(R.string.alert_cancel)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener()
                        {

                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                // TODO Auto-generated method stub
                                //                                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                //                                transaction.remove(DiaryFragment.this);
                                //                                transaction.commit();
                                getFragmentManager().popBackStack();
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener()
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

    protected void saveDiary()
    {
        // TODO Auto-generated method stub
        ContentValues values = new ContentValues();
        values.put(Consts.TITLE, etxtTitle.getText().toString());
        values.put(Consts.CONTENT, etxtContent.getText().toString());
        values.put(Consts.DATE, System.currentTimeMillis());
        if (strDiaryType.equals(Consts.TYPE_NEW))
        {
            getActivity().getContentResolver().insert(Consts.CONTENT_URI, values);
        }
        else
        {
            getActivity().getContentResolver().delete(
                ContentUris.withAppendedId(Consts.CONTENT_URI,
                    Long.parseLong(getArguments().getString(Consts.TID))), null, null);
            getActivity().getContentResolver().insert(Consts.CONTENT_URI, values);
        }
        getFragmentManager().popBackStack();
    }
    
    

}
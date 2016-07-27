package com.mxk.fragments;


import com.mxk.myapp.MyBaseFragment;
import com.mxk.myapp.R;

import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class AboutFragment extends MyBaseFragment
{
    private TextView txtCopyRight;
    private RelativeLayout layout;
    private Button button;

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
        View rootView = inflater.inflate(R.layout.about_fragment, container, false);
        //        txtCopyRight = (TextView) rootView.findViewById(R.id.txt_copy_right);
        layout = (RelativeLayout) rootView.findViewById(R.id.layout);
        button = (Button) rootView.findViewById(R.id.btn);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        button.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                Toast.makeText(
                    getActivity(),
                    "layout width = "
                        + layout.getWidth()
                        + ", layout height = "
                        + layout.getHeight()
                        + ", idel width = "
                        + (getView().getWidth() - getView().getPaddingRight()
                            - getView().getPaddingLeft()) + ", idel height = " + (getView()
                                .getHeight() - getView().getPaddingBottom() - getView()
                                .getPaddingTop()), Toast.LENGTH_SHORT).show();
            }
        });
        ViewTreeObserver viewTreeObserver = getView().getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new OnGlobalLayoutListener()
        {
            @Override
            public void onGlobalLayout()
            {
                getView().getViewTreeObserver().removeGlobalOnLayoutListener(this);
                //最后要刷新一下界面
                layout.layout(layout.getLeft(), layout.getTop(),
                    getView().getWidth() - getView().getPaddingRight()
                        - getView().getPaddingLeft() + layout.getLeft(), getView()
                            .getHeight()
                        - getView().getPaddingBottom()
                        - getView().getPaddingTop() + layout.getTop());
                layout.invalidate();
            }
        });
        getImieStatus();

    }

    private void getImieStatus()
    {
        TelephonyManager tm = (TelephonyManager) getActivity().getSystemService(
            Context.TELEPHONY_SERVICE);
        String deviceId = tm.getDeviceId();
        Log.e("DEVICE_ID ", deviceId + " ");
    }

    @Override
    public void onStart()
    {
        // TODO Auto-generated method stub
        super.onStart();
        //        layout.postDelayed(new Runnable()
        //        {
        //
        //            @Override
        //            public void run()
        //            {
        //                // TODO Auto-generated method stub
        //                layout.layout(getView().getPaddingLeft(), getView().getPaddingTop(),
        //                    getView().getWidth() - getView().getPaddingRight(), getView()
        //                            .getHeight() - getView().getPaddingBottom());
        //                //                layout.getLayoutParams().height = getView().getHeight();
        //            }
        //        }, 2000);

    }

    //    private void getAndroidId(){  
    //      String androidId = System.getString(getActivity().getContentResolver(), System.ANDROID_ID);  
    //      Log.e("ANDROID_ID", androidId + " ");
    //    }
}
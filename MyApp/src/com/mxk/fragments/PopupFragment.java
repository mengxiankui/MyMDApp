package com.mxk.fragments;


import com.mxk.myapp.MyBaseFragment;
import com.mxk.myapp.R;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class PopupFragment extends MyBaseFragment
{
    private Button button1, button2, button3, button4, button5, button6, button7,
            button8, button9;
    private MyOnClickListener clickListener;
    private MyPopupWindow myPopupWindow;
    private boolean bswitch= true;

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
        View rootView = inflater.inflate(R.layout.popup_fragment, container, false);
        //        txtCopyRight = (TextView) rootView.findViewById(R.id.txt_copy_right);
        button1 = (Button) rootView.findViewById(R.id.btn1);
        button2 = (Button) rootView.findViewById(R.id.btn2);
        button3 = (Button) rootView.findViewById(R.id.btn3);
        button4 = (Button) rootView.findViewById(R.id.btn4);
        button5 = (Button) rootView.findViewById(R.id.btn5);
        button6 = (Button) rootView.findViewById(R.id.btn6);
        button7 = (Button) rootView.findViewById(R.id.btn7);
        button8 = (Button) rootView.findViewById(R.id.btn8);
        button9 = (Button) rootView.findViewById(R.id.btn9);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        clickListener = new MyOnClickListener();
        button1.setOnClickListener(clickListener);
        button2.setOnClickListener(clickListener);
        button3.setOnClickListener(clickListener);
        button4.setOnClickListener(clickListener);
        button5.setOnClickListener(clickListener);
        button6.setOnClickListener(clickListener);
        button7.setOnClickListener(clickListener);
        button8.setOnClickListener(clickListener);
        button9.setOnClickListener(clickListener);
    }

    private class MyOnClickListener implements OnClickListener
    {

        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub
            if (null == myPopupWindow)
            {
                myPopupWindow = new MyPopupWindow();

            }
            myPopupWindow.show(v);
            bswitch = !bswitch;
        }

    }

    class MyPopupWindow extends PopupWindow
    {
        private View conentView;

        public MyPopupWindow()
        {
            super();
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
            conentView = inflater.inflate(R.layout.popupwindow, null);
            setContentView(conentView);
            setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
            setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            setBackgroundDrawable(new BitmapDrawable());
            setOutsideTouchable(true);
            setFocusable(true);
            // TODO Auto-generated constructor stub
        }

        public void show(View v)
        {
            if (bswitch)
            {
                showAsDropDown(v);
            }
            else
            {
                showAsDropDown(v, -conentView.getWidth()/2+v.getWidth()/2, 0);
            }
        }

    }
}
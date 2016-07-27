package com.mxk.fragments;


import com.mxk.myapp.MyBaseFragment;
import com.mxk.myapp.R;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class TuLingLauncherFragment extends MyBaseFragment
{
    private EditText editText;
    private Button btnHanzi, btnMeizi;

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
        View rootView = inflater.inflate(R.layout.tuling_launcher_fragment, container,
            false);
        editText = (EditText) rootView.findViewById(R.id.edit);
        btnHanzi = (Button) rootView.findViewById(R.id.btn_hanzi);
        btnMeizi = (Button) rootView.findViewById(R.id.btn_meizi);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        btnHanzi.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                if (editText.getText().toString().equals("19870114"))
                {
                    TuLingChatFragment chatFragment = new TuLingChatFragment();
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("IsHanZi", true);
                    chatFragment.setArguments(bundle);
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().hide(TuLingLauncherFragment.this)
                            .add(R.id.container, chatFragment, "TuLingChatFragment")
                            .addToBackStack(null).commit();
                    editText.setText("");
                    InputMethodManager inputManager = (InputMethodManager) editText
                            .getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

                    inputManager.hideSoftInputFromWindow(editText.getApplicationWindowToken(), 0);
                }
                else
                {
                    showAlertDialog();

                }

            }
        });
        btnMeizi.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                if (editText.getText().toString().equals("19880308"))
                {
                    TuLingChatFragment chatFragment = new TuLingChatFragment();
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("IsHanZi", false);
                    chatFragment.setArguments(bundle);
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().hide(TuLingLauncherFragment.this)
                            .add(R.id.container, chatFragment, "TuLingChatFragment")
                            .addToBackStack(null).commit();
                    editText.setText("");
                    InputMethodManager inputManager = (InputMethodManager) editText
                            .getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

                    inputManager.hideSoftInputFromWindow(editText.getApplicationWindowToken(), 0);
                }
                else
                {
                    showAlertDialog();

                }
            }
        });

    }

    protected void showAlertDialog()
    {
        // TODO Auto-generated method stub
        new AlertDialog.Builder(getActivity()).setMessage(R.string.alert_wrong_birthday)
                .setPositiveButton("确定", new DialogInterface.OnClickListener()
                {

                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        // TODO Auto-generated method stub

                    }
                }).create().show();
    }

}
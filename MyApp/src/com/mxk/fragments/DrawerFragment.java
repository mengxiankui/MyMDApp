package com.mxk.fragments;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.mxk.drawerview.DrawerView;
import com.mxk.myapp.MyBaseFragment;
import com.mxk.myapp.R;
import com.mxk.utils.TimeUtil;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class DrawerFragment extends MyBaseFragment
{
    private DrawerView imgPaint;
    private Button btnSave, btnSetColor;
    private EditText editText;
    private TextView textView;

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
        View rootView = inflater.inflate(R.layout.drawer_fragment, container, false);
        imgPaint = (DrawerView) rootView.findViewById(R.id.img_paint);
        btnSave = (Button) rootView.findViewById(R.id.btn_save);
        editText = (EditText) rootView.findViewById(R.id.etxt_input_color);
        btnSetColor = (Button) rootView.findViewById(R.id.btn_setcolor);
        textView = (TextView) rootView.findViewById(R.id.current_color);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        setCurrentColor();
        setOnClickListener();

    }

    private void setCurrentColor()
    {
        // TODO Auto-generated method stub
        SharedPreferences sp = getActivity().getSharedPreferences("mxk_Preferences", 0);
        int color = sp.getInt("Paint_Color", Color.RED);
        textView.setText("R:" + Color.red(color) + " G:" + Color.green(color) + " B:"
            + Color.blue(color));
        textView.setTextColor(color);
    }

    private void setOnClickListener()
    {
        // TODO Auto-generated method stub
        btnSave.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                savePaint();
                Toast.makeText(getActivity(), "Paint Saved!", Toast.LENGTH_SHORT).show();
            }
        });
        btnSetColor.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub

                String strColor = editText.getText().toString();
                if (strColor.length() != 6 && strColor.length() != 8)
                {
                    return;
                }
                if (!strColor.startsWith("#"))
                {
                    strColor = "#" + strColor;
                }
                int color = Color.parseColor(strColor);
                imgPaint.setPaintColor(color);
                SharedPreferences sp = getActivity().getSharedPreferences(
                    "mxk_Preferences", 0);
                sp.edit().putInt("Paint_Color", color).commit();
                textView.setText("R:" + Color.red(color) + " G:" + Color.green(color)
                    + " B:" + Color.blue(color));
                textView.setTextColor(color);

            }
        });
    }

    private void savePaint()
    {
        File file = new File(Environment.getExternalStorageDirectory(),
            TimeUtil.getDrawerPaintFormat() + ".png");
        if (file.exists())
        {
            file.delete();
        }
        try
        {
            file.createNewFile();
            FileOutputStream out = new FileOutputStream(file);
            imgPaint.getBitMap().compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
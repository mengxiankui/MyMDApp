package com.example.activities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;

import com.example.mymdapp.R;
import com.example.util.TimeUtil;
import com.mxk.baseapplication.LBaseActivity;
import com.mxk.drawerview.DrawerView;

public class DrawerActivity extends LBaseActivity {

	@Bind(R.id.img_paint)
	DrawerView imgPaint;
	@Bind(R.id.btn_save)
    Button btnSave;
	@Bind(R.id.btn_setcolor)
    Button btnSetColor;
	@Bind(R.id.etxt_input_color)
    EditText editText;
	@Bind(R.id.current_color)
    TextView textView;

	@Override
	public int getContentView() {
		// TODO Auto-generated method stub
		return R.layout.activity_drawer;
	}

	@Override
	public void setToolBar(Toolbar mToolbar) {
		// TODO Auto-generated method stub
		mToolbar.setTitle("画板");
	}

	@Override
	public void onAfterOnCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		ButterKnife.bind(this);
		setupToolBar();
		setCurrentColor();
		initAction();
		
	}
	
	private void initAction() {
		// TODO Auto-generated method stub
		btnSave.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                savePaint();
                Toast.makeText(DrawerActivity.this, "Paint Saved!", Toast.LENGTH_SHORT).show();
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
                SharedPreferences sp = getSharedPreferences(
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
            TimeUtil.getStringDateFormat() + ".png");
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

	private void setCurrentColor()
    {
        // TODO Auto-generated method stub
        SharedPreferences sp = getSharedPreferences("mxk_Preferences", 0);
        int color = sp.getInt("Paint_Color", Color.RED);
        textView.setText("R:" + Color.red(color) + " G:" + Color.green(color) + " B:"
            + Color.blue(color));
        textView.setTextColor(color);
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


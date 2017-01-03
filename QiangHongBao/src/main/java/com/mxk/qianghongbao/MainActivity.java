package com.mxk.qianghongbao;

import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.mxk.qianghongbao.helper.QHBHelper;
import com.mxk.qianghongbao.utils.Consts;
import com.mxk.qianghongbao.utils.TimeUtil;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    Button btnMode;
    Button btnOpenService;
    Button btnShowDetail;

    TextView txtHongBaoNum;
    TextView txtMoneySum;
    ScrollView scrollView;
    LinearLayout scrollLayout;

    private String lLastTime = "0";

    private float sum = 0.0f;
    private int num = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindViews();
        bingListeners();
        getData();
    }

    private void bindViews() {
        btnMode = (Button) findViewById(R.id.mode);
        btnOpenService = (Button) findViewById(R.id.openservice);
        btnShowDetail = (Button) findViewById(R.id.detail);

        txtHongBaoNum = (TextView) findViewById(R.id.hongbaonum);
        txtMoneySum = (TextView) findViewById(R.id.moneysum);
        scrollView = (ScrollView) findViewById(R.id.detailScroll);
        scrollLayout = (LinearLayout) findViewById(R.id.detailScrollLayout);
    }

    private void bingListeners() {

        btnMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                QHBHelper.changeMode();
                if (QHBHelper.getMode()) {
                    btnMode.setText("抢红包模式");
                } else {
                    btnMode.setText("普通模式");
                }
            }
        });
        btnOpenService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAccessibilityServiceSettings();
            }
        });

        btnShowDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (scrollView.getVisibility() == View.GONE) {
                    btnShowDetail.setText("隐藏明细");
                    scrollView.setVisibility(View.VISIBLE);
                    updateQHBData();
                } else {
                    btnShowDetail.setText("显示明细");
                    scrollView.setVisibility(View.GONE);
                }

            }
        });
    }

    private void getData() {
//        getMoneySum();
    }

    ContentObserver contentObserver = new ContentObserver(null) {
        @Override
        public void onChange(boolean selfChange) {
            getMoneySum();
            if (scrollView.getVisibility() == View.VISIBLE)
            {
                updateQHBData();
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        getMoneySum();
        if (scrollView.getVisibility() == View.VISIBLE)
        {
            updateQHBData();
        }
        getContentResolver().registerContentObserver(Consts.WeixinQHBConst.CONTENT_URI, false, contentObserver);
    }

    @Override
    protected void onPause() {
        super.onPause();
        getContentResolver().unregisterContentObserver(contentObserver);
    }

    private synchronized void updateQHBData() {
        Log.d(TAG, "updateQHBData !");
        if (scrollView.getVisibility() == View.VISIBLE) {
            boolean needupdate = false;
            Cursor cursor = getContentResolver().query(Consts.WeixinQHBConst.CONTENT_URI, null, Consts.WeixinQHBConst.DATE + " > ? ", new String[]{lLastTime}, Consts.WeixinQHBConst.DATE + " desc");
            if (null == cursor)
            {
                return;
            }
            Log.d(TAG, "cursor.getCount() = " + cursor.getCount());
            while (cursor.moveToNext()) {
                TextView textView = new TextView(MainActivity.this);
                textView.setTextAppearance(MainActivity.this, R.style.TextAppearance_Body1_Inverse);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(5, 2, 5, 2);
                textView.setLayoutParams(layoutParams);
                float m = cursor.getFloat(cursor.getColumnIndex(Consts.WeixinQHBConst.MONEY));
                String money = String.valueOf(m);
                String from = cursor.getString(cursor.getColumnIndex(Consts.WeixinQHBConst.NAME));
                String date = cursor.getString(cursor.getColumnIndex(Consts.WeixinQHBConst.DATE));

                textView.setText(money + " " + from + " " + date);
                scrollLayout.addView(textView);
            }
            cursor.close();
            lLastTime = TimeUtil.getDBDateFormat();
        }

    }

    /**
     * 打开辅助服务的设置
     */
    private void openAccessibilityServiceSettings() {
        try {
            Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            Toast.makeText(this, "打开辅助服务设置", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getMoneySum() {
        Log.d(TAG, "getMoneySum begin !");
        Cursor cursor = getContentResolver().query(Consts.WeixinQHBConst.CONTENT_URI, new String[]{"sum("+Consts.WeixinQHBConst.MONEY+")"}, null, null, null);
        if (null == cursor)
        {
            return;
        }
        if (cursor.moveToNext())
        {
            sum = cursor.getFloat(cursor.getColumnIndex("sum("+Consts.WeixinQHBConst.MONEY+")"));
        }

        cursor.close();
        cursor = getContentResolver().query(Consts.WeixinQHBConst.CONTENT_URI, new String[]{"count(*)"}, null, null, null);
        if (null == cursor)
        {
            return;
        }
        if (cursor.moveToNext())
        {
            num =  cursor.getInt(cursor.getColumnIndex("count(*)"));
        }
        cursor.close();
        Log.d(TAG, "getMoneySum, sum = " + sum + ", num = "+ num);
        txtHongBaoNum.setText(String.valueOf(num));
        txtMoneySum.setText(String.valueOf(sum));

    }
}

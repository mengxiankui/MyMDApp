package com.example.mymdapp;

import android.app.Activity;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adapter.SlideMenuAdapter;
import com.example.util.Consts;
import com.example.util.MemuActivityManager;
import com.example.util.TimeUtil;
import com.mxk.baseapplication.LBaseActivity;
import com.mxk.baseframe.util.log.Logger;
import com.mxk.baseframe.util.toast.frenchtoast.FrenchToast;
import com.qianghongbao.helper.QHBHelper;
import com.tencent.bugly.crashreport.CrashReport;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends LBaseActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Bind(R.id.drawer)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.recyclerview)
    RecyclerView mRecyclerView;

    @Bind(R.id.mode)
    Button btnMode;
    @Bind(R.id.openservice)
    Button btnOpenService;
    @Bind(R.id.detail)
    Button btnShowDetail;
    @Bind(R.id.testCrash)
    Button btnTestCrash;

    @Bind(R.id.hongbaonum)
    TextView txtHongBaoNum;
    @Bind(R.id.moneysum)
    TextView txtMoneySum;
    @Bind(R.id.detailScroll)
    ScrollView scrollView;
    @Bind(R.id.detailScrollLayout)
    LinearLayout scrollLayout;


    private ActionBarDrawerToggle mDrawerToggle;
    private SlideMenuAdapter adapter;
    private OnItemClickListener itemClickListener;

    private float sum = 0.0f;
    private int num = 0;

    ContentObserver contentObserver = new ContentObserver(null) {
        @Override
        public void onChange(boolean selfChange) {
            getMoneySum();
            updateQHBData();
        }
    };

    private String lLastTime = "0";

    @Override
    public int getContentView() {
        // TODO Auto-generated method stub
        return R.layout.activity_main_dr;
    }

    @Override
    public void onAfterOnCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        ButterKnife.bind(this);
        setupToolBar();
        setupDrawer();
        init();
        setClickListeners();

    }

    private void init() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getMoneySum();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateQHBData();
        getContentResolver().registerContentObserver(Consts.WeixinQHBConst.CONTENT_URI, false, contentObserver);
    }

    @Override
    protected void onPause() {
        super.onPause();
        getContentResolver().unregisterContentObserver(contentObserver);
    }

    private synchronized void updateQHBData() {
        Logger.d(LOG_TAG, "updateQHBData !");
        if (scrollView.getVisibility() == View.VISIBLE) {
            boolean needupdate = false;
            Cursor cursor = getContentResolver().query(Consts.WeixinQHBConst.CONTENT_URI, null, Consts.WeixinQHBConst.DATE + " > ? ", new String[]{lLastTime}, Consts.WeixinQHBConst.DATE + " desc");
            Logger.d(LOG_TAG, "cursor.getCount() = " + cursor.getCount());
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

    private void setClickListeners() {
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
        btnTestCrash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CrashReport.testJavaCrash();
            }
        });
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

    private void setupToolBar() {
        // TODO Auto-generated method stub
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, getToolBar(), R.string.drawer_open, R.string.drawer_close);
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);

    }

    private void setupDrawer() {
        // TODO Auto-generated method stub
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
                GravityCompat.START);

        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        adapter = new SlideMenuAdapter(getResources().getStringArray(
                R.array.menu_name));
        mRecyclerView.setAdapter(adapter);

        itemClickListener = new OnItemClickListener();
        adapter.setOnItemClickListener(itemClickListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setToolBar(Toolbar mToolbar) {
        // TODO Auto-generated method stub
        mToolbar.setTitle(R.string.app_name);
    }

    public void getMoneySum() {
        Logger.d(LOG_TAG, "getMoneySum begin !");
        Cursor cursor = getContentResolver().query(Consts.WeixinQHBConst.CONTENT_URI, new String[]{"sum("+Consts.WeixinQHBConst.MONEY+")"}, null, null, null);

        if (cursor.moveToNext())
        {
            sum = cursor.getFloat(cursor.getColumnIndex("sum("+Consts.WeixinQHBConst.MONEY+")"));
        }

        cursor.close();
        cursor = getContentResolver().query(Consts.WeixinQHBConst.CONTENT_URI, new String[]{"count(*)"}, null, null, null);

        if (cursor.moveToNext())
        {
            num =  cursor.getInt(cursor.getColumnIndex("count(*)"));
        }
        cursor.close();
        Logger.d(LOG_TAG, "getMoneySum, sum = " + sum + ", num = "+ num);
        txtHongBaoNum.setText(String.valueOf(num));
        txtMoneySum.setText(String.valueOf(sum));

    }

    private class OnItemClickListener
            implements
            com.mxk.baseframe.baseadapter.BaseRecylerViewAdapter.OnItemClickListener {

        @Override
        public void onItemClick(View v, int position) {
            // TODO Auto-generated method stub
            FrenchToast.with(MainActivity.this).shortLength()
                    .showText(adapter.getData(position));
            Activity a = MemuActivityManager.getActivityAtPosition(position, MainActivity.this);
            if (null != a) {
                Intent i = new Intent(MainActivity.this, a.getClass());
                startActivity(i);
            }
        }

    }

}

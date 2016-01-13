package com.mxk.baseapplication;

import com.mxk.baseframe.R;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;

public abstract class LBaseActivity extends AppCompatActivity {
	/** 模块的名字 */
	private String moduleName = "";
	private Toolbar mToolbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(getContentView());
		initActivity();
		onAfterOnCreate(savedInstanceState);

	}

	@LayoutRes
	public abstract int getContentView();

//	@IdRes
//	public abstract int getRootViewId();

	private void initActivity() {
		setFlags();
		addToolBar();
		// 初始化模块名
		getModuleName();
	}

	private void setFlags() {
		// TODO Auto-generated method stub
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            // Translucent status bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
	}

	public abstract void setToolBar(Toolbar mToolbar);
	
	public Toolbar getToolBar()
	{
		return mToolbar;
	}

	private void addToolBar() {
		// TODO Auto-generated method stub
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		if (null != mToolbar) {
			setToolBar(mToolbar);
			setSupportActionBar(mToolbar);
			
		}

	}

	/**
	 * 获取模块的名字
	 */
	public String getModuleName() {
		String moduleName = this.moduleName;
		if (moduleName == null || moduleName.equalsIgnoreCase("")) {
			moduleName = getClass().getName().substring(0,
					getClass().getName().length() - 8);
			String arrays[] = moduleName.split("\\.");
			this.moduleName = moduleName = arrays[arrays.length - 1]
					.toLowerCase();
		}
		return moduleName;
	}

	public abstract void onAfterOnCreate(Bundle savedInstanceState);

	public LBaseApplication getLApplication() {
		return (LBaseApplication) getApplication();
	}

//	@SuppressLint("NewApi")
//	protected void setStatusBarColor(int color, boolean bNeedBurn) {
//
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//			Window window = getWindow();
//			// 很明显，这两货是新API才有的。
//			if (bNeedBurn) {
//				window.setStatusBarColor(colorBurn(color));
//				window.setNavigationBarColor(colorBurn(color));
//			} else {
//				window.setStatusBarColor(color);
//				window.setNavigationBarColor(color);
//			}
//
//		}
//		// 设定状态栏的颜色，当版本大于4.4时起作用
//		else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//
//			int statusBarHeight = ScreenUtil.getStatusBarHeight(this
//					.getBaseContext());
//
//			if (null == findViewById(getRootViewId())) {
//				return;
//			}
////			getToolBar().setPadding(0, statusBarHeight, 0, 0);
//			findViewById(getRootViewId()).setPadding(0, statusBarHeight, 0, 0);
//
//			SystemBarTintManager tintManager = new SystemBarTintManager(this);
//			tintManager.setStatusBarTintEnabled(true);
//			// 此处可以重新指定状态栏颜色
//			if (bNeedBurn) {
//				tintManager.setStatusBarTintColor(colorBurn(color));
//			} else {
//				tintManager.setStatusBarTintColor(color);
//			}
//		}
//
//	}

	/**
	 * 颜色加深处理
	 * 
	 * @param RGBValues
	 *            RGB的值，由alpha（透明度）、red（红）、green（绿）、blue（蓝）构成，
	 *            Android中我们一般使用它的16进制，
	 *            例如："#FFAABBCC",最左边到最右每两个字母就是代表alpha（透明度）、
	 *            red（红）、green（绿）、blue（蓝）。每种颜色值占一个字节(8位)，值域0~255
	 *            所以下面使用移位的方法可以得到每种颜色的值，然后每种颜色值减小一下，在合成RGB颜色，颜色就会看起来深一些了
	 * @return
	 */
//	private int colorBurn(int RGBValues) {
//		int alpha = RGBValues >> 24;
//		int red = RGBValues >> 16 & 0xFF;
//		int green = RGBValues >> 8 & 0xFF;
//		int blue = RGBValues & 0xFF;
//		red = (int) Math.floor(red * (1 - 0.1));
//		green = (int) Math.floor(green * (1 - 0.1));
//		blue = (int) Math.floor(blue * (1 - 0.1));
//		return Color.argb(alpha, red, green, blue);
//	}

}
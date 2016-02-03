/*
 * Copyright (C) 2013  WhiteCat 白猫 (www.thinkandroid.cn)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mxk.baseapplication;


import java.lang.Thread.UncaughtExceptionHandler;

import android.app.Application;
import android.content.Context;

import com.android.volley.toolbox.ImageLoader;
import com.mxk.baseframe.exception.LAppException;
import com.mxk.baseframe.util.config.PreferenceConfigUtil;
import com.mxk.baseframe.util.config.PropertiesConfigUtil;
import com.mxk.baseframe.util.http.RequestManager;
import com.mxk.baseframe.util.http.cache.LruImageCache;
import com.mxk.baseframe.util.log.Logger;
import com.mxk.baseframe.util.log.LoggerConfig;
import com.mxk.baseframe.util.log.LoggerConfig.LogType;
import com.mxk.baseframe.util.netstate.LNetChangeObserver;
import com.mxk.baseframe.util.netstate.LNetworkStateReceiver;
import com.mxk.baseframe.util.netstate.LNetWorkUtil.netType;
import com.mxk.baseframe.util.toast.frenchtoast.FrenchToast;


public class LBaseApplication extends Application
{
	/**
	 * Global application context.
	 */
	private static Context sContext;
	/**
	 * 	图片加载器
	 */
	private ImageLoader imageLoader;
    /** App异常崩溃处理器 */
    private UncaughtExceptionHandler uncaughtExceptionHandler;
    private Boolean networkAvailable = false;
    private LNetChangeObserver mNetChangeObserver;

    
    
    public LBaseApplication() {
		super();
		sContext = this;
	}

    /**
	 * Get the global application context.
	 * 
	 * @return Application context.
	 * @throws org.litepal.exceptions.GlobalException
	 */
	public static Context getContext() {
		if (sContext == null) {
			throw new RuntimeException("APPLICATION_CONTEXT_IS_NULL");
		}
		return sContext;
	}
	
	@Override
    public void onCreate()
    {
        // TODO Auto-generated method stub
        onPreCreateApplication();
        super.onCreate();
        onCreateApplication();
        onAfterCreateApplication();
    }

    protected void onPreCreateApplication()
    {
        // TODO Auto-generated method stub

    }

    private void onCreateApplication()
    {
        // TODO Auto-generated method stub
    	//设置日志类型
    	Logger.setLogType(LogType.LOGCAT);
    	//设置日志级别
    	Logger.setLogLevel(LoggerConfig.DEBUG);
        //初始化配置信息
    	PropertiesConfigUtil.getPropertiesConfig(this).loadConfig();
    	PreferenceConfigUtil.getPreferenceConfig(this).loadConfig();
    	//初始化网络请求管理类
    	RequestManager.getInstance().init(this);
    	//初始化图片加载器
    	imageLoader = new ImageLoader(RequestManager.getInstance().getRequestQueue(), new LruImageCache(this));
    	//初始化Toast
    	FrenchToast.install(this);
    	// 注册App异常崩溃处理器
        Thread.setDefaultUncaughtExceptionHandler(getUncaughtExceptionHandler());
        //初始化网络监听
        mNetChangeObserver = new LNetChangeObserver()
        {
            @Override
            public void onConnect(netType type)
            {
                // TODO Auto-generated method stub
                super.onConnect(type);
                onConnect(type);
            }

            @Override
            public void onDisConnect()
            {
                // TODO Auto-generated method stub
                super.onDisConnect();
                onDisConnect();

            }
        };
        LNetworkStateReceiver.registerObserver(mNetChangeObserver);
    }

    protected void onAfterCreateApplication()
    {
        // TODO Auto-generated method stub
    }
    
    public ImageLoader getImageLoader()
    {
    	return imageLoader;
    }
    

	/**
     * 当前没有网络连接
     */
    public void onDisConnect()
    {
        networkAvailable = false;
        // TODO
    }

    /**
     * 网络连接连接时调用
     */
    protected void onConnect(netType type)
    {
        // TODO Auto-generated method stub
        networkAvailable = true;
     // TODO
    }
    
    /**
     * 设置 App异常崩溃处理器
     * 
     * @param uncaughtExceptionHandler
     */
    public void setUncaughtExceptionHandler(
            UncaughtExceptionHandler uncaughtExceptionHandler)
    {
        this.uncaughtExceptionHandler = uncaughtExceptionHandler;
    }

    private UncaughtExceptionHandler getUncaughtExceptionHandler()
    {
        if (uncaughtExceptionHandler == null)
        {
            uncaughtExceptionHandler = LAppException.getInstance(this);
        }
        return uncaughtExceptionHandler;
    }

    /**
     * 获取当前网络状态，true为网络连接成功，否则网络连接失败
     * 
     * @return
     */
    public Boolean isNetworkAvailable()
    {
        return networkAvailable;
    }

}

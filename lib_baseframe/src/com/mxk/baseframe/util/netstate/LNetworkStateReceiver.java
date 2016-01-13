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
package com.mxk.baseframe.util.netstate;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.mxk.baseframe.util.log.Logger;
import com.mxk.baseframe.util.netstate.LNetWorkUtil.netType;

/**
 * @Title NetworkStateReceiver
 * @Package com.ta.util.netstate
 * @Description 是一个检测网络状态改变的，需要配置 <receiver
 *              android:name="com.mxk.baseframe.util.netstate.LNetworkStateReceiver" >
 *              <intent-filter> <action
 *              android:name="android.net.conn.CONNECTIVITY_CHANGE"  />
 *              </intent-filter> </receiver>
 * 
 *              需要开启权限 <uses-permission
 *              android:name="android.permission.CHANGE_NETWORK_STATE" />
 *              <uses-permission
 *              android:name="android.permission.CHANGE_WIFI_STATE" />
 *              <uses-permission
 *              android:name="android.permission.ACCESS_NETWORK_STATE" />
 *              <uses-permission
 *              android:name="android.permission.ACCESS_WIFI_STATE" />
 * @author 白猫
 * @date 2013-5-5 下午 22:47
 * @version V1.2
 */
public class LNetworkStateReceiver extends BroadcastReceiver
{
	private static Boolean networkAvailable = false;
	private static netType netType;
	private static ArrayList<LNetChangeObserver> netChangeObserverArrayList = new ArrayList<LNetChangeObserver>();
	private final static String ANDROID_NET_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
	private static BroadcastReceiver receiver;

	private static BroadcastReceiver getReceiver()
	{
		if (receiver == null)
		{
			receiver = new LNetworkStateReceiver();
		}
		return receiver;
	}

	@Override
	public void onReceive(Context context, Intent intent)
	{
		receiver = LNetworkStateReceiver.this;
		if (intent.getAction().equalsIgnoreCase(ANDROID_NET_CHANGE_ACTION))
		{
			Logger.i(LNetworkStateReceiver.this, "网络状态改变.");
			if (!LNetWorkUtil.isNetworkAvailable(context))
			{
				Logger.i(LNetworkStateReceiver.this, "没有网络连接.");
				networkAvailable = false;
			} else
			{
				Logger.i(LNetworkStateReceiver.this, "网络连接成功.");
				netType = LNetWorkUtil.getAPNType(context);
				networkAvailable = true;
			}
			notifyObserver();
		}
	}

	/**
	 * 注册网络状态广播
	 * 
	 * @param mContext
	 */
	public static void registerNetworkStateReceiver(Context mContext)
	{
		IntentFilter filter = new IntentFilter();
		filter.addAction(ANDROID_NET_CHANGE_ACTION);
		mContext.getApplicationContext()
				.registerReceiver(getReceiver(), filter);
	}

	/**
	 * 注销网络状态广播
	 * 
	 * @param mContext
	 */
	public static void unRegisterNetworkStateReceiver(Context mContext)
	{
		if (receiver != null)
		{
			try
			{
				mContext.getApplicationContext().unregisterReceiver(receiver);
			} catch (Exception e)
			{
				// TODO: handle exception
				Logger.d("TANetworkStateReceiver", e.getMessage());
			}
		}

	}

	/**
	 * 获取当前网络状态，true为网络连接成功，否则网络连接失败
	 * 
	 * @return
	 */
	public static Boolean isNetworkAvailable()
	{
		return networkAvailable;
	}

	public static netType getAPNType()
	{
		return netType;
	}

	private void notifyObserver()
	{

		for (int i = 0; i < netChangeObserverArrayList.size(); i++)
		{
			LNetChangeObserver observer = netChangeObserverArrayList.get(i);
			if (observer != null)
			{
				if (isNetworkAvailable())
				{
					observer.onConnect(netType);
				} else
				{
					observer.onDisConnect();
				}
			}
		}

	}

	/**
	 * 注册网络连接观察者
	 * 
	 * @param observerKey
	 *            observerKey
	 */
	public static void registerObserver(LNetChangeObserver observer)
	{
		if (netChangeObserverArrayList == null)
		{
			netChangeObserverArrayList = new ArrayList<LNetChangeObserver>();
		}
		netChangeObserverArrayList.add(observer);
	}

	/**
	 * 注销网络连接观察者
	 * 
	 * @param resID
	 *            observerKey
	 */
	public static void removeRegisterObserver(LNetChangeObserver observer)
	{
		if (netChangeObserverArrayList != null)
		{
			netChangeObserverArrayList.remove(observer);
		}
	}

}
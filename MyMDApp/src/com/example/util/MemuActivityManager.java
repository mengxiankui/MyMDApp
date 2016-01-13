package com.example.util;


import com.example.mymdapp.R;

import android.app.Activity;
import android.content.Context;


public class MemuActivityManager
{
	
    public static <T extends Activity> T getActivityAtPosition(int position,Context c)
    {
    	String menuActivityList[] = c.getResources().getStringArray(
				R.array.menu_activity);
        if (menuActivityList.length > position)
        {
            String strApp = menuActivityList[position];
            if (!"".equals(strApp))
            {
                try
                {
                    T activity = (T) Class.forName(
                        "com.example.activities." + "" + strApp)
                            .newInstance();
                    return activity;
                }
                catch (InstantiationException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                catch (IllegalAccessException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                catch (ClassNotFoundException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

}

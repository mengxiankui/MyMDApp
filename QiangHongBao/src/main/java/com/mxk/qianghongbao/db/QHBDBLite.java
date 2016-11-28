package com.mxk.qianghongbao.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mxk.qianghongbao.utils.Consts.WeixinQHBConst;


public class QHBDBLite extends SQLiteOpenHelper
{
    public QHBDBLite(Context context)
    {
        super(context, WeixinQHBConst.DBNAME, null, WeixinQHBConst.VERSION);
        // TODO Auto-generated constructor stub 
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        // TODO Auto-generated method stub
        //创建抢红包数据表
        db.execSQL("create table " + WeixinQHBConst.TNAME + "(" + WeixinQHBConst.TID
            + " integer primary key autoincrement not null," + WeixinQHBConst.MONEY
            + " float not null," + WeixinQHBConst.NAME + " text not null," + WeixinQHBConst.DATE
            + " text not null);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // TODO Auto-generated method stub 
    }
}
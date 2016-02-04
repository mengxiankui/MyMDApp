package com.example.util;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.util.Consts.DiaryConst;


public class DBLite extends SQLiteOpenHelper
{
    public DBLite(Context context)
    {
        super(context, DiaryConst.DBNAME, null, DiaryConst.VERSION);
        // TODO Auto-generated constructor stub 
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        // TODO Auto-generated method stub
        //创建日记本数据表
        db.execSQL("create table " + DiaryConst.TNAME + "(" + DiaryConst.TID
            + " integer primary key autoincrement not null," + DiaryConst.TITLE
            + " text not null," + DiaryConst.CONTENT + " text not null," + DiaryConst.DATE
            + " interger not null);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // TODO Auto-generated method stub 
    }
}
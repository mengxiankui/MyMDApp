package com.example.util;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBLite extends SQLiteOpenHelper
{
    public DBLite(Context context)
    {
        super(context, Consts.DBNAME, null, Consts.VERSION);
        // TODO Auto-generated constructor stub 
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        // TODO Auto-generated method stub 
        db.execSQL("create table " + Consts.TNAME + "(" + Consts.TID
            + " integer primary key autoincrement not null," + Consts.TITLE
            + " text not null," + Consts.CONTENT + " text not null," + Consts.DATE
            + " interger not null);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // TODO Auto-generated method stub 
    }
}
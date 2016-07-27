package com.mxk.utils;


import android.net.Uri;


public class Consts
{
    //app
    public static final String ARG_SECTION_NUMBER = "section_number";
    
    //日记相关
    public static final String DBNAME = "diarydb";
    public static final String TNAME = "diary";
    public static final int VERSION = 1;

    public static String TID = "_id";
    public static final String TITLE = "title";
    public static final String CONTENT = "content";
    public static final String DATE = "date";
    
    public static final String DIARY_TYPE = "type";
    public static final String TYPE_NEW = "new";
    public static final String TYPE_EDIT = "edit";

    public static final String AUTOHORITY = "com.mxk.diary";
    public static final int ITEM = 1;
    public static final int ITEM_ID = 2;

    public static final String ORDER_DATE_DESC = "date DESC";

    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.mxk.diary";
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.mxk.diary";

    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTOHORITY + "/"
        + TNAME);
    
    
}
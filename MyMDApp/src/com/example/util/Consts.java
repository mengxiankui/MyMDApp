package com.example.util;


import android.net.Uri;


public class Consts
{
    public static final class DiaryConst
    {
        //日记相关
        public static final String DBNAME = "diary_db";
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

    public static final class WeixinQHBConst
    {
        //抢红包相关
        public static final String DBNAME = "qhb_db";
        public static final String TNAME = "weixin_qhb";
        public static final int VERSION = 1;


        public static String TID = "_id";
        public static final String MONEY = "money";
        public static final String NAME = "name";
        public static final String DATE = "date";

        public static final String AUTOHORITY = "com.mxk.qhb";
        public static final int ITEM = 1;
        public static final int ITEM_ID = 2;

        public static final String ORDER_DATE_DESC = "date DESC";

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.mxk.qhb";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.mxk.qhb";

        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTOHORITY + "/"
                + TNAME);
    }
    
}
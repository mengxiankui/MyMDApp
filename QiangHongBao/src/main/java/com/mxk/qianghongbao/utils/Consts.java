package com.mxk.qianghongbao.utils;


import android.net.Uri;


public class Consts
{

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
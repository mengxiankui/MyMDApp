package com.qianghongbao.db;


import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.example.util.Consts.WeixinQHBConst;
import com.mxk.baseframe.util.log.Logger;

public class QHBProvider extends ContentProvider
{
    private static final String LOG_TAG = QHBProvider.class.getSimpleName();
    QHBDBLite dBlite;
    SQLiteDatabase db;

    private static final UriMatcher sMatcher;
    static
    {
        sMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sMatcher.addURI(WeixinQHBConst.AUTOHORITY, WeixinQHBConst.TNAME, WeixinQHBConst.ITEM);
        sMatcher.addURI(WeixinQHBConst.AUTOHORITY, WeixinQHBConst.TNAME + "/#", WeixinQHBConst.ITEM_ID);

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs)
    {
        // TODO Auto-generated method stub 
        db = dBlite.getWritableDatabase();
        int count = 0;
        switch (sMatcher.match(uri))
        {
            case WeixinQHBConst.ITEM :
                count = db.delete(WeixinQHBConst.TNAME, selection, selectionArgs);
                break;
            case WeixinQHBConst.ITEM_ID :
                String id = uri.getPathSegments().get(1);
                count = db
                        .delete(WeixinQHBConst.TNAME,
                            WeixinQHBConst.TID
                                + "="
                                + id
                                + (!TextUtils.isEmpty(selection) ? "AND(" + selection
                                    + ')' : ""), selectionArgs);
                break;
            default :
                throw new IllegalArgumentException("Unknown URI" + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(Uri uri)
    {
        // TODO Auto-generated method stub 
        switch (sMatcher.match(uri))
        {
            case WeixinQHBConst.ITEM :
                return WeixinQHBConst.CONTENT_TYPE;
            case WeixinQHBConst.ITEM_ID :
                return WeixinQHBConst.CONTENT_ITEM_TYPE;
            default :
                throw new IllegalArgumentException("Unknown URI" + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values)
    {
        // TODO Auto-generated method stub 

        db = dBlite.getWritableDatabase();
        long rowId;
        if (sMatcher.match(uri) != WeixinQHBConst.ITEM)
        {
            throw new IllegalArgumentException("Unknown URI" + uri);
        }
        rowId = db.insert(WeixinQHBConst.TNAME, WeixinQHBConst.TID, values);
        if (rowId > 0)
        {
            Uri noteUri = ContentUris.withAppendedId(WeixinQHBConst.CONTENT_URI, rowId);
            getContext().getContentResolver().notifyChange(noteUri, null);
            return noteUri;
        }
        throw new IllegalArgumentException("Unknown URI" + uri);
    }

    @Override
    public boolean onCreate()
    {
        // TODO Auto-generated method stub 
        this.dBlite = new QHBDBLite(this.getContext());
        //            db = dBlite.getWritableDatabase(); 
        //            return (db == null)?false:true; 
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
            String[] selectionArgs, String sortOrder)
    {
        // TODO Auto-generated method stub 
        db = dBlite.getReadableDatabase();
        Cursor c;

        Logger.d(LOG_TAG, "query, sMatcher.match(uri) = " + String.valueOf(sMatcher.match(uri)));
        switch (sMatcher.match(uri))
        {
            case WeixinQHBConst.ITEM :
                c = db.query(WeixinQHBConst.TNAME, projection, selection, selectionArgs, null,
                    null, sortOrder);

                break;
            case WeixinQHBConst.ITEM_ID :
                String id = uri.getPathSegments().get(1);
                c = db.query(WeixinQHBConst.TNAME, projection, WeixinQHBConst.TID + "=" + id
                    + (!TextUtils.isEmpty(selection) ? "AND(" + selection + ')' : ""),
                    selectionArgs, null, null, sortOrder);
                break;
            default :
                Log.d("!!!!!!", "Unknown URI" + uri);
                throw new IllegalArgumentException("Unknown URI" + uri);
        }
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
            String[] selectionArgs)
    {
        // TODO Auto-generated method stub 
        db = dBlite.getWritableDatabase();
        if (sMatcher.match(uri) == WeixinQHBConst.ITEM)
        {
            return db.update(WeixinQHBConst.TNAME, values, selection, selectionArgs);
        }
        else
        {
            String id = uri.getPathSegments().get(1);
            return db.update(WeixinQHBConst.TNAME, values,
                WeixinQHBConst.TID + "=" + id
                    + (!TextUtils.isEmpty(selection) ? "AND(" + selection + ')' : ""),
                selectionArgs);
        }

    }
}

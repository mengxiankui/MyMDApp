package com.example.util;


import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.example.util.Consts.DiaryConst;


public class DiaryProvider extends ContentProvider
{

    DBLite dBlite;
    SQLiteDatabase db;

    private static final UriMatcher sMatcher;
    static
    {
        sMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sMatcher.addURI(DiaryConst.AUTOHORITY, DiaryConst.TNAME, DiaryConst.ITEM);
        sMatcher.addURI(DiaryConst.AUTOHORITY, DiaryConst.TNAME + "/#", DiaryConst.ITEM_ID);

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs)
    {
        // TODO Auto-generated method stub 
        db = dBlite.getWritableDatabase();
        int count = 0;
        switch (sMatcher.match(uri))
        {
            case DiaryConst.ITEM :
                count = db.delete(DiaryConst.TNAME, selection, selectionArgs);
                break;
            case DiaryConst.ITEM_ID :
                String id = uri.getPathSegments().get(1);
                count = db
                        .delete(DiaryConst.TNAME,
                            DiaryConst.TID
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
            case DiaryConst.ITEM :
                return DiaryConst.CONTENT_TYPE;
            case DiaryConst.ITEM_ID :
                return DiaryConst.CONTENT_ITEM_TYPE;
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
        if (sMatcher.match(uri) != DiaryConst.ITEM)
        {
            throw new IllegalArgumentException("Unknown URI" + uri);
        }
        rowId = db.insert(DiaryConst.TNAME, DiaryConst.TID, values);
        if (rowId > 0)
        {
            Uri noteUri = ContentUris.withAppendedId(DiaryConst.CONTENT_URI, rowId);
            getContext().getContentResolver().notifyChange(noteUri, null);
            return noteUri;
        }
        throw new IllegalArgumentException("Unknown URI" + uri);
    }

    @Override
    public boolean onCreate()
    {
        // TODO Auto-generated method stub 
        this.dBlite = new DBLite(this.getContext());
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
        Log.d("-------", String.valueOf(sMatcher.match(uri)));
        switch (sMatcher.match(uri))
        {
            case DiaryConst.ITEM :
                c = db.query(DiaryConst.TNAME, projection, selection, selectionArgs, null,
                    null, sortOrder);

                break;
            case DiaryConst.ITEM_ID :
                String id = uri.getPathSegments().get(1);
                c = db.query(DiaryConst.TNAME, projection, DiaryConst.TID + "=" + id
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
        if (sMatcher.match(uri) == DiaryConst.ITEM)
        {
            return db.update(DiaryConst.TNAME, values, selection, selectionArgs);
        }
        else
        {
            String id = uri.getPathSegments().get(1);
            return db.update(DiaryConst.TNAME, values,
                DiaryConst.TID + "=" + id
                    + (!TextUtils.isEmpty(selection) ? "AND(" + selection + ')' : ""),
                selectionArgs);
        }

    }
}

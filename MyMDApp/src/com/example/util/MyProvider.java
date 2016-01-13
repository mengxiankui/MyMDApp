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


public class MyProvider extends ContentProvider
{

    DBLite dBlite;
    SQLiteDatabase db;

    private static final UriMatcher sMatcher;
    static
    {
        sMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sMatcher.addURI(Consts.AUTOHORITY, Consts.TNAME, Consts.ITEM);
        sMatcher.addURI(Consts.AUTOHORITY, Consts.TNAME + "/#", Consts.ITEM_ID);

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs)
    {
        // TODO Auto-generated method stub 
        db = dBlite.getWritableDatabase();
        int count = 0;
        switch (sMatcher.match(uri))
        {
            case Consts.ITEM :
                count = db.delete(Consts.TNAME, selection, selectionArgs);
                break;
            case Consts.ITEM_ID :
                String id = uri.getPathSegments().get(1);
                count = db
                        .delete(Consts.TNAME,
                            Consts.TID
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
            case Consts.ITEM :
                return Consts.CONTENT_TYPE;
            case Consts.ITEM_ID :
                return Consts.CONTENT_ITEM_TYPE;
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
        if (sMatcher.match(uri) != Consts.ITEM)
        {
            throw new IllegalArgumentException("Unknown URI" + uri);
        }
        rowId = db.insert(Consts.TNAME, Consts.TID, values);
        if (rowId > 0)
        {
            Uri noteUri = ContentUris.withAppendedId(Consts.CONTENT_URI, rowId);
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
            case Consts.ITEM :
                c = db.query(Consts.TNAME, projection, selection, selectionArgs, null,
                    null, sortOrder);

                break;
            case Consts.ITEM_ID :
                String id = uri.getPathSegments().get(1);
                c = db.query(Consts.TNAME, projection, Consts.TID + "=" + id
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
        if (sMatcher.match(uri) == Consts.ITEM)
        {
            return db.update(Consts.TNAME, values, selection, selectionArgs);
        }
        else
        {
            String id = uri.getPathSegments().get(1);
            return db.update(Consts.TNAME, values,
                Consts.TID + "=" + id
                    + (!TextUtils.isEmpty(selection) ? "AND(" + selection + ')' : ""),
                selectionArgs);
        }

    }
}

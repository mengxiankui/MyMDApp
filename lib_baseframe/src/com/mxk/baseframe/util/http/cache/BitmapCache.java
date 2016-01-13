package com.mxk.baseframe.util.http.cache;


import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader.ImageCache;

@Deprecated
public class BitmapCache implements ImageCache
{

    private LruCache<String, Bitmap> mCache;

    public BitmapCache()
    {
        int maxSize = getDefaultLruCacheSize();
        mCache = new LruCache<String, Bitmap>(maxSize)
        {
            @Override
            protected int sizeOf(String key, Bitmap value)
            {
                return value.getRowBytes() * value.getHeight();
            }
        };
    }

    @Override
    public Bitmap getBitmap(String url)
    {
        return mCache.get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap)
    {
        mCache.put(url, bitmap);
    }

    private static int getDefaultLruCacheSize()
    {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory());
        final int cacheSize = maxMemory / 8;
        return cacheSize;
    }

}

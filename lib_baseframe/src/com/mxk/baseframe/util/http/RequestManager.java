package com.mxk.baseframe.util.http;


import android.content.Context;
import android.text.TextUtils;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;


public class RequestManager
{
    private static RequestQueue mRequestQueue;

    private volatile static RequestManager INSTANCE = null;

    public void init(Context context)
    {
        mRequestQueue = Volley.newRequestQueue(context);
    }

    private RequestManager()
    {

    }

    public static RequestManager getInstance()
    {
        if (null == INSTANCE)
        {
            synchronized (RequestManager.class)
            {
                if (null == INSTANCE)
                {
                    INSTANCE = new RequestManager();
                }
            }
        }
        return INSTANCE;
    }

    public RequestQueue getRequestQueue()
    {
        return mRequestQueue;
    }

    private static String makeURLForParams(String url, RequestMap params)
    {
        if (TextUtils.isEmpty(url))
        {
            url = null;
        }
        String strParams = params.getParams();
        if (TextUtils.isEmpty(strParams))
        {
            return url;
        }
        return url + "?" + strParams;
    }

    private void throwCallbackNull(RequestCallback callback)
    {
        if (callback == null)
            throw new NullPointerException("网络请求的回调函数为空");
    }

    public void cancelAll(Object tag)
    {
        if (tag == null)
        {
            return;
        }
        getRequestQueue().cancelAll(tag);
    }

    public void download(String url, String path, String name, RequestCallback callback)
    {
        download(url, path, name, null, callback);
    }

    public void download(String url, String path, String name, Object tag,
            RequestCallback callback)
    {
        throwCallbackNull(callback);
        LDownloadRequest downloadRequest = new LDownloadRequest(url, path, name, callback);
        downloadRequest.setTag(tag);
        getRequestQueue().add(downloadRequest);
    }

    public void get(String url, RequestCallback callback)
    {
        get(url, null, null, callback);
    }

    public void get(String url, RequestMap params, RequestCallback callback)
    {
        get(url, params, null, callback);
    }

    public void get(String url, RequestMap params, Object tag, RequestCallback callback)
    {
        throwCallbackNull(callback);
        String requesturl = makeURLForParams(url, params);
        LStringRequest stringRequest = new LStringRequest(0, requesturl, params, callback);
        stringRequest.setTag(tag);
        getRequestQueue().add(stringRequest);
    }

    public void post(String url, RequestCallback callback)
    {
        post(url, null, null, callback);
    }

    public void post(String url, RequestMap params, RequestCallback callback)
    {
        post(url, params, null, callback);
    }

    public void post(String url, RequestMap params, Object tag, RequestCallback callback)
    {
        throwCallbackNull(callback);
        if ((params != null) && (params.hasUpload()))
        {
            LUploadRequest uploadRequest = new LUploadRequest(1, url, params, callback);
            uploadRequest.setTag(tag);
            getRequestQueue().add(uploadRequest);
        }
        else
        {
            LStringRequest stringRequest = new LStringRequest(1, url, params, callback);
            stringRequest.setTag(tag);
            getRequestQueue().add(stringRequest);
        }

    }

}
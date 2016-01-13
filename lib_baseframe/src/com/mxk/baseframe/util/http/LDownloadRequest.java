package com.mxk.baseframe.util.http;


import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class LDownloadRequest extends Request<String>
{
    private static final float BACKOFF_MULT = 2.0F;
    private static final int MAX_RETRIES = 2;
    private static final int TIMEOUT_MS = 30000;
    private static final Object sDecodeLock = new Object();
    private final RequestCallback mCallback;
    private final String name;
    private final String path;

    public LDownloadRequest(String url, String path, String name, RequestCallback callback)
    {
        super(Method.GET, url, null);
        setRetryPolicy(new DefaultRetryPolicy(TIMEOUT_MS, MAX_RETRIES, BACKOFF_MULT));
        this.path = path;
        this.name = name;
        mCallback = callback;
        onStart();
    }

    private void onStart()
    {
        mCallback.onStart(getMethod(), getUrl());
    }

    private File doParse(NetworkResponse networkResponse) throws IOException
    {
        byte[] b = networkResponse.data;
        if ((b == null) || (b.length <= 0))
        {
            return null;
        }

        File file = new File(this.path, this.name);
        if ((!file.getParentFile().exists()) && (file.getParentFile().isDirectory()))
        {
            file.getParentFile().mkdirs();
        }

        FileOutputStream fOStream = new FileOutputStream(file);
        BufferedOutputStream bOStream = new BufferedOutputStream(fOStream);
        try
        {
            bOStream.write(b);
            return file;
        }
        finally
        {
            bOStream.close();
            fOStream.close();
        }
    }

    @Override
    public void deliverError(VolleyError volleyError)
    {
        LError error = null;
        int statusCode = -1;
        if (null != volleyError.networkResponse)
        {
            statusCode = volleyError.networkResponse.statusCode;
        }
        if ((volleyError == null) || ((volleyError instanceof NetworkError))
            || ((volleyError instanceof NoConnectionError))
            || ((volleyError instanceof ServerError)))
        {
            error = LError.NET_ERROR;

        }
        else if (volleyError instanceof TimeoutError)
        {
            error = LError.NET_TIMEOUT_ERROR;
        }
        else if (volleyError instanceof DownloadError)
        {
            error = LError.DOWNLOAD_ERROR;
        }
        else if (volleyError instanceof AuthFailureError)
        {
            error = LError.AUTH_ERROR;
        }
        else
        {
            error = LError.OTHER;
        }
        mCallback.onError(error, statusCode);
    }

    protected void deliverResponse(String response)
    {
        mCallback.onResponse(response);
    }

    @Override
    public Request.Priority getPriority()
    {
        return Request.Priority.LOW;
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse networkResponse)
    {
        try
        {
            synchronized (sDecodeLock)
            {
                File file = doParse(networkResponse);
                if ((file != null) && (file.exists()))
                {
                    return Response.success(file.getPath(),
                        HttpHeaderParser.parseCacheHeaders(networkResponse));
                }
                else
                {
                    return Response.error(new DownloadError(networkResponse));
                }
            }
        }
        catch (OutOfMemoryError outOfMemoryError)
        {
            return Response.error(new DownloadError(outOfMemoryError));
        }
        catch (IOException IOException)
        {
            return Response.error(new DownloadError(IOException));
        }
    }
}
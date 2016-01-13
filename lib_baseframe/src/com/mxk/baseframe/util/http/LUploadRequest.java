package com.mxk.baseframe.util.http;


import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.mxk.baseframe.util.http.RequestMap.PFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.http.entity.mime.MultipartEntityBuilder;


public class LUploadRequest extends Request<String>
{
    private static final float BACKOFF_MULT = 0.0F;
    private static final int MAX_RETRIES = 0;
    private static final int TIMEOUT_MS = 30000;
    private final RequestCallback mCallback;
    private MultipartEntityBuilder builder;
    private final RequestMap params;

    public LUploadRequest(int method, String url, RequestMap params,
            RequestCallback callback)
    {
        super(method, url, null);
        setRetryPolicy(new DefaultRetryPolicy(TIMEOUT_MS, MAX_RETRIES, BACKOFF_MULT));
        this.params = params;
        mCallback = callback;
        builder = MultipartEntityBuilder.create();
        initStringParams();
        initFileParams();
        onStart();
    }

    private void initFileParams()
    {
        if (params == null)
        {
            return;
        }
        else
        {
            Iterator<PFile> iterator = params.getFList().iterator();
            while (iterator.hasNext())
            {
                PFile f = (PFile) iterator.next();
                builder.addBinaryBody(f.name, f.file);
            }
        }

    }

    private void initStringParams()
    {
        if (params == null)
        {
            return;
        }
        Iterator<Entry<String, String>> iterator = params.getMap().entrySet().iterator();
        while (iterator.hasNext())
        {
            Entry<String, String> entry = (Entry<String, String>) iterator.next();
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            builder.addTextBody(key, value);
        }
    }

    private void onStart()
    {
        this.mCallback.onStart(getMethod(), getUrl());
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
        else if (volleyError instanceof ParseError)
        {
            error = LError.PARSE_ERROR;
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

    @Override
    protected void deliverResponse(String response)
    {
        mCallback.onResponse(response);
    }

    @Override
    public byte[] getBody()
    {
        ByteArrayOutputStream BAOStream = new ByteArrayOutputStream();
        try
        {
            builder.build().writeTo(BAOStream);
        }
        catch (IOException localIOException)
        {
        }
        return BAOStream.toByteArray();
    }

    public String getBodyContentType()
    {
        return builder.build().getContentType().getValue();
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse paramNetworkResponse)
    {
        return null;

    }
}
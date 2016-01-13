/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mxk.baseframe.util.http;


import java.io.UnsupportedEncodingException;
import java.util.Map;

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
import com.android.volley.toolbox.HttpHeaderParser;


/**
 * A canned request for retrieving the response body at a given URL as a String.
 */
public class LStringRequest extends Request<String>
{
    private static final float BACKOFF_MULT = 0.0F;
    private static final int MAX_RETRIES = 0;
    private static final int TIMEOUT_MS = 30000;
    private final RequestCallback mCallback;
    private final RequestMap mParams;

    /**
     * Creates a new request with the given method.
     *
     * @param method the request {@link Method} to use
     * @param url URL to fetch the string at
     * @param listener Listener to receive the String response
     * @param errorListener Error listener, or null to ignore errors
     */
    public LStringRequest(int method, String url, RequestMap params, RequestCallback callback)
    {
        super(method, url, null);
        setRetryPolicy(new DefaultRetryPolicy(TIMEOUT_MS, MAX_RETRIES, BACKOFF_MULT));
        mParams = params;
        mCallback = callback;
        onStart();
    }

    private void onStart()
    {
        mCallback.onStart(getMethod(), getUrl());
    }

    /**
     * Creates a new GET request.
     *
     * @param url URL to fetch the string at
     * @param listener Listener to receive the String response
     * @param errorListener Error listener, or null to ignore errors
     */
    public LStringRequest(String url, RequestCallback callback)
    {
        this(Method.GET, url, null, callback);
    }

    @Override
    protected Map<String, String> getParams()
    {
        return null == mParams ? null : mParams.getMap();
    }

    @Override
    public void deliverError(VolleyError volleyError)
    {
        // TODO Auto-generated method stub
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
    protected Response<String> parseNetworkResponse(NetworkResponse response)
    {
        String parsed;
        try
        {
            parsed = new String(response.data,
                HttpHeaderParser.parseCharset(response.headers));
        }
        catch (UnsupportedEncodingException e)
        {
            parsed = new String(response.data);
        }
        return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
    }

}

package com.mxk.baseframe.util.http;


public abstract class RequestCallback
{
    protected abstract void onResponse(String response);

    protected abstract void onError(LError error, int statusCode);

    protected abstract void onParse(String strResponse);

    protected void onStart(int method, String url)
    {
        
    }
}
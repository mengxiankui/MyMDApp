package com.mxk.baseframe.util.http;


import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

/**
 * Indicates that the download task has failed.
 */
@SuppressWarnings("serial")
public class DownloadError extends VolleyError
{
    public DownloadError()
    {
    }

    public DownloadError(NetworkResponse paramNetworkResponse)
    {
        super(paramNetworkResponse);
    }

    public DownloadError(Throwable paramThrowable)
    {
        super(paramThrowable);
    }
}
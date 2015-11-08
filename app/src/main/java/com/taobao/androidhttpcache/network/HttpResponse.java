package com.taobao.androidhttpcache.network;

import java.io.InputStream;

/**
 * Created by rowandjj on 15/11/8.
 */
@SuppressWarnings("unused")
public class HttpResponse {
    private int responseCode;
    private InputStream data;
    private int contentLength;

    public HttpResponse(int responseCode, InputStream data, int contentLength) {
        this.responseCode = responseCode;
        this.data = data;
        this.contentLength = contentLength;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public InputStream getData() {
        return data;
    }

    public void setData(InputStream data) {
        this.data = data;
    }

    public int getContentLength() {
        return contentLength;
    }

    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }
}

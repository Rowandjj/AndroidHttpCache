package com.taobao.androidhttpcache.network;

import android.graphics.Bitmap;

/**
 * Created by rowandjj on 15/11/8.
 */
@SuppressWarnings("unused")
public class HttpResponse {
    private int responseCode;
    private String data;
    private int contentLength;
    private Bitmap bitmap;

    public HttpResponse(int responseCode, String data, int contentLength,Bitmap bitmap) {
        this.responseCode = responseCode;
        this.data = data;
        this.contentLength = contentLength;
        this.bitmap = bitmap;
    }


    public void setData(String data) {
        this.data = data;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public int getContentLength() {
        return contentLength;
    }

    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }
}

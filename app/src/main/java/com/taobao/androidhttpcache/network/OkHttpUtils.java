package com.taobao.androidhttpcache.network;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.squareup.okhttp.Cache;
import com.squareup.okhttp.CacheControl;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

/**
 * Created by rowandjj on 15/11/9.
 * <p>
 * 1. learn how to use okhttp
 * 2. okhttp with cache control
 */
@SuppressWarnings("unused")
public class OkHttpUtils {
    private OkHttpClient mHttpClient;
    private static final String TAG = OkHttpUtils.class.getSimpleName();

    private static final int DEFAULT_CONNECT_TIMEOUT = 10 * 1000;
    private static final int DEFAULT_READ_TIMEOUT = 10 * 1000;

    private static final String CACHE_DIR = "http";
    private static final long CACHE_SIZE = 10 * 1024 * 1024;

    private OkHttpUtils(Context context) {
        mHttpClient = new OkHttpClient();
        mHttpClient.setConnectTimeout(DEFAULT_CONNECT_TIMEOUT, TimeUnit.SECONDS);
        mHttpClient.setReadTimeout(DEFAULT_READ_TIMEOUT, TimeUnit.SECONDS);

        try {
            mHttpClient.setCache(new Cache(new File(context.getApplicationContext().getCacheDir(), CACHE_DIR), CACHE_SIZE));
        } catch (Exception e) {
        }
    }

    private static OkHttpUtils instance;

    public static OkHttpUtils getInstance(Context context) {
        if (instance == null) {
            synchronized (OkHttpUtils.class) {
                if (instance == null) {
                    instance = new OkHttpUtils(context);
                }
            }
        }
        return instance;
    }


    public void asyncGetBitmap(final Uri uri, final Policy policy, final com.taobao.androidhttpcache.network.Callback callback) {
        if (uri == null) {
            throw new IllegalArgumentException("uri is empty");
        }
        new AsyncTask<Void, Void, HttpResponse>() {
            @Override
            protected HttpResponse doInBackground(Void... params) {
                return getBitmap(uri.toString(), policy);
            }

            @Override
            protected void onPostExecute(HttpResponse httpResponse) {
                if (callback != null) {
                    callback.call(httpResponse);
                }
            }
        }.execute();
    }

    public HttpResponse getBitmap(String url, Policy policy) {
        if (TextUtils.isEmpty(url)) {
            throw new IllegalArgumentException("url is empty");
        }

        CacheControl cacheControl;
        if (policy == Policy.Cache) {
            cacheControl = CacheControl.FORCE_CACHE;
        } else {
            CacheControl.Builder builder = new CacheControl.Builder();
            cacheControl = builder.noCache().build();
        }


        Request request = new Request.Builder()
                .url(url).cacheControl(cacheControl).build();
        try {
            //sync call
            Response response = mHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                InputStream in = response.body().byteStream();
                if (in != null) {
                    Bitmap bitmap = BitmapFactory.decodeStream(in);
                    Log.d(TAG, "cache response:" + response.cacheResponse());
                    return new HttpResponse(response.code(), null, (int) response.body().contentLength(), bitmap);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void shutdown() {
        com.squareup.okhttp.Cache cache = mHttpClient.getCache();
        if (cache != null) {
            try {
                cache.close();
            } catch (IOException ignored) {
            }
        }
    }


    //--------基本使用--------------
    private static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    public String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = mHttpClient.newCall(request).execute();
        return response.body().string();
    }

    public String get(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        Response response = mHttpClient.newCall(request).execute();
        return response.body().string();
    }

    public void asyncGet(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        mHttpClient.newCall(request).enqueue(new com.squareup.okhttp.Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                //I am not in UI Thread..
                Log.d(TAG, "Thread name:" + Thread.currentThread().getName());
                //do sth...
            }
        });
    }


}

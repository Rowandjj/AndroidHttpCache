package com.taobao.androidhttpcache.network;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.net.http.HttpResponseCache;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.taobao.androidhttpcache.MainActivity;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by rowandjj on 15/11/8.
 *
 * HttpUrlConnection with Http Cache used.
 */
@SuppressWarnings("unused")
public class NetworkUtils {
    private NetworkUtils() {
    }

    private static final String TAG = NetworkUtils.class.getSimpleName();
    private static volatile HttpResponseCache mCache;
    private static final int DEFAULT_CONNECT_TIMEOUT = 10*1000;
    private static final int DEFAULT_READ_TIMEOUT = 10*1000;


    public static void asyncGetBitmap(final Context context, final Uri uri, final Policy policy, final Callback callback){
        new AsyncTask<Void,Void,HttpResponse>(){
            @Override
            protected HttpResponse doInBackground(Void... params) {
                return getBitmap(context, uri, policy);
            }

            @Override
            protected void onPostExecute(HttpResponse httpResponse) {
                if(callback != null){
                    callback.call(httpResponse);
                }
            }
        }.execute();
    }

    public static HttpResponse getBitmap(Context context,Uri uri,Policy policy){
        if(uri == null){
            throw new IllegalArgumentException("uri is null");
        }
        installCacheIfNeeded(context);
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(MainActivity.URL).openConnection();
            connection.setConnectTimeout(DEFAULT_CONNECT_TIMEOUT);
            connection.setReadTimeout(DEFAULT_READ_TIMEOUT);

            if(policy == Policy.Cache){
                connection.setUseCaches(true);
            }else{
                connection.setUseCaches(false);
                connection.addRequestProperty("Cache-Control", "no-cache");
                connection.addRequestProperty("Cache-Control","max-age=0");
            }

            int contentLen = connection.getContentLength();
            int responseCode = connection.getResponseCode();
            HttpResponse response = new HttpResponse(responseCode,null,contentLen, BitmapFactory.decodeStream(connection.getInputStream()));
            connection.getInputStream().close();
            return response;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }




    private static void installCacheIfNeeded(Context context) {
        if (mCache == null) {
            synchronized (NetworkUtils.class) {
                if (mCache == null) {
                    mCache = HttpCache.install(context);
                }
            }
        }
    }

    private static void uninstallCache(){
        if(mCache != null){
            HttpCache.delete(mCache);
        }
    }



    public static void shutDown() {
        if (mCache != null) {
            HttpCache.close(mCache);
        }
    }

    public static void statistics() {
        if (mCache != null) {
            Log.d(TAG, "request count:" + HttpCache.getRequestCount(mCache) +
                    ",network count:" + HttpCache.getNetworkCount(mCache) + ",hit count:" + HttpCache.getHitCount(mCache));
        }
    }

    public static void statistics(Context context) {
        if (mCache != null) {
            Toast.makeText(context,"request count:" + HttpCache.getRequestCount(mCache) +
                    ",network count:" + HttpCache.getNetworkCount(mCache) + ",hit count:" + HttpCache.getHitCount(mCache),Toast.LENGTH_SHORT).show();
        }
    }




}

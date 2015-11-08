package com.taobao.androidhttpcache.network;

import android.content.Context;
import android.net.http.HttpResponseCache;
import android.os.Build;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * Created by rowandjj on 15/11/8.
 */
@SuppressWarnings("unused")
class HttpCache {

    private HttpCache() {
    }

    private static final String CACHE_DIR = "http";
    private static final long CACHE_SIZE = 10 * 1024 * 1024;
    private static final String TAG = HttpCache.class.getSimpleName();


    static HttpResponseCache install(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("context must not be null");
        }

        File cacheDir = new File(context.getApplicationContext().getCacheDir(), CACHE_DIR);
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }

        HttpResponseCache cache = null;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            try {
                if ((cache = HttpResponseCache.getInstalled()) != null) {
                    return cache;
                }
                cache = HttpResponseCache.install(cacheDir, CACHE_SIZE);
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "http response installation failed,code = 0");
            }
        } else {
            try {
                if ((cache = (HttpResponseCache) Class.forName("android.net.http.HttpResponseCache").getMethod("getInstalled").invoke(null)) != null) {
                    return cache;
                }
                Method method = Class.forName("android.net.http.HttpResponseCache").getMethod("install", File.class, long.class);
                cache = (HttpResponseCache) method.invoke(null, cacheDir, CACHE_SIZE);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "http response installation failed,code = 1");
            }
        }
        return cache;
    }

    static void close(HttpResponseCache cache){
        if(cache != null){
            try {
                cache.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static int getHitCount(HttpResponseCache cache){
        if(cache != null){
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH){
                return cache.getHitCount();
            }else{
                try {
                    return (int) Class.forName("android.net.http.HttpResponseCache").getMethod("getHitCount").invoke(cache);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return 0;
    }

    static int getNetworkCount(HttpResponseCache cache){
        if(cache != null){
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH){
                return cache.getNetworkCount();
            }else{
                try {
                    return (int) Class.forName("android.net.http.HttpResponseCache").getMethod("getNetworkCount").invoke(cache);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return 0;
    }

    static int getRequestCount(HttpResponseCache cache){
        if(cache != null){
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH){
                return cache.getRequestCount();
            }else{
                try {
                    return (int) Class.forName("android.net.http.HttpResponseCache").getMethod("getRequestCount").invoke(cache);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return 0;
    }

    /**
     * uninstall cache and delete cache data
     * */
    static void delete(HttpResponseCache cache){
        if(cache != null){
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH){
                try {
                    cache.delete();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                try {
                    Class.forName("android.net.http.HttpResponseCache").getMethod("delete").invoke(cache);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}





















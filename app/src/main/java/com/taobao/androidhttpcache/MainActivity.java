package com.taobao.androidhttpcache;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.taobao.androidhttpcache.network.Callback;
import com.taobao.androidhttpcache.network.HttpResponse;
import com.taobao.androidhttpcache.network.NetworkUtils;
import com.taobao.androidhttpcache.network.Policy;

public class MainActivity extends AppCompatActivity {
    public static final String URL = "http://img.alicdn.com/bao/uploaded/i4/2433767071/TB2hL7dfXXXXXaeXXXXXXXXXXXX_!!2433767071-0-paimai.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NetworkUtils.statistics(MainActivity.this);
            }
        });

        final TextView tv = (TextView) findViewById(R.id.tv_result);
        final ImageView iv = (ImageView) findViewById(R.id.iv_result);

        findViewById(R.id.btn_cache).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetworkUtils.asyncGetBitmap(MainActivity.this, Uri.parse(URL), Policy.Cache, new Callback() {
                    @Override
                    public void call(HttpResponse response) {
                        if (response == null) {
                            tv.setText("response is null...");
                            return;
                        }
                        tv.setText("response code:" + response.getResponseCode() + ",len:" + response.getContentLength());
                        if (response.getBitmap() != null) {
                            iv.setImageBitmap(response.getBitmap());
                        }
                    }
                });


//                OkHttpUtils.getInstance(MainActivity.this).asyncGetBitmap(Uri.parse(URL), Policy.Cache, new Callback() {
//                    @Override
//                    public void call(HttpResponse response) {
//                        if (response == null) {
//                            tv.setText("response is null...");
//                            return;
//                        }
//                        tv.setText("response code:" + response.getResponseCode() + ",len:" + response.getContentLength());
//                        if (response.getBitmap() != null) {
//                            iv.setImageBitmap(response.getBitmap());
//                        }
//                    }
//                });
            }
        });

        findViewById(R.id.btn_no_cache).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //使用HttpUrlConnection
                NetworkUtils.asyncGetBitmap(MainActivity.this, Uri.parse(URL), Policy.Network, new Callback() {
                    @Override
                    public void call(HttpResponse response) {
                        if (response == null) {
                            tv.setText("response is null...");
                            return;
                        }
                        tv.setText("response code:" + response.getResponseCode() + ",len:" + response.getContentLength());
                        if (response.getBitmap() != null) {
                            iv.setImageBitmap(response.getBitmap());
                        }
                    }
                });
                //使用OkHttp
//                OkHttpUtils.getInstance(MainActivity.this).asyncGetBitmap(Uri.parse(URL), Policy.Network, new Callback() {
//                    @Override
//                    public void call(HttpResponse response) {
//                        if (response == null) {
//                            tv.setText("response is null...");
//                            return;
//                        }
//                        tv.setText("response code:" + response.getResponseCode() + ",len:" + response.getContentLength());
//                        if (response.getBitmap() != null) {
//                            iv.setImageBitmap(response.getBitmap());
//                        }
//                    }
//                });


            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

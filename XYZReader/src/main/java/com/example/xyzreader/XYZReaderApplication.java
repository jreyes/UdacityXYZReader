package com.example.xyzreader;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import com.bumptech.glide.Glide;
import com.bumptech.glide.integration.okhttp.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.example.xyzreader.remote.XYZReaderApi;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static java.util.concurrent.TimeUnit.SECONDS;

public class XYZReaderApplication extends Application {
// ------------------------------ FIELDS ------------------------------

    private static final long CACHE_SIZE = 25 * 1024 * 1024;

    private OkHttpClient mOkHttpClient;
    private XYZReaderApi mXYZReaderApi;

// -------------------------- STATIC METHODS --------------------------

    public static XYZReaderApplication getApplication(@NonNull Context context) {
        return (XYZReaderApplication) context.getApplicationContext();
    }

    public static XYZReaderApi getXYZReaderApi(@NonNull Context context) {
        return getApplication(context).mXYZReaderApi;
    }

// -------------------------- OTHER METHODS --------------------------

    public void initXYZReaderApi() {
        mXYZReaderApi = new XYZReaderApi(mOkHttpClient);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        initOkClient();
        initGlide();
        initXYZReaderApi();
    }

    private Cache cache() {
        try {
            return new Cache(new File(getApplicationContext().getCacheDir(), "http"), CACHE_SIZE);
        } catch (IOException e) {
            return null;
        }
    }

    private void initGlide() {
        Glide
                .get(getApplicationContext())
                .register(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(mOkHttpClient));
    }

    private void initOkClient() {
        mOkHttpClient = new OkHttpClient();
        mOkHttpClient.setCache(cache());
        mOkHttpClient.setConnectTimeout(10, SECONDS);
        mOkHttpClient.setReadTimeout(10, SECONDS);
        mOkHttpClient.setWriteTimeout(10, SECONDS);
    }
}

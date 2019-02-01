package com.criteo.pubsdk.network;

import android.content.Context;

import com.criteo.pubsdk.BuildConfig;
import com.criteo.pubsdk.R;
import com.criteo.pubsdk.model.Cdb;
import com.criteo.pubsdk.model.Config;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

final class PubSdkApi {
    private static final int TIMEOUT = 60;

    private PubSdkApi() {
    }

    static Config loadConfig(Context context, int networkId, String appId, String sdkVersion) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(context.getString(R.string.config_url))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(getCdbHttpConfig())
                .build();
        Endpoints endpoints = retrofit.create(Endpoints.class);

        Call<JsonObject> responseCall
                = endpoints.config(networkId, appId, sdkVersion);
        Response<JsonObject> retrofitResponse = null;
        JsonObject result = new JsonObject();
        try {
            retrofitResponse = responseCall.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (retrofitResponse != null && retrofitResponse.isSuccessful()) {
            result = retrofitResponse.body();
        }
        return new Config(result);
    }

    static Cdb loadCdb(Context context, Cdb cdb) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(context.getString(R.string.cdb_url))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(getCdbHttpConfig())
                .build();
        Endpoints endpoints = retrofit.create(Endpoints.class);
        Call<JsonObject> responseCall
                = endpoints.cdb(cdb.toJson());
        Response<JsonObject> retrofitResponse = null;
        JsonObject result = new JsonObject();
        try {
            retrofitResponse = responseCall.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (retrofitResponse != null && retrofitResponse.isSuccessful()) {
            result = retrofitResponse.body();
        }
        return new Cdb(result);
    }

    private static OkHttpClient getCdbHttpConfig() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        if (BuildConfig.DEBUG) {
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        }
        return new OkHttpClient.Builder()
                .addInterceptor(new okhttp3.Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        Request.Builder builder = request.newBuilder()
                                .addHeader("Content-Type", "text/plain");
                        request = builder.build();
                        okhttp3.Response response = chain.proceed(request);
                        return response;
                    }
                })
                .addInterceptor(interceptor)
                .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
                .build();
    }

    private static OkHttpClient getConfigHttpConfig() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        if (BuildConfig.DEBUG) {
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        }
        return new OkHttpClient.Builder()
                .addInterceptor(new okhttp3.Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        Request.Builder builder = request.newBuilder()
                                .addHeader("Content-Type", "text/plain");
                        request = builder.build();
                        okhttp3.Response response = chain.proceed(request);
                        return response;
                    }
                })
                .addInterceptor(interceptor)
                .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
                .build();
    }
}
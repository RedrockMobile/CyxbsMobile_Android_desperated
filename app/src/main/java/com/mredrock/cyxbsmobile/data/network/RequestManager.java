package com.mredrock.cyxbsmobile.data.network;

import okhttp3.OkHttpClient;
import retrofit2.FastjsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.RxJavaCallAdapterFactory;
import retrofit2.SimpleXmlConverterFactory;

/**
 * Created by dayaa on 16/1/20.
 */
public class RequestManager {

    private static ApiService apiService;
    private static OkHttpClient client;

    public static void init() {

        client = new OkHttpClient();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://")
                .client(client)
                .addConverterFactory(FastjsonConverterFactory.create())
//                .addConverterFactory(SimpleXmlConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
    }

    public static ApiService getService() {
        return apiService;
    }
}

package com.mredrock.cyxbsmobile.util.download.progress;


import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;


public class ProgressHelper {
    /**
     * 包装OkHttpClient，用于下载文件的回调
     *
     * @param client           待包装的OkHttpClient
     * @param progressListener 进度回调接口
     * @return 包装后的OkHttpClient，使用clone方法返回
     */
    public static OkHttpClient addProgressResponseListener(OkHttpClient client, final ProgressListener progressListener) {
     /*   //克隆
        OkHttpClient clone = client;
        //增加拦截器
        client.networkInterceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {

                Request request = chain.request();
                //拦截
                Response originalResponse = chain.proceed(request);
                //包装响应体并返回
                return originalResponse.newBuilder()
                        .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                        .build();
            }
        });*/

        client = initOhHttp().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                //拦截
                Response originalResponse = chain.proceed(chain.request());
                //包装响应体并返回
                return originalResponse.newBuilder()
                        .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                        .build();
            }
        }).build();

        return client;
    }

    public final static OkHttpClient.Builder initOhHttp() {
        return new OkHttpClient.Builder();
    }

}

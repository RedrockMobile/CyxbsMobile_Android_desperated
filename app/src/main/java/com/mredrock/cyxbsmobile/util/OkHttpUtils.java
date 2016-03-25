package com.mredrock.cyxbsmobile.util;

import android.net.Uri;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * okHttp 工具类
 */
public class OkHttpUtils {

    public static final String TAG = "OkHttpUtils.java";

    private OkHttpUtils() {
        throw new UnsupportedOperationException("OkHttpUtils can't be instantiated");
    }

    /**
     * 返回请求体中的 File 部分
     * @param fileUri 文件uri
     * @param key File 的POST请求参数
     * @return MultipartBody.Part (File)
     */
    public static MultipartBody.Part createFileRequestBody(String key, Uri fileUri) {
        File file = FileUtils.getFile(fileUri);
        return file != null
                ? MultipartBody.Part.createFormData(key, file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file))
                : null;
    }

    /**
     * 返回请求体中的其他字符串部分
     * @param values POST请求参数，key在 请求服务
     *               {@link com.mredrock.cyxbsmobile.network.service.UpDownloadService}
     *               的 @Part("key") 注解中写
     * @return RequestBody
     * {@link retrofit2.http.Part}
     */
    public static RequestBody createStringRequestBody(String values) {
        return RequestBody.create(MediaType.parse("multipart/form-data"), values);
    }
}

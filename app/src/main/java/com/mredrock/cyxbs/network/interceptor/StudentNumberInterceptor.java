package com.mredrock.cyxbs.network.interceptor;

import com.mredrock.cyxbs.APP;
import com.mredrock.cyxbs.config.Const;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * A okhttp3 interceptor for new api version 20160805
 * Auto add optional parameters idNum & stuNum when user has login
 * Intercepted API:
 * {@link com.mredrock.cyxbs.config.Const#API_SOCIAL_HOT_LIST}
 * {@link com.mredrock.cyxbs.config.Const#API_SOCIAL_BBDD_LIST}
 * {@link com.mredrock.cyxbs.config.Const#API_GET_PERSON_LATEST}
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

public class StudentNumberInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        String url = chain.request().url().toString();
        if (APP.isLogin() && (
                   url.equals(Const.END_POINT_REDROCK + Const.API_SOCIAL_HOT_LIST)
                || url.equals(Const.END_POINT_REDROCK + Const.API_SOCIAL_BBDD_LIST)
                || url.equals(Const.END_POINT_REDROCK + Const.API_GET_PERSON_LATEST)
                || url.equals(Const.END_POINT_REDROCK + Const.API_TREND_DETAIL))) {
            return doIntercept(chain);
        } else {
            return chain.proceed(chain.request());
        }
    }

    private Response doIntercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        Request.Builder builder = originalRequest.newBuilder();
        RequestBody originalFormBody = originalRequest.body();
        if (originalFormBody instanceof FormBody) {
            FormBody.Builder newBuilder = new FormBody.Builder();
            for (int i = 0; i < ((FormBody) originalFormBody).size(); i++) {
                newBuilder.addEncoded(((FormBody) originalFormBody).encodedName(i), ((FormBody) originalFormBody).encodedValue(i));
            }
            // Add optional idNum and stuNum
            newBuilder.add("idNum", APP.getUser(APP.getContext()).idNum);
            newBuilder.add("stuNum", APP.getUser(APP.getContext()).stuNum);
            builder.method(originalRequest.method(), newBuilder.build());
        }
        return chain.proceed(builder.build());
    }

}

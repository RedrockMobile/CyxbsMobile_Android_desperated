package com.mredrock.cyxbs.network.exception;

import com.mredrock.cyxbs.model.lost.LostStatus;

import retrofit2.adapter.rxjava.HttpException;

/**
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

public class LostApiException extends RuntimeException {

    private int code;
    private String statusString;

    public LostApiException(int code, LostStatus status) {
        super("Exception in Lost API: HTTP Code " + code + ", status: " + status);
        this.code = code;
        this.statusString = status.status;
    }

    public LostApiException(HttpException http, LostStatus status) {
        super("Exception in Lost API: HTTP Code " + http.code() + ", status: " + status, http);
        this.code = http.code();
        this.statusString = status.status;
    }

    public int getCode() {
        return code;
    }

    public String getStatusString() {
        return statusString;
    }
}

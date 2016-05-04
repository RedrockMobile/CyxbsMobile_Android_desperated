package com.mredrock.cyxbsmobile.network.exception;

/**
 * Created by cc on 16/3/19.
 */
public class RedrockApiException extends RuntimeException {
    public RedrockApiException() {
    }

    public RedrockApiException(String detailMessage) {
        super(detailMessage);
    }

    public RedrockApiException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public RedrockApiException(Throwable throwable) {
        super(throwable);
    }
}

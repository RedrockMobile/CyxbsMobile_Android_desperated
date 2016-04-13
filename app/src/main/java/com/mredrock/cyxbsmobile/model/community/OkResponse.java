package com.mredrock.cyxbsmobile.model.community;

/**
 * Created by mathiasluo on 16-4-12.
 */
public class OkResponse {

    /**
     * state : 200
     * info : success
     */
public static final int RESPONSE_OK = 200;
    private int state;
    private String info;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}

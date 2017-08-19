package com.mredrock.freshmanspecial.beans;

/**
 * Created by Jay on 2017/8/17.
 */

public class HttpResult<T> {

    /**
     * Status : 200
     * Info : success
     * Version : 1.0
     */

    private int Status;
    private String Info;
    private String Version;
    private T Data;

    public T getData() {
        return Data;
    }

    public void setData(T data) {
        Data = data;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int Status) {
        this.Status = Status;
    }

    public String getInfo() {
        return Info;
    }

    public void setInfo(String Info) {
        this.Info = Info;
    }

    public String getVersion() {
        return Version;
    }

    public void setVersion(String Version) {
        this.Version = Version;
    }
}

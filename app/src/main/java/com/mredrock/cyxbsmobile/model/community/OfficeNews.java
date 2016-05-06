package com.mredrock.cyxbsmobile.model.community;

import java.util.List;

/**
 * Created by mathiasluo on 16-4-22.
 */
public class OfficeNews {
    private int status;
    private String page;
    private List<ContentBean> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public List<ContentBean> getData() {
        return data;
    }

    public void setData(List<ContentBean> data) {
        this.data = data;
    }
}

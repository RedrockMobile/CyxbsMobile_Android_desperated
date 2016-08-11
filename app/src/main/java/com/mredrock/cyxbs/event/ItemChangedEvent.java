package com.mredrock.cyxbs.event;


import com.mredrock.cyxbs.model.social.HotNews;

/**
 * Created by  : ACEMURDER
 * Created at  : 16/8/11.
 * Created for : CyxbsMobile_Android
 */
public class ItemChangedEvent {
    private String num;
    private String articleId;
    private boolean isMyLike;

    public ItemChangedEvent(String num, String articleId,boolean isMyLike) {
        this.num = num;
        this.articleId = articleId;
        this.isMyLike = isMyLike;
    }

    public String getNum() {
        return num;
    }

    public String getArticleId() {
        return articleId;
    }

    public boolean isMyLike() {
        return isMyLike;
    }
}
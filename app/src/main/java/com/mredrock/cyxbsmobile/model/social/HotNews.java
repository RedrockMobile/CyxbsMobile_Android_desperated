package com.mredrock.cyxbsmobile.model.social;

import com.mredrock.cyxbsmobile.model.RedrockApiWrapper;

import java.util.List;

/**
 * Created by mathiasluo on 16-4-5.
 */
public class HotNews extends RedrockApiWrapper<HotNewsContent> {


    public String page;

    public HotNews(PersonLatest personLatest, String userId, String userName, String userHead) {
        this.data = new HotNewsContent(BBDDNews.BBDD + ""
                , personLatest.id
                , BBDDNews.BBDD
                , userId
                , userName
                , userHead
                , personLatest.createdTime
                , new OfficeNewsContent(personLatest.content)
                , new HotNewsContent.ImgBean(personLatest.thumbnailPhoto, personLatest.photo)
                , personLatest.likeNum
                , personLatest.remarkNum
                , true);
    }

    public HotNews(OfficeNewsContent officeNewsContent) {
        this.data = new HotNewsContent(officeNewsContent);
    }

    public HotNews(BBDDNewsContent bbddNewsContent) {
        this.data = new HotNewsContent(bbddNewsContent.type_id
                , bbddNewsContent.id
                , 5
                , bbddNewsContent.stunum
                , bbddNewsContent.nickname
                , bbddNewsContent.photo_src
                , bbddNewsContent.created_time
                , new OfficeNewsContent(bbddNewsContent.content)
                , new HotNewsContent.ImgBean(bbddNewsContent.article_thumbnail_src, bbddNewsContent.article_photo_src)
                , bbddNewsContent.like_num
                , bbddNewsContent.remark_num
                , bbddNewsContent.is_my_like);
    }

    public HotNews(String content, List<Image> list) {
        list.remove(0);
        String a = "";
        String b = "";
        for (Image image : list) {
            a += image.url + ",";
            b += image.url + ",";

        }
        this.data = new HotNewsContent(new HotNewsContent.ImgBean(a, b), new OfficeNewsContent(content));
    }


}

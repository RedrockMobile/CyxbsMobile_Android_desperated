package com.mredrock.cyxbsmobile.model.social;

import android.os.Parcel;
import android.os.Parcelable;

import com.mredrock.cyxbsmobile.model.RedrockApiWrapper;

import java.util.List;

/**
 * Created by mathiasluo on 16-4-5.
 */
public class HotNews extends RedrockApiWrapper<HotNewsContent> implements Parcelable {


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
                , true
                , personLatest.id);
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
                , bbddNewsContent.is_my_like
                , bbddNewsContent.id);
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

    public HotNews(BBDDDetail bbddDetail) {
        this.data = new HotNewsContent("bbdd"
                , bbddDetail.id
                , 5
                , ""
                , ""
                , bbddDetail.photo_src
                , bbddDetail.created_time
                , new OfficeNewsContent(bbddDetail.content)
                , new HotNewsContent.ImgBean(bbddDetail.thumbnail_src, bbddDetail.photo_src)
                , bbddDetail.like_num
                , bbddDetail.remark_num
                , false
                , bbddDetail.id);
    }

    protected HotNews(Parcel in) {
        page = in.readString();
    }

    public static final Creator<HotNews> CREATOR = new Creator<HotNews>() {
        @Override
        public HotNews createFromParcel(Parcel in) {
            return new HotNews(in);
        }

        @Override
        public HotNews[] newArray(int size) {
            return new HotNews[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(page);
    }
}

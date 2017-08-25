package com.mredrock.cyxbs.model.social;

import android.os.Parcel;
import android.os.Parcelable;

import com.mredrock.cyxbs.model.RedrockApiWrapper;
import com.mredrock.cyxbs.util.Utils;

import java.util.List;

/**
 * Created by mathiasluo on 16-4-5.
 */
public class HotNews extends RedrockApiWrapper<HotNewsContent> implements Parcelable {

    public String page;

    public HotNews(PersonLatest personLatest, String userId, String userName, String userHead) {
        this.data = new HotNewsContent(personLatest.typeId
                , personLatest.id
                , Integer.valueOf(personLatest.typeId)
                , userId
                , userName
                , userHead
                , personLatest.createdTime
                , new OfficeNewsContent(personLatest.content)
                , new HotNewsContent.ImgBean(personLatest.thumbnailPhoto, personLatest.photo)
                , personLatest.likeNum
                , personLatest.remarkNum
                , personLatest.isMyLike
                , personLatest.id);
    }

    public HotNews(OfficeNewsContent officeNewsContent) {
        this.data = new HotNewsContent(officeNewsContent);
    }

    public HotNews(BBDDNewsContent bbddNewsContent) {
        this.data = new HotNewsContent(bbddNewsContent.typeId
                , bbddNewsContent.id
                , Integer.valueOf(bbddNewsContent.typeId)
                , bbddNewsContent.stuNum
                , bbddNewsContent.nickName
                , bbddNewsContent.photoSrc
                , bbddNewsContent.createdTime
                , new OfficeNewsContent(bbddNewsContent.content)
                , new HotNewsContent.ImgBean(bbddNewsContent.articleThumbnailSrc, bbddNewsContent.articlePhotoSrc)
                , bbddNewsContent.likeNum
                , bbddNewsContent.remarkNum
                , bbddNewsContent.isMyLike
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
                , Integer.parseInt(bbddDetail.typeId)
                , ""
                , Utils.checkNotNullWithDefaultValue(bbddDetail.nickName, "")
                , Utils.checkNotNullWithDefaultValue(bbddDetail.userHead, "")
                , Utils.checkNotNullWithDefaultValue(bbddDetail.createdTime != null ? bbddDetail.createdTime : bbddDetail.date, "")
                , new OfficeNewsContent(bbddDetail.content)
                , new HotNewsContent.ImgBean(Utils.checkNotNullWithDefaultValue(bbddDetail.thumbnailSrc, ""), Utils.checkNotNullWithDefaultValue(bbddDetail.photoSrc, ""))
                , bbddDetail.likeNum
                , bbddDetail.remarkNum
                , bbddDetail.isMyLike
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

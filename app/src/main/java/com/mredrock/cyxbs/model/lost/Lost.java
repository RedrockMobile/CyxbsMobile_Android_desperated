package com.mredrock.cyxbs.model.lost;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Model of the single item of lost and found API <br>
 * http://hongyan.cqupt.edu.cn/laf/api/view/lost/all/1
 *
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

public class Lost implements Serializable {

    @SerializedName("pro_id")
    public int id;   // id, unique in items
    @SerializedName("pro_name")
    public String category;  // category, such as "钱包", "一卡通" or "其他", which listed in {@link R.array.lost_category_list}
    @SerializedName("connect_name")
    public String connectName;  // the name of the owner of this message item
    @SerializedName("connect_wx")
    public String connectWx;    // the qq number
    @SerializedName("wx_avatar")
    public String avatar;     // the WeChat avatar, a url of a JPEG image (40KB).
    @SerializedName("pro_description")
    public String description;  // the external description
    @SerializedName("created_at")
    public String createdAt;    // created time

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Lost && this.id == ((Lost) obj).id;
    }

    @Override
    public int hashCode() {
        return -32094398 + id;
    }
}

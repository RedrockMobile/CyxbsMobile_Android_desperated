package com.mredrock.cyxbs.model.lost;

import com.google.gson.annotations.SerializedName;

/**
 * Model of the detail of Lost and Found API <br>
 *      http://hongyan.cqupt.edu.cn/laf/api/detail/458
 *
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

public class LostDetail extends Lost {

    @SerializedName("L_or_F_time")
    public String time; // when lost or found
    @SerializedName("L_or_F_place")
    public String place;    // where lost or found
    @SerializedName("connect_phone")
    public String connectPhone; // phone number

    /**
     * marge external detail information with origin lost item
     * @param l origin lost item
     * @return a full lost detail item
     */
    public LostDetail mergeLost(Lost l) {
        this.id = l.id;
        this.category = l.category;
        this.connectName = l.connectName;
        this.connectWx = l.connectWx;
        this.avatar = l.avatar;
        this.description = l.description;
        this.createdAt = l.createdAt;
        return this;
    }

}

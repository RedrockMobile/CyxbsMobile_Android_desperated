package com.mredrock.cyxbs.freshman.bean;

import com.google.gson.annotations.SerializedName;

/**
 * 接口2
 * 性别比例
 */
public class SexRatio {

    /**
     * female_amount : 381
     * male_amount : 1372
     */

    @SerializedName("female_amount")
    private int femaleAmount;
    @SerializedName("male_amount")
    private int maleAmount;

    public int getFemaleAmount() {
        return femaleAmount;
    }

    public void setFemaleAmount(int femaleAmount) {
        this.femaleAmount = femaleAmount;
    }

    public int getMaleAmount() {
        return maleAmount;
    }

    public void setMaleAmount(int maleAmount) {
        this.maleAmount = maleAmount;
    }
}

package com.excitingboat.freshmanspecial.model.bean;

/**
 * Created by PinkD on 2016/8/11.
 * PlaceWithIntroduction
 */
public class PlaceWithIntroduction extends Place {
    private String introduction;

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    @Override
    public String toString() {
        return super.toString() +
                "introduction='" + introduction;
    }
}

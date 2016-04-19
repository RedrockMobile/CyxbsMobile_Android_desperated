package com.mredrock.cyxbsmobile.model;

import java.util.List;

/**
 * Created by Stormouble on 15/12/12.
 */
public class EmptyRoom {
    /**
     * 楼层
     */
    private String floor;

    /**
     * 对应楼层的教室数
     */
    private List<String> emptyRooms;

    public EmptyRoom() {

    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public List<String> getEmptyRooms() {
        return emptyRooms;
    }

    public void setEmptyRooms(List<String> emptyRooms) {
        this.emptyRooms = emptyRooms;
    }
}

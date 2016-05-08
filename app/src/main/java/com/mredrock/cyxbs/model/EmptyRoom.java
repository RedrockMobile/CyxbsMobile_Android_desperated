package com.mredrock.cyxbs.model;

import java.util.List;

/**
 * Created by Stormouble on 15/12/12.
 */
public class EmptyRoom {

    private String floor;

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

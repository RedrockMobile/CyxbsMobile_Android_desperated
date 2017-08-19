package com.mredrock.cyxbs.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Stormouble on 15/12/12.
 */
public class EmptyRoom implements Parcelable {

    private String floor;

    private List<String> emptyRooms;

    public EmptyRoom() {

    }

    protected EmptyRoom(Parcel in) {
        floor = in.readString();
        emptyRooms = in.createStringArrayList();
    }

    public static final Creator<EmptyRoom> CREATOR = new Creator<EmptyRoom>() {
        @Override
        public EmptyRoom createFromParcel(Parcel in) {
            return new EmptyRoom(in);
        }

        @Override
        public EmptyRoom[] newArray(int size) {
            return new EmptyRoom[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(floor);
        dest.writeStringList(emptyRooms);
    }
}

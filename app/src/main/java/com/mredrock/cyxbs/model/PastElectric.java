package com.mredrock.cyxbs.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ：AceMurder
 * Created on ：2017/4/3
 * Created for : CyxbsMobile_Android.
 * Enjoy it !!!
 */

public class PastElectric {
    private int time;
    private int spend;
    @SerializedName("elec_start")
    private String electricStart;
    @SerializedName("elec_end")
    private String electricEnd;

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getSpend() {
        return spend;
    }

    public void setSpend(int spend) {
        this.spend = spend;
    }

    public String getElectricStart() {
        return electricStart;
    }

    public void setElectricStart(String electricStart) {
        this.electricStart = electricStart;
    }

    public String getElectricEnd() {
        return electricEnd;
    }

    public void setElectricEnd(String electricEnd) {
        this.electricEnd = electricEnd;
    }


    public static class Result{
        ElectricCharge current;
        List<PastElectric> trend;

        public ElectricCharge getCurrent() {
            return current;
        }

        public void setCurrent(ElectricCharge current) {
            this.current = current;
        }

        public List<PastElectric> getTrend() {
            return trend;
        }

        public void setTrend(List<PastElectric> trend) {
            this.trend = trend;
        }
    }


    public static class PastElectricResultWrapper{
        private String building;
        private String room;
        private Result result;


        public String getBuilding() {
            return building;
        }

        public void setBuilding(String building) {
            this.building = building;
        }

        public String getRoom() {
            return room;
        }

        public void setRoom(String room) {
            this.room = room;
        }

        public Result getResult() {
            return result;
        }

        public void setResult(Result result) {
            this.result = result;
        }
    }
}

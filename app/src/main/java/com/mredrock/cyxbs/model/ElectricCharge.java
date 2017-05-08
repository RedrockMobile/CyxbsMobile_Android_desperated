package com.mredrock.cyxbs.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ：AceMurder
 * Created on ：11/01/2017
 * Created for : CyxbsMobile_Android.
 * Enjoy it !!!
 */

public class ElectricCharge {


    /**
     * elec_spend : 58
     * elec_cost : ["15","12"]
     * record_time : 12月15日
     * elec_start : 3341
     * elec_end : 3399
     * elec_free : 30
     * elec_month : 01
     */

    @SerializedName("elec_spend")
    private String electricSpend;
    @SerializedName("record_time")
    private String recordTime;
    @SerializedName("elec_start")
    private String electricStart;
    @SerializedName("elec_end")
    private String electricEnd;
    @SerializedName("elec_free")
    private String electricFree;
    @SerializedName("elec_month")
    private String electricMonth;
    @SerializedName("elec_cost")
    private List<String> electricCost;

    public String getElectricSpend() {
        return electricSpend;
    }

    public void setElectricSpend(String electricSpend) {
        this.electricSpend = electricSpend;
    }

    public String getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(String recordTime) {
        this.recordTime = recordTime;
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

    public String getElectricFree() {
        return electricFree;
    }

    public void setElectricFree(String electricFree) {
        this.electricFree = electricFree;
    }

    public String getElectricMonth() {
        return electricMonth;
    }

    public void setElectricMonth(String electricMonth) {
        this.electricMonth = electricMonth;
    }

    public List<String> getElectricCost() {
        return electricCost;
    }

    public void setElectricCost(List<String> electricCost) {
        this.electricCost = electricCost;
    }


    public static class ElectricChargeWrapper{
        private int status;

        @SerializedName("elec_inf")
        private ElectricCharge electricCharge;


        public ElectricCharge getElectricCharge() {
            return electricCharge;
        }

        public void setElectricCharge(ElectricCharge electricCharge) {
            this.electricCharge = electricCharge;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }





}

package com.mredrock.cyxbs.event;

/**
 * Created by ：AceMurder
 * Created on ：2017/2/27
 * Created for : CyxbsMobile_Android.
 * Enjoy it !!!
 */

public class ForceFetchCourseEvent {
    private int currentWeek;

    public ForceFetchCourseEvent(int currentWeek) {
        this.currentWeek = currentWeek;
    }

    public int getCurrentWeek() {
        return currentWeek;
    }

    public void setCurrentWeek(int currentWeek) {
        this.currentWeek = currentWeek;
    }
}

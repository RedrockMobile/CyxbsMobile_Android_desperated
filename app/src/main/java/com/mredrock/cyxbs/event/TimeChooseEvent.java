package com.mredrock.cyxbs.event;

import com.mredrock.cyxbs.component.widget.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ：AceMurder
 * Created on ：2016/11/12
 * Created for : CyxbsMobile_Android.
 * Enjoy it !!!
 */

public class TimeChooseEvent {
    private ArrayList<Position> positions = new ArrayList<>();


    public TimeChooseEvent(ArrayList<Position> positions) {
        this.positions = positions;
    }

    public ArrayList<Position> getPositions() {
        return positions;
    }
}

package com.mredrock.cyxbs.network.func;

import com.mredrock.cyxbs.model.Affair;

import java.util.List;

import rx.functions.Func1;

/**
 * Created by simonla on 2016/11/16.
 * 下午3:20
 */

public class AffairWeekFilterFunc implements Func1<List<Affair>, List<Affair>> {

    private int mWeek;

    public AffairWeekFilterFunc(int week) {
        mWeek = week;
    }

    @Override
    public List<Affair> call(List<Affair> affairs) {
        for (Affair a : affairs) {
            if (!a.week.contains(mWeek)) {
                affairs.remove(a);
            }
        }
        return affairs;
    }
}

package com.mredrock.cyxbs.network.func;

import com.mredrock.cyxbs.model.Affair;

import java.util.List;

import io.reactivex.functions.Function;


/**
 * Created by simonla on 2016/11/16.
 * 下午3:20
 */

public class AffairWeekFilterFunc implements Function<List<Affair>, List<Affair>> {

    private int mWeek;

    public AffairWeekFilterFunc(int week) {
        mWeek = week;
    }

    @Override
    public List<Affair> apply(List<Affair> affairs) throws Exception {
        for (Affair a : affairs) {
            if (!a.week.contains(mWeek)) {
                affairs.remove(a);
            }
        }
        return affairs;
    }
}

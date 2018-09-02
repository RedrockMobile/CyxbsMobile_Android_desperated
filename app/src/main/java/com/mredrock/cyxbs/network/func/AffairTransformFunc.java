package com.mredrock.cyxbs.network.func;

import com.mredrock.cyxbs.model.Affair;
import com.mredrock.cyxbs.model.AffairApi;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Function;


/**
 * Created by ：AceMurder
 * Created on ：2016/11/14
 * Created for : CyxbsMobile_Android.
 * Enjoy it !!!
 */

public class AffairTransformFunc implements Function<AffairApi<List<AffairApi.AffairItem>>, List<Affair>> {

    @Override
    public List<Affair> apply(AffairApi<List<AffairApi.AffairItem>> listAffairApi) throws Exception {
        ArrayList<Affair> affairs = new ArrayList<>();
        for (AffairApi.AffairItem item : listAffairApi.data) {
            for (AffairApi.AffairItem.DateBean date : item.getDate()) {
                Affair affair = new Affair();
                affair.uid = item.getId();
                affair.hash_day = date.getDay();
                affair.hash_lesson = date.getClassX();
                affair.course = item.getTitle();
                affair.period = 2;
                affair.week = date.getWeek();
                affair.courseType = 2;
                affair.teacher = item.getContent();
                affair.classroom = "  ";
                affair.begin_lesson = 2 * date.getClassX() + 1;
                affair.type = "提醒";
                affair.time = item.getTime();
                affair.rawWeek = " ";
                affairs.add(affair);
            }
        }
        return affairs;
    }
}

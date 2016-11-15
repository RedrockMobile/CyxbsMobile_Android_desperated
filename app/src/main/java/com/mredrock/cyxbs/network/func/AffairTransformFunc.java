package com.mredrock.cyxbs.network.func;

import com.mredrock.cyxbs.config.Config;
import com.mredrock.cyxbs.model.Affair;
import com.mredrock.cyxbs.model.Course;
import com.mredrock.cyxbs.model.AffairApi;
import com.mredrock.cyxbs.model.social.Image;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import rx.functions.Func1;



/**
 * Created by ：AceMurder
 * Created on ：2016/11/14
 * Created for : CyxbsMobile_Android.
 * Enjoy it !!!
 */

public class AffairTransformFunc implements Func1<AffairApi<List<AffairApi.AffairItem>>,List<Affair>> {

    @Override
    public List<Affair> call(AffairApi<List<AffairApi.AffairItem>> listAffairApi) {
        ArrayList<Affair> affairs = new ArrayList<>();
        for (AffairApi.AffairItem item : listAffairApi.data){

            Affair affair = new Affair();
            affair.uid = item.getId();
            affair.hash_day = item.getDay();
            affair.hash_lesson = item.getClassX();
            affair.course = item.getTitle();
            affair.period = 2;
            affair.week = item.getWeek();
            affair.courseType = 2;
            affair.teacher = item.getContent();
            affair.classroom = "  ";
            affair.begin_lesson = 2 * item.getClassX() + 1;
            affair.type = "提醒";
            affair.time = item.getTime();
            affair.rawWeek = " ";
            affairs.add(affair);
        }

        return affairs;
    }
}

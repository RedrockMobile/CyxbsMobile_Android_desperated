package com.mredrock.cyxbs.network.func;

import com.mredrock.cyxbs.model.StartPage;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.functions.Function;


/**
 * Created by simonla on 2016/12/29.
 * 上午10:55
 */

public class StartPageFunc implements Function<List<StartPage>, StartPage> {

    @Override
    public StartPage apply(List<StartPage> startPages) throws Exception {
        for (StartPage s : startPages) {
            Date date;
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", new Locale("zh"));
            try {
                long now = System.currentTimeMillis() / 1000;
                date = dateFormat.parse(s.getStart());
                if (Math.abs(date.getTime() / 1000 - now) < 24 * 60 * 60) {
                    return s;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}

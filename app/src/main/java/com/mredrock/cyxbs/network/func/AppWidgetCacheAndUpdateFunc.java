package com.mredrock.cyxbs.network.func;

import com.google.gson.Gson;
import com.mredrock.cyxbs.APP;
import com.mredrock.cyxbs.config.Config;
import com.mredrock.cyxbs.model.Course;
import com.mredrock.cyxbs.ui.widget.CourseListAppWidget;
import com.mredrock.cyxbs.util.FileUtils;
import com.mredrock.cyxbs.util.SchoolCalendar;

import java.io.File;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import rx.functions.Func1;

/**
 * Manage cache file for {@link CourseListAppWidget} and call its update
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

public class AppWidgetCacheAndUpdateFunc implements Func1<List<Course>, List<Course>> {

    @Override
    public List<Course> call(List<Course> courses) {
        List<Course> weekCourses = new UserCourseFilterFunc(new SchoolCalendar().getWeekOfTerm()).call(courses);
        List<Course> dayCourses = new UserCourseFilterByWeekDayFunc(new GregorianCalendar().get(Calendar.DAY_OF_WEEK)).call(weekCourses);
        // List<Course> dayCourses = new UserCourseFilterByWeekDayFunc(Calendar.THURSDAY).call(weekCourses);
        FileUtils.writeStringToFile(new Gson().toJson(dayCourses), new File(APP.getContext().getFilesDir().getAbsolutePath() + "/" + Config.APP_WIDGET_CACHE_FILE_NAME));
        CourseListAppWidget.updateNow(APP.getContext());
        return courses;
    }

    public static void deleteCache() {
        try {
            //noinspection ResultOfMethodCallIgnored
            new File(APP.getContext().getFilesDir().getAbsolutePath() + "/" + Config.APP_WIDGET_CACHE_FILE_NAME).delete();
            CourseListAppWidget.updateNow(APP.getContext());
        } catch (Exception ignored) {

        }
    }

}

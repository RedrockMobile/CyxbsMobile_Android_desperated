package com.mredrock.cyxbs.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.component.widget.ScheduleView;
import com.mredrock.cyxbs.config.Const;
import com.mredrock.cyxbs.model.Course;
import com.mredrock.cyxbs.ui.activity.social.TopicArticleActivity;
import com.mredrock.cyxbs.ui.widget.CourseListAppWidget;
import com.mredrock.cyxbs.util.LogUtils;

import java.util.ArrayList;

/**
 * 负责处理和分发各种 action
 * <p>避免重新启动已经在运行的 activity 造成的 getIntent 不更新问题</p>
 * <p>这个 Activity 启动后必须马上 finish ，而且不应该有界面</p>
 *
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

public class ActionActivity extends AppCompatActivity {

    private static final String TAG = ActionActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        try {
            checkMatch(checkNull(intent));
            checkMatch(checkTopicArticle(intent));
            checkMatch(checkCourseListAppWidget(intent));
            startActivity(SplashActivity.class);
        } catch (ActionMatchException e) {
            // do nothing
        } catch (Exception e) {
            LogUtils.LOGE(TAG, "error in action match test", e);
        }
        finish();
    }

    private boolean checkNull(Intent intent) {
        if (intent == null) {
            startActivity(SplashActivity.class);
            return true;
        }
        return false;
    }

    private boolean checkTopicArticle(Intent intent) {
        if (intent.getData() != null) {
            Uri uri = intent.getData();
            if (Const.CyxbsUri.SCHEME.equals(uri.getScheme()) && Const.CyxbsUri.HOST_TOPIC.equals(uri.getHost())) {
                String path = uri.getPath();
                int topicId = Integer.parseInt(path.split("/")[1]);
                TopicArticleActivity.start(this, topicId);
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("RestrictedApi")
    private boolean checkCourseListAppWidget(Intent intent) {
        String action = intent.getAction();
        if (action != null && getString(R.string.action_appwidget_item_on_click).equals(action)) {
            ArrayList<Course> courses = intent.getParcelableArrayListExtra(CourseListAppWidget.EXTRA_COURSES);
            if (courses != null && courses.size() != 0) {
                ScheduleView.CourseList courseList = new ScheduleView.CourseList();
                courseList.list = courses;
                courseListToShow = courseList;
                startActivity(MainActivity.class);
                return true;
            }
        }
        return false;
    }

    private static ScheduleView.CourseList courseListToShow;

    public static ScheduleView.CourseList getCourseListToShow() {
        ScheduleView.CourseList courseList = courseListToShow;
        courseListToShow = null;
        return courseList;
    }

    private void startActivity(Class<? extends Activity> activityClazz) {
        startActivity(new Intent(this, activityClazz));
    }

    private void checkMatch(boolean b) {
        if (b) {
            throw new ActionMatchException();
        }
    }

    private class ActionMatchException extends RuntimeException {
    }
}

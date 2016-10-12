package com.mredrock.cyxbs.network.func;

import com.mredrock.cyxbs.APP;
import com.mredrock.cyxbs.ui.widget.CourseListAppWidget;

import rx.functions.Func1;

/**
 * Send App Widget request with some action from Rx
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

public class UpdateAppWidgetFunc<T> implements Func1<T, T> {

    private boolean needUpdate;

    public UpdateAppWidgetFunc(boolean hasUpdate) {
        this.needUpdate = hasUpdate;
    }

    @Override
    public T call(T t) {
        if (needUpdate) {
            CourseListAppWidget.updateNow(APP.getContext());
        }
        return t;
    }
}

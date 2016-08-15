package com.excitingboat.freshmanspecial.view.iview;

import java.util.List;

/**
 * Created by PinkD on 2016/8/4.
 * Interface getInformation
 */
public interface IGetInformation<T> {
    void requestSuccess(List<T> list);

    void requestFail();
}

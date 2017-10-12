package com.mredrock.cyxbs.network.func;

import com.mredrock.cyxbs.BaseAPP;
import com.mredrock.cyxbs.model.User;
import com.mredrock.cyxbs.ui.activity.me.EditNickNameActivity;

import rx.functions.Func1;

/**
 * Created by cc on 16/5/6.
 */
public class UserInfoVerifyFunc implements Func1<User, User>{
    @Override
    public User call(User user) {
        if (user == null) {
            EditNickNameActivity.start(BaseAPP.getContext());
        }
        return user;
    }
}

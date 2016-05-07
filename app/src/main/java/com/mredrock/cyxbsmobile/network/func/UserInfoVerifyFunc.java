package com.mredrock.cyxbsmobile.network.func;

import com.mredrock.cyxbsmobile.model.User;
import com.mredrock.cyxbsmobile.network.exception.UnsetUserInfoException;

import rx.functions.Func1;

/**
 * Created by cc on 16/5/6.
 */
public class UserInfoVerifyFunc implements Func1<User, User>{
    @Override
    public User call(User user) {
        if (user == null) {
            throw new UnsetUserInfoException("同学你还没有完善信息哦");
        }
        return user;
    }
}

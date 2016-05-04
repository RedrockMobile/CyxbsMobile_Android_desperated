package com.mredrock.cyxbsmobile.util;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by Stormouble on 16/4/22.
 */
public class FragmentUtils {

    public static void startAnotherFragment(@NonNull FragmentManager fragmentManager,
                                            @NonNull Fragment fromFragment,
                                            @NonNull Fragment toFragment, int frameId) {
        Utils.checkNotNullWithException(fragmentManager);
        Utils.checkNotNullWithException(fromFragment);
        Utils.checkNotNullWithException(toFragment);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.add(frameId,toFragment, toFragment.getClass().getName());
        transaction.hide(fromFragment);
        transaction.addToBackStack(toFragment.getClass().getName());
        transaction.commit();
    }
}

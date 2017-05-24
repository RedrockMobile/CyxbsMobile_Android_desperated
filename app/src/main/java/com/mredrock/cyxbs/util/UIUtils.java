package com.mredrock.cyxbs.util;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by Stormouble on 16/4/26.
 */
public class UIUtils {

    public static void addFragmentToActivity(@NonNull FragmentManager fragmentManager,
                                             @NonNull Fragment fragment, int frameId) {
        Utils.checkNotNullWithException(fragmentManager);
        Utils.checkNotNullWithException(fragment);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(frameId, fragment, fragment.getClass().getName());
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        transaction.addToBackStack(fragment.getClass().getName());
        transaction.commit();
    }

    public static void startAnotherFragment(@NonNull FragmentManager fragmentManager,
                                            @NonNull Fragment fromFragment,
                                            @NonNull Fragment toFragment, int frameId) {
        Utils.checkNotNullWithException(fragmentManager);
        Utils.checkNotNullWithException(fromFragment);
        Utils.checkNotNullWithException(toFragment);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.add(frameId, toFragment, toFragment.getClass().getName());
        transaction.hide(fromFragment);
        transaction.addToBackStack(toFragment.getClass().getName());
        transaction.commit();
    }

    private UIUtils() {
    }

}

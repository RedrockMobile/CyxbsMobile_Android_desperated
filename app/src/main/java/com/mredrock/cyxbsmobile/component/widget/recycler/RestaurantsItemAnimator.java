package com.mredrock.cyxbsmobile.component.widget.recycler;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.animation.DecelerateInterpolator;

import com.mredrock.cyxbsmobile.ui.adapter.FoodListAdapter;
import com.mredrock.cyxbsmobile.util.LogUtils;

/**
 * Created by Stormouble on 16/4/24.
 */
public class RestaurantsItemAnimator extends DefaultItemAnimator{

    private static final String TAG = LogUtils.makeLogTag(RestaurantsItemAnimator.class);

    private static final long ANIMATION_DURATION = 700;

    private static final long ANIMATION_DELAY_FACTOR = 20;

    @Override
    public boolean canReuseUpdatedViewHolder(@NonNull RecyclerView.ViewHolder viewHolder) {
        return true;
    }

    @Override
    public boolean animateAdd(RecyclerView.ViewHolder holder) {
        runEnterAnimation((FoodListAdapter.RestaurantsViewHolder) holder);
        dispatchAddFinished(holder);
        return false;
    }

    private void runEnterAnimation(FoodListAdapter.RestaurantsViewHolder holder) {
        holder.itemView.setTranslationY(100);
        holder.itemView.setAlpha(0.f);
        holder.itemView.animate()
                .translationY(0.f)
                .alpha(1.f)
                .setStartDelay(ANIMATION_DELAY_FACTOR * holder.getLayoutPosition())
                .setInterpolator(new DecelerateInterpolator(2.f))
                .setDuration(ANIMATION_DURATION)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        dispatchAddFinished(holder);
                    }
                })
                .start();
    }
}

package com.mredrock.cyxbsmobile.component.widget.recycler;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.animation.DecelerateInterpolator;

import com.mredrock.cyxbsmobile.ui.adapter.AroundDishesAdapter;
import com.mredrock.cyxbsmobile.util.DensityUtils;

import java.util.List;

/**
 * Created by Stormouble on 16/4/24.
 */
public class AroundDishesItemAnimator extends DefaultItemAnimator{

    @Override
    public boolean canReuseUpdatedViewHolder(@NonNull RecyclerView.ViewHolder viewHolder) {
        return true;

    }

    @NonNull
    @Override
    public ItemHolderInfo recordPreLayoutInformation(@NonNull RecyclerView.State state,
                                                     @NonNull RecyclerView.ViewHolder viewHolder,
                                                     int changeFlags, @NonNull List<Object> payloads) {
        return super.recordPreLayoutInformation(state, viewHolder, changeFlags, payloads);
    }

    @Override
    public boolean animateAdd(RecyclerView.ViewHolder holder) {
        runEnterAnimation((AroundDishesAdapter.AroundDishesViewHolder) holder);
        dispatchAddFinished(holder);
        return false;
    }

    private void runEnterAnimation(AroundDishesAdapter.AroundDishesViewHolder holder) {
        final int screenHeight = DensityUtils.getScreenHeight(holder.itemView.getContext());
        holder.itemView.setTranslationY(screenHeight);
        holder.itemView.animate()
                .translationY(0)
                .setInterpolator(new DecelerateInterpolator(3f))
                .setDuration(700)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        dispatchAddFinished(holder);
                    }
                })
                .start();
    }
}

package com.mredrock.cyxbsmobile.component.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mredrock.cyxbsmobile.R;
import com.mredrock.cyxbsmobile.model.RestaurantComment;
import com.mredrock.cyxbsmobile.util.SchoolCalendar;

import java.util.List;

public class CommentView extends LinearLayout {

    private LinearLayout mContainer;

    public CommentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        inflate(getContext(), R.layout.layout_restaurant_comment, this);
        mContainer = (LinearLayout) findViewById(R.id.container);
    }

    public void setData(List<RestaurantComment> comments) {
        mContainer.removeAllViews();
        LayoutInflater mInflater = LayoutInflater.from(getContext());
        if (comments != null) {
            for (int i = 0; i < comments.size(); i++) {
                View itemView = mInflater.inflate(R.layout.layout_restaurant_comment_item, mContainer, false);
                TextView content = (TextView) itemView.findViewById(R.id.content);
                TextView date = (TextView) itemView.findViewById(R.id.date);
                TextView author = (TextView) itemView.findViewById(R.id.author);
                content.setText(comments.get(i).comment_content.replaceAll("(\r?\n(\\s*\r?\n)+)", ""));
                date.setText(new SchoolCalendar(Long.parseLong(comments.get(i).comment_date)).getString("yyyy年MM月dd日HH:mm"));
                author.setText(comments.get(i).comment_author_name);
                itemView.setTag(comments.get(i).comment_id);
                mContainer.addView(itemView);
            }

        }
    }

}

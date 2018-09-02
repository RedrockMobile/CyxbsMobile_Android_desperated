package com.mredrock.cyxbs.model;

import android.support.annotation.NonNull;

import com.mredrock.cyxbs.util.Utils;

/**
 * Created by Stormouble on 16/4/12.
 */
public class FoodComment implements Comparable<FoodComment> {
    public String comment_id;
    public String comment_content;
    public String comment_date;
    public String comment_author_name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (null == o || getClass() != o.getClass()) return false;
        FoodComment comment = (FoodComment) o;
        return Utils.equal(comment_id, comment.comment_id)
                && Utils.equal(comment_content, comment.comment_content)
                && Utils.equal(comment_date, comment.comment_date)
                && Utils.equal(comment_author_name, comment.comment_author_name);
    }

    @Override
    public int hashCode() {
        return Utils.hashCode(comment_id, comment_content,
                comment_date, comment_author_name);
    }

    @Override
    public String toString() {
        return "author " + comment_author_name + "with content " + comment_content
                + ", publish at" + comment_date;
    }


    @Override
    public int compareTo(@NonNull FoodComment another) {
        return Integer.valueOf(another.comment_id)
                .compareTo(Integer.valueOf(comment_id));
    }
}

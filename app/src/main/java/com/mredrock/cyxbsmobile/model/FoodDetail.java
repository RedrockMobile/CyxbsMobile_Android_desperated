package com.mredrock.cyxbsmobile.model;

import com.mredrock.cyxbsmobile.util.Utils;

import java.util.Arrays;
import java.util.List;

/**
 * Detail info of restaurant
 * <p>
 * Created by Stormouble on 16/4/12.
 */
public class FoodDetail {

    public String shop_id;
    public String shop_name;
    public String shop_address;
    public String shop_tel;
    public String shop_content;
    public String shop_sale_content;
    public String[] shop_image;
    public int page_count;

    public List<FoodComment> foodComments;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (null == o || getClass() != o.getClass()) return false;
        FoodDetail foodDetail = (FoodDetail) o;
        return Utils.equal(shop_id, foodDetail.shop_id)
                && Utils.equal(shop_name, foodDetail.shop_name)
                && Utils.equal(shop_address, foodDetail.shop_address)
                && Utils.equal(shop_tel, foodDetail.shop_tel)
                && Utils.equal(shop_content, foodDetail.shop_content)
                && Utils.equal(shop_sale_content, foodDetail.shop_sale_content)
                && Utils.equal(Arrays.toString(shop_image), Arrays.toString(shop_image))
                && Utils.equal(page_count, foodDetail.page_count);
    }

    @Override
    public int hashCode() {
        return Utils.hashCode(shop_id, shop_name, shop_address, shop_tel
                , shop_content, shop_sale_content, Arrays.toString(shop_image), page_count);
    }

    @Override
    public String toString() {
        return "Shop " + shop_name + " with img " + Arrays.toString(shop_image);
    }
}

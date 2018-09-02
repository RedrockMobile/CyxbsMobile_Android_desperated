package com.mredrock.cyxbs.model;

import com.mredrock.cyxbs.util.Utils;

/**
 * Created by Stormouble on 16/4/7.
 */
public class Food {

    public int page;
    public String id;
    public String name;
    public String shop_address;
    public String shopimg_src;

    public String introduction;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (null == o || getClass() != o.getClass()) return false;
        Food food = (Food) o;
        return Utils.equal(id, food.id)
                && Utils.equal(name, food.name)
                && Utils.equal(shop_address, food.shop_address)
                && Utils.equal(shopimg_src, food.shopimg_src)
                && Utils.equal(introduction, food.introduction);
    }

    @Override
    public int hashCode() {
        return Utils.hashCode(id, name, shop_address, shopimg_src, introduction);
    }

    @Override
    public String toString() {
        return "Shop " + name + ", located in " + shop_address + "with img url " + shopimg_src;
    }
}

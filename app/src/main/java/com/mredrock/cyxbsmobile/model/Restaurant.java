package com.mredrock.cyxbsmobile.model;

import com.mredrock.cyxbsmobile.util.Utils;

import java.io.Serializable;
import java.util.List;

/**
 * result of around food
 *
 * Created by Stormouble on 16/4/7.
 */
public class Restaurant {

	public int page;
	public String id;
	public String name;
	public String shop_address;
	public String shopimg_src;
	public String content;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (null == o || getClass() != o.getClass()) return false;
		Restaurant restaurant = (Restaurant) o;
		return Utils.equal(id, restaurant.id)
				&& Utils.equal(name, restaurant.name)
				&& Utils.equal(shop_address, restaurant.shop_address)
				&& Utils.equal(shopimg_src, restaurant.shopimg_src)
				&& Utils.equal(content, restaurant.content);
	}

	@Override
	public int hashCode() {
		return Utils.hashCode(id, name, shop_address, shopimg_src, content);
	}

	@Override
	public String toString() {
		return "Shop " + name + ", located in " + shop_address  + ", with content " + content;
	}
}

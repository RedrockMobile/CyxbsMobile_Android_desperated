package com.mredrock.cyxbsmobile.model;

import com.mredrock.cyxbsmobile.util.Utils;

/**
 * Result of Shaking
 *
 * Created by Stormouble on 16/4/7.
 */
public class EatWhat {

    public String id;
    public String name;
    public String address;
    public String img;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (null == o || getClass() != o.getClass()) return false;
        EatWhat eatWhat = (EatWhat) o;
        return Utils.equal(id, eatWhat.id)
                && Utils.equal(name, eatWhat.name)
                && Utils.equal(address, eatWhat.address)
                && Utils.equal(img, eatWhat.img);
    }

    @Override
    public int hashCode() {
        return Utils.hashCode(id, name, address, img);
    }

    @Override
    public String toString() {
        return  "Shop " + name + " located in " + address;
    }
}

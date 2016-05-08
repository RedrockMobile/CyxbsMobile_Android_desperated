package com.mredrock.cyxbs.model;

import com.mredrock.cyxbs.util.Utils;

/**
 * Result of Shaking
 * <p>
 * Created by Stormouble on 16/4/7.
 */
public class Shake {

    public String id;
    public String name;
    public String address;
    public String img;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (null == o || getClass() != o.getClass()) return false;
        Shake eatWhat = (Shake) o;
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
        return "Shop " + name + " located in " + address;
    }
}

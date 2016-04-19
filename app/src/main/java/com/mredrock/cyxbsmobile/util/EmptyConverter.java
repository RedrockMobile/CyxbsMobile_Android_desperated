package com.mredrock.cyxbsmobile.util;

import com.mredrock.cyxbsmobile.model.EmptyRoom;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by Stormouble on 15/12/12.
 */
public class EmptyConverter {
    /** 楼层的数字表示 */
    public static final String[] floorNumArray = {"1", "2", "3", "4", "5", "6"};

    /** 楼层的中文表示 */
    public static final String[] floorArray = {"一楼", "二楼", "三楼", "四楼", "五楼", "六楼"};

    /** 八教教学楼的数字表示 */
    public static final String[] eighthBuildingNumArray = {"1", "2", "3"};

    /** 八教教学楼的中文表示 */
    public static final String[] eighthBuildingArray = {"一栋", "二栋", "三栋"};

    /** 是否需要筛选数据 */
    private boolean mIsNeedPick;

    /** 数据是否为八教教室 */
    private boolean mIsEighthBuilding;

    /** 数据集 */
    private List<String> mEmptyData;

    /** 数据组, key代表楼层,value代表相应楼层的空教室 */
    private Map<String, List<String>> mEmptyGroup;

    /** 转换后的数据集 */
    private List<EmptyRoom> mEmptyRoomList;

    public EmptyConverter() {
        mEmptyData = new ArrayList<>();
        mEmptyRoomList = new ArrayList<>();
        mEmptyGroup = new TreeMap<>();
        mIsNeedPick = false;
        mIsEighthBuilding = false;
    }


    public void setEmptyData(List<String> data) {
        if (!mIsNeedPick) {
            mEmptyData.addAll(data);
            mIsNeedPick = true;
        } else {
            pick(data);
        }
    }

    /**
     * 课时多选时,筛选数据.
     */
    private void pick(List<String> data) {
        List<String> latestData = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            if (mEmptyData.contains(data.get(i))) {
                latestData.add(data.get(i));
            }
        }
        mEmptyData = latestData;
    }

    /**
     * 转换数据组{@link #mEmptyGroup}的数据,
     * 一个键值对对应一个EmptyRoom对象.
     */
    public List<EmptyRoom> convert() {
        grouping(mEmptyData);

        if (!mEmptyGroup.isEmpty()) {
            Set<String> keySet = mEmptyGroup.keySet();
            for (String key : keySet) {
                EmptyRoom emptyRoom = new EmptyRoom();
                if (!mIsEighthBuilding) {
                    for (int i = 0; i < floorNumArray.length; i++) {
                        if (key.equals(floorNumArray[i])) {
                            emptyRoom.setFloor(floorArray[i]);
                        }
                    }
                } else {
                    for (int i = 0; i < eighthBuildingNumArray.length; i++) {
                        if (key.equals(eighthBuildingNumArray[i])) {
                            emptyRoom.setFloor(eighthBuildingArray[i]);
                        }
                    }
                }
                emptyRoom.setEmptyRooms(mEmptyGroup.get(key));
                mEmptyRoomList.add(emptyRoom);
            }
        }
        return mEmptyRoomList;
    }

    /**
     * 根据不同楼层进行分组.
     */
    private void grouping(List<String> data) {
        check(data);

        List<String> emptyRooms = new ArrayList<>();
        String lastFloor = null;
        for (int i = 0; i < data.size(); i++) {
            String empty = data.get(i);
            String floor = empty.substring(1, 2);
            if (floor.equals(lastFloor) || lastFloor == null) {
                emptyRooms.add(empty);
                mEmptyGroup.put(floor, emptyRooms);
                lastFloor = floor;
            } else {
                grouping(data.subList(i, data.size()));
                break;
            }
        }
    }

    /**
     * 判断是否为八教
     */
    private void check(List<String> data) {
        if (data != null && !data.isEmpty()) {
            String empty = data.get(0);
            if ((empty.substring(0,1)).equals("8")) {
                mIsEighthBuilding = true;
            }
        }
    }

}

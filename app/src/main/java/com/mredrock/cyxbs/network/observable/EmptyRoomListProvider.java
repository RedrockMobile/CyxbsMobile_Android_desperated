package com.mredrock.cyxbs.network.observable;

import android.util.SparseArray;

import com.mredrock.cyxbs.model.EmptyRoom;
import com.mredrock.cyxbs.network.RequestManager;
import com.mredrock.cyxbs.network.exception.RedrockApiException;
import com.mredrock.cyxbs.util.EmptyConverter;

import java.io.IOException;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

public enum EmptyRoomListProvider {
    INSTANCE;

    private static final short WEEK_SHIFT = Short.SIZE - 1 - 5;
    private static final short WEEK_MASK = 0x1F << WEEK_SHIFT;

    private static final short WEEKDAY_SHIFT = WEEK_SHIFT - 3;
    private static final short WEEKDAY_MASK = 0x7 << WEEKDAY_SHIFT;

    private static final short BUIlDING_SHIFT = WEEKDAY_SHIFT - 4;
    private static final short BUILDING_MASK = WEEKDAY_MASK >> 4;

    private static final short SECTION_SHIFT = BUIlDING_SHIFT - 3;
    private static final short SECTION_MASK = BUILDING_MASK >> 3;

    private final SparseArray<List<String>> mMemoryCache;

    EmptyRoomListProvider() {
        mMemoryCache = new SparseArray<>();
    }

    public static int makeKey(int week, int weekday, int building, int section) {
        return week << WEEK_SHIFT | weekday << WEEKDAY_SHIFT | building << BUIlDING_SHIFT | section;
    }

    public EmptyRoomListProvider getInstance() {
        return INSTANCE;
    }

    public Observable<List<EmptyRoom>> createObservable(int week, int weekday, int building, int[] sections) {
        return Observable.create(new OnSubscribe(week, weekday, building, sections));
    }

    private class OnSubscribe implements Observable.OnSubscribe<List<EmptyRoom>> {
        private final int mWeek;
        private final int mWeekday;
        private final int mBuilding;
        private final int[] mSections;

        private OnSubscribe(int week, int weekday, int building, int[] sections) {
            mWeek = week;
            mWeekday = weekday;
            mBuilding = building;
            mSections = sections;
        }

        @Override
        public void call(Subscriber<? super List<EmptyRoom>> subscriber) {
            subscriber.onStart();
            try {
                EmptyConverter converter = new EmptyConverter();
                for (int section : mSections) {
                    converter.setEmptyData(load(section));
                }
                subscriber.onNext(converter.convert());
                subscriber.onCompleted();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
                subscriber.onError(throwable);
            }
        }

        private List<String> load(int section) throws IOException {
            final int key = makeKey(mWeek, mWeekday, mBuilding, section);
            List<String> list = mMemoryCache.get(key, null);
            if (list == null) {
                list = RequestManager.getInstance().getEmptyRoomListSync(mWeek, mWeekday, mBuilding, section);
                if (list == null) {
                    throw new RedrockApiException();
                }
                mMemoryCache.put(key, list);
            }
            return list;
        }
    }
}
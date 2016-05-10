package com.mredrock.cyxbs.util.cache;

import com.jakewharton.disklrucache.DiskLruCache;
import com.mredrock.cyxbs.util.LogUtils;
import com.mredrock.cyxbs.util.Utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Stormouble on 16/5/10.
 */
public class DiskCache implements Closeable, Flushable {
    private static final String TAG = LogUtils.makeLogTag(DiskCache.class);

    private static final int VERSION = 1;
    private static final int ENTRY_COUNT = 1;
    private static final int DISK_CACHE_INDEX = 0;
    private static final int IO_BUFFER_SIZE = 4 * 1024; //4MB

    private final DiskLruCache mCache;

    public DiskCache(File directory, long maxSize) {
        this(directory, VERSION, maxSize);
    }

    public DiskCache(File directory, int appVersion, long maxSize) {
        mCache = create(directory, appVersion, ENTRY_COUNT, maxSize);
    }

    public void put(String key, InputStream stream) {
        DiskLruCache.Editor editor = null;

        BufferedOutputStream out = null;
        BufferedInputStream in = null;
        try {
            editor = mCache.edit(md5HexKey(key));
            if (editor != null) {
                out = new BufferedOutputStream(editor.newOutputStream(DISK_CACHE_INDEX), IO_BUFFER_SIZE);
                in = new BufferedInputStream(stream, IO_BUFFER_SIZE);

                int b;
                while ((b = stream.read()) != -1) {
                    out.write(b);
                }
                editor.commit();
            }

            flush();
        } catch (IOException e) {
            abortQuietly(editor);
        } finally {
            Utils.closeQuietly(out);
            Utils.closeQuietly(in);
        }
    }

    public InputStream get(String key) {
        try {
            DiskLruCache.Snapshot snapshot = mCache.get(md5HexKey(key));
            if (snapshot != null) {
                return snapshot.getInputStream(DISK_CACHE_INDEX);
            } else {
                return null;
            }
        } catch (IOException e) {
            // Give up because the cache cannot be read.
            return null;
        }
    }

    public void update(String key) throws IOException {
        remove(key);
    }

    public void remove(String key) throws IOException {
        mCache.remove(md5HexKey(key));
    }

    public long size() throws IOException {
        return mCache.size();
    }

    public long maxSize() {
        return mCache.getMaxSize();
    }

    public File directory() {
        return mCache.getDirectory();
    }

    public boolean isClosed() {
        return mCache.isClosed();
    }

    public void clearDiskCache() {
        try {
            mCache.delete();
            mCache.close();
        } catch (IOException ignored) {
        }
    }

    @Override
    public void flush() throws IOException {
        mCache.flush();
    }

    @Override
    public void close() throws IOException {
        mCache.close();
    }

    private DiskLruCache create(File cacheDir, int appVersion, int valueCount, long maxSize) {
        try {
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            return DiskLruCache.open(cacheDir, appVersion, valueCount, maxSize);
        } catch (IOException e) {
            LogUtils.LOGE(TAG, "Couldn't open disk cache.");
        }
        return null;
    }

    private String md5HexKey(String key) {
        return Utils.md5Hex(key);
    }

    private void abortQuietly(DiskLruCache.Editor editor) {
        // Give up because the cache cannot be written.
        try {
            if (editor != null) {
                editor.abort();
            }
        } catch (IOException ignored) {
        }
    }

    public static class MarkedCacheResponse {
        private final MarkableInputStream mMarkSteam;

        public MarkedCacheResponse(InputStream inputStream) {
            mMarkSteam = new MarkableInputStream(inputStream);
        }

        public long saveToPosition(int position) {
            return mMarkSteam.savePosition(position);
        }

        public void reset(long mark) throws IOException {
            mMarkSteam.reset(mark);
        }
    }
}

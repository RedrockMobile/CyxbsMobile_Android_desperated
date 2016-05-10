package com.mredrock.cyxbs.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by mathiasluo on 16-4-28.
 */
public class BitmapUtil {

    public static final int COMPRESSION_SIZE = 2;

    public static File decodeBitmapFromRes(Context context, String path) throws IOException {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);

        if (getBitmapSize(bitmap) < 3) return new File(path);
        while (getBitmapSize(bitmap) > 3) {
            options.inSampleSize = COMPRESSION_SIZE;
            bitmap = BitmapFactory.decodeFile(path, options);
        }

        File file = new File(context.getCacheDir(), "cache.jpg");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
        fileOutputStream.flush();
        fileOutputStream.close();
        return file;

    }

    public static ByteArrayInputStream getBitmapOutStream(Bitmap bitmap) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        return new ByteArrayInputStream(out.toByteArray());
    }

    //单位 M
    public static double getBitmapSize(Bitmap bitmap) {
        return bitmap.getRowBytes() * bitmap.getHeight() / (1024.0 * 1024.0);
    }


}

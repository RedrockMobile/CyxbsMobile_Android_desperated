package com.mredrock.cyxbsmobile.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;

import com.mredrock.cyxbsmobile.APP;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.ResponseBody;

/**
 * 文件工具类
 */
public class FileUtils {

    public static final String TAG = "FileUtils.java";

    private FileUtils() {
        throw new UnsupportedOperationException("FileUtils can't be instantiated");
    }

    /**
     * Uri -> File
     * @param uri 路径
     * @return 文件
     */
    public static File getFile(Uri uri) {
        Context context = APP.getContext();
        String path = null, scheme = uri.getScheme();
        if (scheme.equals("file")) {
            path = uri.getEncodedPath();
            if (path != null) {
                path = Uri.decode(path);
                ContentResolver cr = context.getContentResolver();
                Cursor cur = cr.query(Images.Media.EXTERNAL_CONTENT_URI, new String[] {
                        Images.ImageColumns._ID, Images.ImageColumns.DATA },
                        "(" + Images.ImageColumns.DATA + "=" + "'" + path + "'" + ")",
                        null,
                        null);

                int dataIndex;
                if (cur != null) {
                    for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                        dataIndex = cur.getColumnIndex(Images.ImageColumns.DATA);
                        path = cur.getString(dataIndex);
                    }
                    cur.close();
                }
            }
            if (path != null) return new File(path);
        } else if (scheme.equals("content")) {
            String[] projection = { MediaStore.Images.Media.DATA };
            Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndexOrThrow(Images.Media.DATA);
                    path = cursor.getString(columnIndex);
                }
                cursor.close();
            }
            if (path != null) return new File(path);
        }
        return null;
    }

    /**
     * 保存文件到本地
     * @param responseBody 网络请求的数据
     * @param dir 保存的路径
     * @param fileName 文件名
     * @return 操作情况
     */
    public static String saveFile(ResponseBody responseBody, File dir, String fileName) {
        return saveFile(responseBody.byteStream(), dir, fileName);
    }

    /**
     * 保存文件到本地
     * @param in 流呀流
     * @param dir 保存的路径
     * @param fileName 文件名
     * @return 操作情况
     */
    public static String saveFile(InputStream in, File dir, String fileName) {
        if (in == null) {
            return null;
        } else {
            try {
                FileOutputStream out = new FileOutputStream(new File(dir, fileName));
                String url = dir.getAbsolutePath() + "/" + fileName;
                int read;
                byte[] buffer = new byte[32768];
                while ((read = in.read(buffer)) > 0) {
                    out.write(buffer, 0, read);
                }
                out.close();
                in.close();
                return "保存成功，保存位置在: " + url;
            } catch (IOException e) {
                e.printStackTrace();
                return "保存失败，原因: " + e.getMessage();
            }
        }
    }

}

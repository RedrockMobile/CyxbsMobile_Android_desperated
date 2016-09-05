package com.mredrock.cyxbs.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.Log;
import android.widget.Toast;

import com.mredrock.cyxbs.APP;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.Writer;

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
     *
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
                Cursor cur = cr.query(Images.Media.EXTERNAL_CONTENT_URI, new String[]{
                                Images.ImageColumns._ID, Images.ImageColumns.DATA},
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
            String[] projection = {MediaStore.Images.Media.DATA};
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
     *
     * @param responseBody 网络请求的数据
     * @param dir          保存的路径
     * @param fileName     文件名
     * @return 操作情况
     */
    public static String saveFile(ResponseBody responseBody, File dir, String fileName) {
        return saveFile(responseBody.byteStream(), dir, fileName);
    }

    /**
     * 保存文件到本地
     *
     * @param in       流呀流
     * @param dir      保存的路径
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

    public boolean saveUriToFile(Context context, Uri imageUri, String filePath, String fileName) {
        if (imageUri != null && imageUri.getScheme().equals("file")) {
            File file = new File(filePath, fileName);
            imageUri.getPath();
        } else {
            Toast.makeText(context, "Unexpected explore_error", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    /**
     * 从文件中读取字符串
     * @param src 文件名
     * @return 读取的字符串
     */
    public static String readStringFromFile(File src) {
        Reader reader = null;
        try {
            reader = new FileReader(src);
            char[] flush = new char[10];
            int len;
            StringBuilder sb = new StringBuilder();
            while (-1 != (len = reader.read(flush))) {
                sb.append(flush, 0, len);
            }
            return sb.toString();
        } catch (FileNotFoundException e) {
            Log.e("FileUtils", "readStringFromFile", e);
            return null;
        } catch (IOException e) {
            Log.e("FileUtils", "readStringFromFile", e);
            return null;
        } finally {
            if (null != reader) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e("FileUtils", "readStringFromFile", e);
                }
            }
        }
    }

    /**
     * 向文件中写入字符串
     * @param text 要写入的字符串
     * @param dest 文件名
     */
    public static void writeStringToFile(String text, File dest) {
        Writer wr = null;
        try {
            wr = new FileWriter(dest);
            wr.write(text);
            wr.flush();
        } catch (IOException e) {
            Log.e("FileUtils", "writeStringToFile", e);
        } finally {
            if (null != wr) {
                try {
                    wr.close();
                } catch (IOException e) {
                    Log.e("FileUtils", "writeStringToFile", e);
                }
            }
        }
    }

}

package com.mredrock.cyxbs.util;

import android.app.AlertDialog;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.TouchDelegate;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.mredrock.cyxbs.config.Config;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import okio.ByteString;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

/**
 * Created by cc on 15/8/22.
 */
public class Utils {

    public static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat DATE_FORMAT_DATE    = new SimpleDateFormat("yyyy-MM-dd");
    private static      int              screenWidth         = 0;
    private static      int              screenHeight        = 0;

    public static int sp2px(Context context, float spValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static int px2sp(Context context, float pxValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public static float dp2Px(Context context, float dp) {
        if (context == null) {
            return -1;
        }
        return dp * context.getResources().getDisplayMetrics().density;
    }

    public static float px2Dp(Context context, float px) {
        if (context == null) {
            return -1;
        }
        return px / context.getResources().getDisplayMetrics().density;
    }

    public static int dip2px(Context ctx, float dpValue) {
        final float scale = ctx.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dip(Context ctx, float pxValue) {
        final float scale = ctx.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static float dp2PxInt(Context context, float dp) {
        return (int) (dp2Px(context, dp) + 0.5f);
    }

    public static float px2DpCeilInt(Context context, float px) {
        return (int) (px2Dp(context, px) + 0.5f);
    }

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static int getScreenHeight(Context c) {
        if (screenHeight == 0) {
            WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenHeight = size.y;
        }
        return screenHeight;
    }

    public static int getScreenWidth(Context c) {
        if (screenWidth == 0) {
            WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenWidth = size.x;
        }
        return screenWidth;
    }

    /**
     * long time to string
     *
     * @param timeInMillis ...
     * @param dateFormat   ...
     * @return ...
     */
    public static String getTime(long timeInMillis, SimpleDateFormat dateFormat) {
        return dateFormat.format(new Date(timeInMillis));
    }

    /**
     * long time to string, format is {@link #DEFAULT_DATE_FORMAT}
     *
     * @param timeInMillis .
     * @return
     */
    public static String getTime(long timeInMillis) {
        return getTime(timeInMillis, DEFAULT_DATE_FORMAT);
    }

    /**
     * get current time in milliseconds
     *
     * @return
     */
    public static long getCurrentTimeInLong() {
        return System.currentTimeMillis();
    }

    /**
     * get current time in milliseconds, format is {@link #DEFAULT_DATE_FORMAT}
     *
     * @return
     */
    public static String getCurrentTimeInString() {
        return getTime(getCurrentTimeInLong());
    }

    /**
     * get current time in milliseconds
     *
     * @return
     */
    public static String getCurrentTimeInString(SimpleDateFormat dateFormat) {
        return getTime(getCurrentTimeInLong(), dateFormat);
    }

    /**
     * Hide soft input method manager
     *
     * @param view
     * @return view
     */
    public static View hideSoftInput(final View view) {
        InputMethodManager manager = (InputMethodManager) view.getContext()
                                                              .getSystemService(INPUT_METHOD_SERVICE);
        if (manager != null)
            manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        return view;
    }

    public static void toast(Context context, String content) {
        Toast toast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
        toast.show();
    }

    public static void alert(Context context, String content) {
        try {
            AlertDialog dialog = new AlertDialog.Builder(context).create();
            dialog.setMessage(content);
            dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * getAppVersionCode
     */
    public static int getAppVersionCode(Context context) {
        try {
            PackageManager mPackageManager = context.getPackageManager();
            PackageInfo _info = mPackageManager.getPackageInfo(context.getPackageName(), 0);
            return _info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            return 0;
        }
    }

    /**
     * getAppVersionName
     */
    public static String getAppVersionName(Context context) {
        try {
            PackageManager mPackageManager = context.getPackageManager();
            PackageInfo _info = mPackageManager.getPackageInfo(context.getPackageName(), 0);
            return _info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    /**
     * Set visibility of given view to be gone or visible
     * <p>
     * This method has no effect if the view visibility is currently invisible
     *
     * @param view
     * @param gone
     * @return view
     */
    public static <V extends View> V setGone(final V view, final boolean gone) {
        if (view != null)
            if (gone) {
                if (GONE != view.getVisibility())
                    view.setVisibility(GONE);
            } else {
                if (VISIBLE != view.getVisibility())
                    view.setVisibility(VISIBLE);
            }
        return view;
    }

    /**
     * Set visibility of given view to be invisible or visible
     * <p>
     * This method has no effect if the view visibility is currently gone
     *
     * @param view
     * @param invisible
     * @return view
     */
    public static <V extends View> V setInvisible(final V view,
                                                  final boolean invisible) {
        if (view != null)
            if (invisible) {
                if (INVISIBLE != view.getVisibility())
                    view.setVisibility(INVISIBLE);
            } else {
                if (VISIBLE != view.getVisibility())
                    view.setVisibility(VISIBLE);
            }
        return view;
    }

    /**
     * Increases the hit rect of a view. This should be used when an icon is small and cannot be easily tapped on.
     * Source: http://stackoverflow.com/a/1343796/5210
     *
     * @param amount   The amount of dp's to be added to all four sides of the view hit purposes.
     * @param delegate The view that needs to have its hit rect increased.
     */
    public static void increaseHitRectBy(final int amount, final View delegate) {
        increaseHitRectBy(amount, amount, amount, amount, delegate);
    }

    /**
     * Increases the hit rect of a view. This should be used when an icon is small and cannot be easily tapped on.
     * Source: http://stackoverflow.com/a/1343796/5210
     *
     * @param top      The amount of dp's to be added to the top for hit purposes.
     * @param left     The amount of dp's to be added to the left for hit purposes.
     * @param bottom   The amount of dp's to be added to the bottom for hit purposes.
     * @param right    The amount of dp's to be added to the right for hit purposes.
     * @param delegate The view that needs to have its hit rect increased.
     */
    public static void increaseHitRectBy(final int top, final int left, final int bottom, final int right, final View delegate) {
        final View parent = (View) delegate.getParent();
        if (parent != null && delegate.getContext() != null) {
            parent.post(new Runnable() {
                // Post in the parent's message queue to make sure the parent
                // lays out its children before we call getHitRect()
                public void run() {
                    final float densityDpi = delegate.getContext()
                                                     .getResources()
                                                     .getDisplayMetrics().densityDpi;
                    final Rect r = new Rect();
                    delegate.getHitRect(r);
                    r.top -= transformToDensityPixel(top, densityDpi);
                    r.left -= transformToDensityPixel(left, densityDpi);
                    r.bottom += transformToDensityPixel(bottom, densityDpi);
                    r.right += transformToDensityPixel(right, densityDpi);
                    parent.setTouchDelegate(new TouchDelegate(r, delegate));
                }
            });
        }
    }

    public static int transformToDensityPixel(int regularPixel, DisplayMetrics displayMetrics) {
        return transformToDensityPixel(regularPixel, displayMetrics.densityDpi);
    }

    public static int transformToDensityPixel(int regularPixel, float densityDpi) {
        return (int) (regularPixel * densityDpi);
    }


    public static Boolean write(Context context, String fileName, String content, String charset) {
        return writeToInternalStorage(context, fileName, content, charset);
    }

    public static Boolean write(Context context, String fileName, InputStream is) {
        return writeToInternalStorage(context, fileName, is);
    }

    public static Boolean write(String dirPath, String fileName, String content, String charset) {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return writeToExternalStorage(dirPath, fileName, content, charset);
        } else {
            return false;
        }
    }

    public static Boolean write(String dirPath, String fileName, InputStream is) {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return writeToExternalStorage(dirPath, fileName, is);
        } else {
            return false;
        }
    }

    public static Boolean write(Context context, String dirPath, String fileName, String content, String charset) {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return writeToExternalStorage(dirPath, fileName, content, charset);
        }
        /*
        else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state))
        {

        }
        */
        else {
            return writeToInternalStorage(context, fileName, content, charset);
        }
    }

    public static byte[] readBytes(String path) {
        try {
            File file = new File(path);
            long size = file.length();
            byte[] bytes = new byte[(int) size];
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
            buf.read(bytes, 0, bytes.length);
            buf.close();
            return bytes;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String read(String dirPath, String fileName) {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return readFromExternalStorage(dirPath, fileName);
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return readFromExternalStorage(dirPath, fileName);
        } else {
            return null;
        }
    }

    public static String read(Context context, String fileName) {
        return readFromInternalStorage(context, fileName);
    }

    public static String read(Context context, String dirPath, String fileName) {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return readFromExternalStorage(dirPath, fileName);
        }
        /*
        else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state))
        {

        }
        */
        else {
            return readFromInternalStorage(context, fileName);
        }
    }

    private static Boolean writeToExternalStorage(String dirPath, String fileName, String content, String charset) {
        if (charset == null) {
            charset = "UTF-8";
        }

        try {
            File esd = Environment.getExternalStorageDirectory();
            File dir = new File(esd.getAbsolutePath() + dirPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File file = new File(dir, fileName);
//            FileWriter writer = new FileWriter(file);
//            writer.write(content);
//            writer.flush();
//            writer.close();

            FileOutputStream fo = new FileOutputStream(file);
            OutputStreamWriter writer = new OutputStreamWriter(fo, charset);
            writer.write(content);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private static Boolean writeToExternalStorage(String dirPath, String fileName, InputStream is) {
        try {
            File esd = Environment.getExternalStorageDirectory();
            File dir = new File(esd.getAbsolutePath() + dirPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File file = new File(dir, fileName);
            FileOutputStream fos = new FileOutputStream(file);
            int count;
            byte[] buffer = new byte[8192];
            while ((count = is.read(buffer, 0, 8192)) != -1) {
                fos.write(buffer, 0, count);
            }
            fos.flush();
            fos.close();
            is.close();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private static Boolean writeToInternalStorage(Context context, String fileName, String content, String charset) {
        if (charset == null) {
            charset = "UTF-8";
        }

        try {
            FileOutputStream fo = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            OutputStreamWriter writer = new OutputStreamWriter(fo, charset);
            writer.write(content);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private static Boolean writeToInternalStorage(Context context, String fileName, InputStream is) {

        try {
            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            byte[] buffer = new byte[8192];
            int count;
            while ((count = is.read(buffer, 0, 8192)) != -1) {
                fos.write(buffer, 0, count);
            }
            fos.flush();
            fos.close();
            is.close();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private static String readFromExternalStorage(String dirPath, String fileName) {
        String content = null;
        try {
            File esd = Environment.getExternalStorageDirectory();
            File dir = new File(esd.getAbsolutePath() + dirPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File file = new File(dir, fileName);
            FileInputStream fi = new FileInputStream(file);
            BufferedReader rd = new BufferedReader(new InputStreamReader(fi), 8192);
            StringBuffer stringBuffer = new StringBuffer();
            String line;
            while ((line = rd.readLine()) != null) {
                stringBuffer.append(line);
            }

            content = stringBuffer.toString();
            fi.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return content;
    }

    private static String readFromInternalStorage(Context context, String fileName) {
        String content = null;
        try {
            FileInputStream fi = context.openFileInput(fileName);
            BufferedReader rd = new BufferedReader(new InputStreamReader(fi), 8192);
            StringBuffer stringBuffer = new StringBuffer();
            String line;
            while ((line = rd.readLine()) != null) {
                stringBuffer.append(line);
            }

            content = stringBuffer.toString();
            fi.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return content;
    }

    public static String read(InputStream is) {
        String content = null;
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is), 8192);
            StringBuffer stringBuffer = new StringBuffer();
            String line;
            while ((line = rd.readLine()) != null) {
                stringBuffer.append(line);
            }

            content = stringBuffer.toString();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return content;
    }


    public static List<File> readSubDirectory(String dirPath) {
        List<File> fileList = new ArrayList<File>();

        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            File esd = Environment.getExternalStorageDirectory();
            if (dirPath.indexOf(esd.getAbsolutePath()) == -1) {
                dirPath = esd.getAbsolutePath() + dirPath;
            }
            File dir = new File(dirPath);
            if (dir.exists()) {
                for (File file : dir.listFiles()) {
                    System.out.println(file.getAbsolutePath());
                    if (file.isDirectory()) {
                        fileList.add(file);
                    }
                }
            }
        }

        return fileList;
    }

    public static String getExternalStoragePath() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            File esd = Environment.getExternalStorageDirectory();
            return esd.getAbsolutePath();
        }

        return null;
    }

    public static void copy(File src, File dst) {
        try {
            File parent = new File(dst.getParent());
            if (!parent.exists()) {
                parent.mkdirs();
            }

            InputStream in = new FileInputStream(src);
            OutputStream out = new FileOutputStream(dst);

            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getMimeType(String fileName) {
        String mime = "3gp=video/3gpp;aab=application/x-authoware-bin;aam=application/x-authoware-map;aas=application/x-authoware-seg;ai=application/postscript;aif=audio/x-aiff;aifc=audio/x-aiff;aiff=audio/x-aiff;als=audio/X-Alpha5;amc=application/x-mpeg;ani=application/octet-stream;asc=text/plain;asd=application/astound;asf=video/x-ms-asf;asn=application/astound;asp=application/x-asap;asx=video/x-ms-asf;au=audio/basic;avb=application/octet-stream;avi=video/x-msvideo;awb=audio/amr-wb;bcpio=application/x-bcpio;bin=application/octet-stream;bld=application/bld;bld2=application/bld2;bmp=application/x-MS-bmp;bpk=application/octet-stream;bz2=application/x-bzip2;cal=image/x-cals;ccn=application/x-cnc;cco=application/x-cocoa;cdf=application/x-netcdf;cgi=magnus-internal/cgi;chat=application/x-chat;class=application/octet-stream;clp=application/x-msclip;cmx=application/x-cmx;co=application/x-cult3d-object;cod=image/cis-cod;cpio=application/x-cpio;cpt=application/mac-compactpro;crd=application/x-mscardfile;csh=application/x-csh;csm=chemical/x-csml;csml=chemical/x-csml;css=text/css;cur=application/octet-stream;dcm=x-lml/x-evm;dcr=application/x-director;dcx=image/x-dcx;dhtml=text/html;dir=application/x-director;dll=application/octet-stream;dmg=application/octet-stream;dms=application/octet-stream;doc=application/msword;dot=application/x-dot;dvi=application/x-dvi;dwfdrawing/x-dwf=;dwg=application/x-autocad;dxf=application/x-autocad;dxr=application/x-director;ebk=application/x-expandedbook;emb=chemical/x-embl-dl-nucleotide;embl=chemical/x-embl-dl-nucleotide;eps=application/postscript;epub=application/epub+zip;eri=image/x-eri;es=audio/echospeech;esl=audio/echospeech;etc=application/x-earthtime;etxtext/x-setext=;evm=x-lml/x-evm;evy=application/x-envoy;exe=application/octet-stream;fh4=image/x-freehand;fh5=image/x-freehand;fhc=image/x-freehand;fif=image/fif;flv=flv-application/octet-stream;fm=application/x-maker;fpx=image/x-fpx;fvi=video/isivideo;gau=chemical/x-gaussian-input;gca=application/x-gca-compressed;gdb=x-lml/x-gdb;gif=image/gif;gps=application/x-gps;gtar=application/x-gtar;gz=application/x-gzip;hdf=application/x-hdf;hdm=text/x-hdml;hdml=text/x-hdml;hlp=application/winhlp;hqxapplication/mac-binhex40=;htm=text/html;html=text/html;hts=text/html;ice=x-conference/x-cooltalk;ico=application/octet-stream;ief=image/ief;ifm=image/gif;ifs=image/ifs;imy=audio/melody;ins=application/x-NET-Install;ips=application/x-ipscript;ipx=application/x-ipix;it=audio/x-mod;itz=audio/x-mod;ivr=i-world/i-vrml;j2k=image/j2k;jad=text/vnd.sun.j2me.app-descriptor;jam=application/x-jam;jar=application/java-archive;jnlp=application/x-java-jnlp-file;jpe=image/jpeg;jpeg=image/jpeg;jpg=image/jpeg;jpz=image/jpeg;js=application/x-javascript;jwc=application/jwc;kjx=application/x-kjx;lak=x-lml/x-lak;latex=application/x-latex;lcc=application/fastman;lcl=application/x-digitalloca;lcr=application/x-digitalloca;lgh=application/lgh;lha=application/octet-stream;lml=x-lml/x-lml;lmlpack=x-lml/x-lmlpack;lsf=video/x-ms-asf;lsx=video/x-ms-asf;lzh=application/x-lzh;m13=application/x-msmediaview;m14=application/x-msmediaview;m15=audio/x-mod;m3u=audio/x-mpegurl;m3url=audio/x-mpegurl;ma1=audio/ma1;ma2=audio/ma2;ma3=audio/ma3;ma5=audio/ma5;man=application/x-troff-man;map=magnus-internal/imagemap;mbd=application/mbedlet;mct=application/x-mascot;mdb=application/msaccess;mdz=audio/x-mod;me=application/x-troff-me;mel=text/x-vmel;mi=application/x-mif;mid=audio/midi;midi=audio/midi;mif=application/x-mif;mil=image/x-cals;mio=audio/x-mio;mmf=application/x-skt-lbs;mng=video/x-mng;mny=application/x-msmoney;moc=application/x-mocha;mocha=application/x-mocha;mod=audio/x-mod;mof=application/x-yumekara;mol=chemical/x-mdl-molfile;mop=chemical/x-mopac-input;mov=video/quicktime;movie=video/x-sgi-movie;mp2=audio/x-mpeg;mp3=audio/x-mpeg;mp4=video/mp4;mpc=application/vnd.mpohun.certificate;mpe=video/mpeg;mpeg=video/mpeg;mpg=video/mpeg;mpg4=video/mp4;mpga=audio/mpeg;mpn=application/vnd.mophun.application;mpp=application/vnd.ms-project;mps=application/x-mapserver;mrl=text/x-mrml;mrm=application/x-mrm;ms=application/x-troff-ms;mts=application/metastream;mtx=application/metastream;mtz=application/metastream;mzv=application/metastream;nar=application/zip;nbmp=image/nbmp;nc=application/x-netcdf;ndb=x-lml/x-ndb;ndwn=application/ndwn;nif=application/x-nif;nmz=application/x-scream;nokia-op-logo=image/vnd.nok-oplogo-color;npx=application/x-netfpx;nsnd=audio/nsnd;nva=application/x-neva1;oda=application/oda;oom=application/x-AtlasMate-Plugin;pac=audio/x-pac;pae=audio/x-epac;pan=application/x-pan;pbm=image/x-portable-bitmap;pcx=image/x-pcx;pda=image/x-pda;pdb=chemical/x-pdb;pdf=application/pdf;pfr=application/font-tdpfr;pgm=image/x-portable-graymap;pict=image/x-pict;pm=application/x-perl;pmd=application/x-pmd;png=image/png;pnm=image/x-portable-anymap;pnz=image/png;pot=application/vnd.ms-powerpoint;ppm=image/x-portable-pixmap;pps=application/vnd.ms-powerpoint;ppt=application/vnd.ms-powerpoint;pqf=application/x-cprplayer;pqi=application/cprplayer;prc=application/x-prc;proxy=application/x-ns-proxy-autoconfig;ps=application/postscript;ptlk=application/listenup;pub=application/x-mspublisher;pvx=video/x-pv-pvx;qcp=audio/vnd.qcelp;qt=video/quicktime;qti=image/x-quicktime;qtif=image/x-quicktime;r3t=text/vnd.rn-realtext3d;ra=audio/x-pn-realaudio;ram=audio/x-pn-realaudio;rar=application/octet-stream;ras=image/x-cmu-raster;rdf=application/rdf+xml;rf=image/vnd.rn-realflash;rgb=image/x-rgb;rlf=application/x-richlink;rm=audio/x-pn-realaudio;rmf=audio/x-rmf;rmm=audio/x-pn-realaudio;rmvb=audio/x-pn-realaudio;rnx=application/vnd.rn-realplayer;roff=application/x-troff;rp=image/vnd.rn-realpix;rpm=audio/x-pn-realaudio-plugin;rt=text/vnd.rn-realtext;rte=x-lml/x-gps;rtf=application/rtf;rtg=application/metastream;rtx=text/richtext;rv=video/vnd.rn-realvideo;rwc=application/x-rogerwilco;s3m=audio/x-mod;s3z=audio/x-mod;sca=application/x-supercard;scd=application/x-msschedule;sdf=application/e-score;sea=application/x-stuffit;sgmtext/x-sgml=;sgml=text/x-sgml;sh=application/x-sh;shar=application/x-shar;shtml=magnus-internal/parsed-html;shw=application/presentations;si6=image/si6;si7=image/vnd.stiwap.sis;si9=image/vnd.lgtwap.sis;sis=application/vnd.symbian.install;sit=application/x-stuffit;skd=application/x-Koan;skm=application/x-Koan;skp=application/x-Koan;skt=application/x-Koan;slc=application/x-salsa;smd=audio/x-smd;smi=application/smil;smil=application/smil;smp=application/studiom;smz=audio/x-smd;snd=audio/basic;spc=text/x-speech;spl=application/futuresplash;spr=application/x-sprite;sprite=application/x-sprite;sdp=application/sdp;spt=application/x-spt;src=application/x-wais-source;stk=application/hyperstudio;stm=audio/x-mod;sv4cpio=application/x-sv4cpio;sv4crc=application/x-sv4crc;svf=image/vnd;svg=image/svg-xml;svh=image/svh;svr=x-world/x-svr;swf=application/x-shockwave-flash;swfl=application/x-shockwave-flash;t=application/x-troff;tad=application/octet-stream;talk=text/x-speech;tar=application/x-tar;taz=application/x-tar;tbp=application/x-timbuktu;tbt=application/x-timbuktu;tcl=application/x-tcl;tex=application/x-tex;texi=application/x-texinfo;texinfo=application/x-texinfo;tgz=application/x-tar;thmapplication/vnd.eri.thm=;tif=image/tiff;tiff=image/tiff;tki=application/x-tkined;tkined=application/x-tkined;toc=application/toc;toy=image/toy;tr=application/x-troff;trk=x-lml/x-gps;trm=application/x-msterminal;tsi=audio/tsplayer;tsp=application/dsptype;tsv=text/tab-separated-values;ttf=application/octet-stream;ttz=application/t-time;txt=text/plain;ult=audio/x-mod;ustar=application/x-ustar;uu=application/x-uuencode;uue=application/x-uuencode;vcd=application/x-cdlink;vcf=text/x-vcard;vdo=video/vdo;vib=audio/vib;viv=video/vivo;vivo=video/vivo;vmd=application/vocaltec-media-desc;vmf=application/vocaltec-media-file;vmi=application/x-dreamcast-vms-info;vms=application/x-dreamcast-vms;vox=audio/voxware;vqe=audio/x-twinvq-plugin;vqf=audio/x-twinvq;vql=audio/x-twinvq;vre=x-world/x-vream;vrml=x-world/x-vrml;vrt=x-world/x-vrt;vrw=x-world/x-vream;vts=workbook/formulaone;wav=audio/x-wav;wax=audio/x-ms-wax;wbmp=image/vnd.wap.wbmp;web=application/vnd.xara;wi=image/wavelet;wis=application/x-InstallShield;wm=video/x-ms-wm;wma=audio/x-ms-wma;wmd=application/x-ms-wmd;wmf=application/x-msmetafile;wml=text/vnd.wap.wml;wmlc=application/vnd.wap.wmlc;wmls=text/vnd.wap.wmlscript;wmlsc=application/vnd.wap.wmlscriptc;wmlscript=text/vnd.wap.wmlscript;wmv=video/x-ms-wmv;wmx=video/x-ms-wmx;wmz=application/x-ms-wmz;wpng=image/x-up-wpng;wpt=x-lml/x-gps;wri=application/x-mswrite;wrl=x-world/x-vrml;wrz=x-world/x-vrml;ws=text/vnd.wap.wmlscript;wsc=application/vnd.wap.wmlscriptc;wv=video/wavelet;wvx=video/x-ms-wvx;wxl=application/x-wxl;x-gzipapplication/x-gzip=;xar=application/vnd.xara;xbm=image/x-xbitmap;xdm=application/x-xdma;xdma=application/x-xdma;xdw=application/vnd.fujixerox.docuworks;xht=application/xhtml+xml;xhtm=application/xhtml+xml;xhtml=application/xhtml+xml;xla=application/vnd.ms-excel;xlc=application/vnd.ms-excel;xll=application/x-excel;xlm=application/vnd.ms-excel;xls=application/vnd.ms-excel;xlt=application/vnd.ms-excel;xlw=application/vnd.ms-excel;xm=audio/x-mod;xml=text/xml;xmz=audio/x-mod;xpi=application/x-xpinstall;xpm=image/x-xpixmap;xsit=text/xml;xsl=text/xml;xul=text/xul;xwd=image/x-xwindowdump;xyz=chemical/x-pdb;yz1=application/x-yz1;z=application/x-compress;zac=application/x-zaurus-zac;zip=application/zip;";
        int dot = fileName.lastIndexOf(".");
        if (dot != -1) {
            String ext = fileName.substring(dot + 1);
            int start = mime.indexOf(ext);
            if (start != -1) {
                int end = mime.indexOf(";", start);
                if (end != -1) {
                    return mime.substring(start + ext.length() + 1, end);
                }
            }
        }

        return null;
    }


    public static void prepareAppDir() {
        try {
            File esd = Environment.getExternalStorageDirectory();
            File dir = new File(esd.getAbsolutePath() + Config.DIR);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File dirFile = new File(esd.getAbsolutePath() + Config.DIR_FILE);
            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void set(Context context, String key, String value) {
        SharedPreferences.Editor sp = context.getSharedPreferences("data", 0).edit();
        sp.putString(key, value);
        sp.commit();
    }

    public static String get(Context context, String key) {
        String value = null;
        try {
            SharedPreferences sp = context.getSharedPreferences("data", 0);
            value = sp.getString(key, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 获取SharedPreferences
     *
     * @param ctx
     * @return SharedPreferences
     */
    public static SharedPreferences getPreference(Context ctx) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(ctx);
        return sp;
    }

    /**
     * 清除空行
     *
     * @param content
     * @return
     */
    public static String clrearEmptyLine(String content) {
        String result = content.replaceAll("(\r?\n(\\s*\r?\n)+)", "\r\n");
        if (result.startsWith("\r\n")) {
            result = result.substring(2);
        }
        return result;
    }

    public static Bitmap getViewBitmap(View v) {
        v.clearFocus();
        v.setPressed(false);
        boolean willNotCache = v.willNotCacheDrawing();
        v.setWillNotCacheDrawing(false);
        int color = v.getDrawingCacheBackgroundColor();
        v.setDrawingCacheBackgroundColor(0);

        if (color != 0) {
            v.destroyDrawingCache();
        }
        v.buildDrawingCache();
        Bitmap cacheBitmap = v.getDrawingCache();
        if (cacheBitmap == null) {
            return null;
        }
        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);
        v.destroyDrawingCache();
        v.setWillNotCacheDrawing(willNotCache);
        v.setDrawingCacheBackgroundColor(color);
        return bitmap;
    }

    public static Bitmap loadBitmapFromView(View v) {
        if (v == null) {
            return null;
        }
        Bitmap screenshot;
        screenshot = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(screenshot);
        c.translate(-v.getScrollX(), -v.getScrollY());
        v.draw(c);
        return screenshot;
    }

    public static void saveBitmapToFile(Bitmap mBitmap, String bitpath, String bitName) throws IOException {
        File dir = new File(Config.dataFilePath);
        if (!dir.isDirectory()) {
            dir.mkdirs();
        }

        File f = new File(bitpath + bitName);
        if (!f.exists()) {
            f.createNewFile();
        }
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 是否有网络
     *
     * @param ctx
     * @return
     */
    public static boolean isNetWorkAvilable(Context ctx) {
        ConnectivityManager connectivityManager = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        return !(activeNetInfo == null || !activeNetInfo.isAvailable());
    }

    public static void updateWidget(Context context) {
        Intent updateIntent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        context.sendBroadcast(updateIntent);
    }

    public static <T> T checkNotNullWithException(T reference) {
        if (reference == null) {
            throw new NullPointerException();
        }
        return reference;
    }

    public static <T> boolean checkNotNull(T reference) {
        return reference != null;
    }

    public static boolean checkNotNullAndNotEmpty(Collection<?> reference) {
        return reference != null && !reference.isEmpty();
    }

    public static boolean checkNotNullAndNotEmpty(Map<?, ?> reference) {
        return reference != null && !reference.isEmpty();
    }

    public static int hashCode(Object... objects) {
        return Arrays.hashCode(objects);
    }

    public static boolean equal(Object a, Object b) {
        return a == b || (a != null && a.equals(b));
    }


    public static void closeQuietly(InputStream is) {
        if (is == null) return;
        try {
            is.close();
        } catch (IOException ignored) {
            // ignore
        }
    }

    public static void closeQuietly(OutputStream os) {
        if (os == null) return;
        try {
            os.close();
        } catch (IOException ignored) {
            // ignore
        }
    }

    public static int getAppVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

    public static boolean isSDCardMounted() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    public static File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath = null;
        if (isSDCardMounted() || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }

    public static File getDiskCacheDirInternal(Context context, String uniqueName) {
        String cachePath = context.getCacheDir().getPath();
        return new File(cachePath + File.separator + uniqueName);
    }

    public static File getDiskCacheDirExternal(Context context, String uniqueName) {
        String cachePath = context.getExternalCacheDir().getPath();
        return new File(cachePath + File.separator + uniqueName);
    }

    public static String md5Hex(String s) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] md5bytes = messageDigest.digest(s.getBytes("UTF-8"));
            return ByteString.of(md5bytes).hex();
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }
    }

    /**
     * Check null, return default value if it is null
     * @param object the object which need to be checked
     * @param defaultValue the default value which will be return when the object == null
     * @param <T> the type of object
     * @return object when it not equal to null, or defaultValue when object == null
     */
    public static <T> T checkNotNullWithDefaultValue(T object, T defaultValue) {
        if (object != null) {
            return object;
        } else {
            return defaultValue;
        }
    }
}

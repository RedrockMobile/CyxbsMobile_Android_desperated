package com.mredrock.cyxbs.component.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by ：AceMurder
 * Created on ：2017/4/4
 * Created for : CyxbsMobile_Android.
 * Enjoy it !!!
 */

public class PastElectricChartView extends View {

    private static final String TAG = "PastElectricChartView";
    private List<Double> yValue = new ArrayList<>();
    private List<String> xValue;
    private double maxValue;
    private double minValue;
    private Paint mPaint = new Paint();
    private List<Point> mPoints = new ArrayList<>();
    private Canvas mCanvas;
    private boolean needDrawCircle = false;
    private int position;


    private boolean needDraw = true;

    private int startX, startY, endX, endY;
    private Path mPath = new Path();

    private ItemClickCallBack callBack;


    public PastElectricChartView(Context context) {
        super(context);

    }

    public PastElectricChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    private void initData() {
//        List<Double> doubles = new ArrayList<>();
//        double[] data = {40.0, 25, 0, 27, 57.0,36.0};
//        doubles.add(40.0);
//        doubles.add(250.0);
//        doubles.add(0.0);
//        doubles.add(56.5);
//        doubles.add(36.0);
//        doubles.add(75.0);
//        yValue.addAll(doubles);
    }

    public PastElectricChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData();
    }

    public void setCallBack(ItemClickCallBack callBack) {
        this.callBack = callBack;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            startX = (int) event.getX();
            startY = (int) event.getY();
        }

        if (event.getAction() == MotionEvent.ACTION_UP) {
            endX = (int) event.getX();
            endY = (int) event.getY();
        }
        int distance = (int) Math.sqrt(Math.pow(startX - endX, 2) + Math.pow(startY - endY, 2));
        if (distance <= 20) {
            int width = getWidth();
            float x = event.getX() - dip2px(getContext(), 60) / (width / (yValue.size() - 1));
            for (int i = 0; i < mPoints.size(); i++) {
                int dis = Math.abs((int) (event.getX() - mPoints.get(i).x));
                if (dis <= dip2px(getContext(), 30)) {
                    position = i;
                    needDrawCircle = true;
                    invalidate();

                    return true;
                }
            }
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        this.mCanvas = canvas;
        super.onDraw(canvas);

        if (yValue.size() == 0)
            return;
        mPaint.setStrokeWidth(10);
        mPaint.setColor(Color.WHITE);
        drawScrollLine(getPointsFromData(), canvas);
        if (needDrawCircle)
            drawCircle(position);
        drawText(canvas);


    }


    public void setyValue(List<Double> yValue) {
        this.yValue = yValue;
        position = yValue.size() - 1;
        needDrawCircle = true;
        for (Double aDouble : this.yValue) {
            if (aDouble > maxValue)
                maxValue = aDouble;
            if (aDouble < minValue)
                minValue = aDouble;
        }
        invalidate();

    }

    public void setxValue(List<String> xValue) {
        this.xValue = xValue;
    }

    private void drawScrollLine(List<Point> points, Canvas canvas) {
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(12);
        mPaint.setColor(Color.WHITE);
        Point start;
        Point end;
        points.add(0, new Point(0, (points.get(0).y + points.get(1).y) / 2));
        points.add(new Point(getWidth(), (points.get(points.size() - 1).y + points.get(points.size() - 2).y) / 2));
        for (int i = 0; i < points.size() - 1; i++) {
            start = points.get(i);
            end = points.get(i + 1);
            int wt = (start.x + end.x) / 2;
            Point p3 = new Point();
            Point p4 = new Point();
            p3.y = start.y;
            p3.x = wt;
            p4.y = end.y;
            p4.x = wt;
            mPath.reset();
//            Path path = new Path();
            mPath.moveTo(start.x, start.y);
            mPath.cubicTo(p3.x, p3.y, p4.x, p4.y, end.x, end.y);
            canvas.drawPath(mPath, mPaint);
            mPath.close();

        }
    }


    public void drawCircle(int i) {
        if (callBack != null)
            callBack.onClick(i);
        mPaint.setStyle(Paint.Style.FILL);
        Point p = mPoints.get(i);
        mCanvas.drawCircle(p.x, p.y, dip2px(getContext(), 8), mPaint);
        mPaint.setColor(Color.parseColor("#6effffff"));
        mCanvas.drawCircle(p.x, p.y, dip2px(getContext(), 12), mPaint);
        mPaint.setStrokeWidth(dip2px(getContext(), 1));
        mPaint.setColor(Color.parseColor("#aeffffff"));
        mCanvas.drawLine(p.x, p.y, p.x, getHeight(), mPaint);
        needDrawCircle = false;
        drawAnchor(i);


    }


    private void drawText(Canvas canvas) {
        //            canvas.drawText(text, centerX - dip2px(context, 45), baseLine, paint);
        if (xValue.size() == 0)
            return;
        int width = getWidth() - dip2px(getContext(), 40);
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextSize(dip2px(getContext(), 15));
        for (int i = 0; i < xValue.size(); i++) {
            Log.i(TAG, xValue.get(i));
            canvas.drawText(xValue.get(i), width / (yValue.size() - 1) * i + dip2px(getContext(), 10), getHeight() - dip2px(getContext(), 30), mPaint);

        }
    }


    private void drawAnchor(int i) {
        Log.i(TAG, "drawAnchor" + "  " + i);
        int width = getWidth() - dip2px(getContext(), 40);
        mPath.reset();
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);

        int x1 = mPoints.get(i).x;
        int y1 = getHeight() - dip2px(getContext(), 12);
        int x2 = x1 - width / (yValue.size() - 1) / 2;

        int y2 = getHeight();
        mPath.moveTo(x2, y2);
        mPath.quadTo((x1 + x2) / 2, getHeight(), x1, y1);
        int x3 = x1 + width / (yValue.size() - 1) / 2;
        mPath.quadTo((x1 + x3) / 2, getHeight(), x3, getHeight());
        mPath.lineTo(x2, y2);
        mCanvas.drawPath(mPath, mPaint);


    }

    public int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private List<Point> getPointsFromData() {
        mPoints.clear();
        int width = getWidth() - dip2px(getContext(), 40);
        int height = getHeight() - dip2px(getContext(), 80);
        List<Point> points = new ArrayList<>();
        double midValue = (maxValue - minValue);
        for (int i = 0; i < yValue.size(); i++) {
            Point p = new Point(width / (yValue.size() - 1) * i + dip2px(getContext(), 20), (int) ((1 - (yValue.get(i) - minValue) / midValue) * height) + dip2px(getContext(), 30));
            points.add(p);
            mPoints.add(p);
            Log.i(TAG, points.get(i).x + " " + points.get(i).y);
        }
        return points;
    }


    public interface ItemClickCallBack {
        void onClick(int position);
    }

    public boolean isNeedDraw() {
        return needDraw;
    }

    public void setNeedDraw(boolean needDraw) {
        this.needDraw = needDraw;
    }
}

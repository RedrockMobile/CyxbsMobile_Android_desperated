package com.mredrock.freshmanspecial.units;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;

import java.util.List;

/**
 * Created by zia on 17-8-1.
 */

public class CircleChart extends android.support.v7.widget.AppCompatTextView {

    private boolean isLog = false;
    private Paint paint;//画笔
    private Paint paint1;//圆圈的画笔
    private int paintWidth = ScreenUnit.dip2px(getContext(),17);//线宽
    private int speed = 1;//移动速度
    private int rotate = 0;//转动角度
    private int startAngle = -90;//起始角度，默认在最上方开始
    private boolean isRun = false;
    private List<ChartData> list;//数据
    private int space = ScreenUnit.dip2px(getContext(),8);//线间距
    private float centerX = 0, centerY = 0;
    private int radius = ScreenUnit.dip2px(getContext(),50);//最小半径
    private boolean autoSpace = false;//默认自动调整间距
    private boolean textSlope = false;//设置字沿圈写
    static int defaultColor = Color.RED, defaultStrokeColor = Color.BLACK, defaultTextColor = Color.BLACK,
            defultBackgroundColor = Color.LTGRAY, defultBackgroundStrokeColor = Color.DKGRAY;


    public CircleChart(Context context) {
        super(context);
    }

    public CircleChart(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();
        paint.setAntiAlias(true);//抗锯齿
        paint.setDither(true);//防抖动
        paint1 = new Paint();
        paint1.setAntiAlias(true);//抗锯齿
        paint1.setDither(true);//防抖动
        paint1.setShadowLayer(3,-2,1,Color.WHITE);
        //setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        if (isRun) {
            if (list == null || list.size() == 0) return;
            if (centerX == 0 || centerY == 0) {
                centerX = getWidth() / 2;
                centerY = getHeight() / 2;
            }
            log("onDraw");
            int count = 0;
            for (ChartData data : list) {
                if (data.getRadius() == 0) {
                    if (autoSpace) {
                        int space = (getWidth() / 2 - 10) / list.size();
                        //设置半径，最大宽度一半减去宽度的1/16的padding，再减间距
                        data.setRadius(centerX - centerX / 7 - space * count);
                        count++;
                    } else {
                        data.setRadius(radius + (space+ paintWidth) * count );
                        count++;
                    }
                    if (data.getSpeed() == 1) {
                        data.setSpeed(speed);
                    }
                }
                //if (data.getPercentage() == 0) return;//防bug
                //绘制底层装饰
                drawBackground(canvas, data.getBackgroundColor(), data.getBackgroundStrokeColor(), data.getRadius());
                if(data.getPercentage() != 0){
                    //绘制动画数据条
                    drawArc(canvas, data.getPercentage(), data.getColor(), data.getStrokeColor(), data.getRadius(), data.getSpeed());
                    //绘制文字，这里设置字体为线宽的一半
                    drawText(canvas, data.getPercentage()+"%", paint.measureText(data.getPercentage()+"%"), paintWidth / 3 * 2, data.getTextColor(), data.getRadius());
                }
            }
        } else {
            invalidate();
        }
        //if(rotate >= 360) isRun = false;
    }

    /**
     * 画圆方法封装
     *
     * @param canvas     画布
     * @param percentage 百分比
     * @param color      圆圈颜色
     * @param radius     半径
     * @param s          速度
     */
    private void drawArc(Canvas canvas, float percentage, int color, int strokeColor, float radius, int s) {
        if(percentage == 0) return;
        if (percentage > 90) {  //防止首尾相接
            percentage = 90;
        }
        if ((float) rotate / 360 * 100 < percentage) {//还没到达终点
            drawArc(canvas, radius, startAngle, rotate, paintWidth + 10, strokeColor,paint);//绘制底层阴影
            drawArc(canvas, radius, startAngle, rotate, paintWidth - 1, color,paint1);//绘制上层，宽度稍微小点，留出空间
            rotate = rotate + s;//角度增加一度
            log(rotate + "");
            invalidate();
        } else {//到达终点,停止绘制
            drawArc(canvas, radius, startAngle, (int) (percentage / 100 * 360), paintWidth + 10, strokeColor,paint);//绘制底层阴影
            drawArc(canvas, radius, startAngle, (int) (percentage / 100 * 360), paintWidth - 1, color,paint1);//绘制上层，宽度稍微小点，留出空间
        }
    }

    /**
     * 画图再次封装
     *
     * @param canvas      画布
     * @param radius      半径
     * @param angle       起始角度
     * @param rotate      绘制角度
     * @param strokeWidth 描边宽度
     * @param color       颜色
     */
    private void drawArc(Canvas canvas, float radius, int angle, int rotate, int strokeWidth, int color, Paint paint) {
        RectF oval = new RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius);//用一个正方形包裹圆形
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(color);
        paint.setStrokeWidth(strokeWidth);
        canvas.drawArc(oval, angle, rotate + (-90 - angle), false, paint);
    }

    /**
     * 背景圆画法封装
     *
     * @param canvas                画布
     * @param backgroundColor       背景颜色
     * @param backgroundStrokeColor 背景描边颜色
     * @param radius                半径
     */
    private void drawBackground(Canvas canvas, int backgroundColor, int backgroundStrokeColor, float radius) {
        RectF oval = new RectF(centerX - radius, centerY - radius,
                centerX + radius, centerY + radius);//用一个正方形包裹圆形
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(paintWidth + 10);//描边宽度
        paint.setColor(backgroundStrokeColor);
        canvas.drawArc(oval, -90, 360, false, paint);
        paint.setStrokeWidth(paintWidth);//描边宽度
        paint.setColor(backgroundColor);
        canvas.drawArc(oval, -90, 360, false, paint);
    }

    private void drawText(Canvas canvas, String text, float textWidth, int textSize, int color, float radius) {
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(textSize);
        paint.setMaskFilter(null);
        if (textSlope && radius < getWidth() / 4) {//如果允许字体沿圈，且圈在半径一半以内，生效
            RectF oval = new RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius);//用一个正方形包裹圆形
            Path path = new Path();
            path.addArc(oval, -130, textWidth);
            canvas.drawTextOnPath(text, path, 0, 15, paint);
        } else {
            canvas.drawText(text, centerX - textWidth - getWidth() / 20, centerY - radius + paintWidth/4, paint);
        }
    }

    public void setData(List<ChartData> list) {
        this.list = list;
    }

    public void setDefaultColor(int color) {
        defaultColor = color;
    }

    public void setDefaultTextColor(int color) {
        defaultTextColor = color;
    }

    /**
     * 设置速度，默认为1，每秒走1/6圆
     * 若设置为2，每秒走1/3圈
     * 公式 6/速度
     *
     * @param speed 速度
     */
    public void setSpeed(int speed) {
        if (speed <= 0) return;
        this.speed = speed;
    }

    /**
     * 设置起始角度
     *
     * @param angle 角度
     */
    public void setStartAngle(int angle) {
        this.startAngle = angle;
    }

    /**
     * 设置线宽
     *
     * @param width 宽度 px
     */
    public void setPaintWidth(int width) {
        if (width <= 0) return;
        paintWidth = width;
    }

    /**
     * 设置间距
     *
     * @param space 间距 px
     */
    public void setSpace(int space) {
        if (space <= 0) return;
        this.space = space;
        autoSpace = false;
    }

    public void setTextSlope() {
        textSlope = true;
    }

    /**
     * 开始动画
     */
    public void run() {
        isRun = true;
        rotate = 0;
        invalidate();
    }

    public void openLog() {
        isLog = true;
    }

    private void log(String msg) {
        if (isLog) Log.d("CircleChartTest", msg);
    }
}



package com.mredrock.cyxbs.freshman.ui.widget;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.mredrock.cyxbs.freshman.R;

/*
 by Cynthia at 2018/8/12
 description : 环形进度条
  */
public class CircleProcessView extends View {

    private Paint circlePaint;
    private Paint textPaint;
    private Paint processPaint;
    private Paint rectPaint;
    private float[] centerLocation;
    private float[] processes;
    private String[] colors;
    private float time;
    private int num;
    private float everySize;
    private float[] process;
    private RectF rectF = new RectF();
    private Rect mText = new Rect();
    private boolean isStart;

    public CircleProcessView(Context context) {
        this(context, null);
    }

    public CircleProcessView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProcessView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(attrs);
        initPaints();
        isStart = false;
    }

    private void initAttr(AttributeSet attributeSet) {
        TypedArray typeArray = getContext().obtainStyledAttributes(attributeSet, R.styleable.CircleProcessView);
        CharSequence[] colors = typeArray.getTextArray(R.styleable.CircleProcessView_colors);
        CharSequence[] processes = typeArray.getTextArray(R.styleable.CircleProcessView_processes);
        time = typeArray.getFloat(R.styleable.CircleProcessView_animatorTime, 2f);
        num = typeArray.getInt(R.styleable.CircleProcessView_circleNumber, 1);
        typeArray.recycle();
        initData(colors, processes);
    }

    private void initData(CharSequence[] colors, CharSequence[] process) {
        this.colors = new String[num * 2];
        processes = new float[num];
        for (int i = 0; i < num; i++) {
            processes[i] = Float.parseFloat(String.valueOf(process[i]));
        }
        for (int i = 0; i < num * 2; i++) {
            this.colors[i] = String.valueOf(colors[i]);
        }
        setAnim();
    }

    private void initPaints() {
        circlePaint = new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeCap(Paint.Cap.ROUND);

        processPaint = new Paint();
        processPaint.setAntiAlias(true);
        processPaint.setStyle(Paint.Style.STROKE);
        processPaint.setStrokeCap(Paint.Cap.ROUND);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(dp2px(17));

        rectPaint = new Paint();
        rectPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int length = widthSize / 4 * 3;
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(length, MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) (length * 1.25f), MeasureSpec.EXACTLY);
        setPosition(length);
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!isStart)
            return;
        drawDescription(canvas);
        int x = getWidth() / 2;
        for (int i = 0; i < num; i++) {
            circlePaint.setColor(Color.parseColor(colors[i + 2]));
            circlePaint.setStrokeWidth(everySize / 3 * 2);
            circlePaint.setAlpha(25);
            canvas.drawCircle(x, x, centerLocation[i], circlePaint);
            circlePaint.setColor(Color.parseColor("#ffffff"));
            circlePaint.setStrokeWidth(everySize / 3 * 2 - dp2px(4));
            circlePaint.setAlpha(100);
            canvas.drawCircle(x, x, centerLocation[i], circlePaint);

            processPaint.setStrokeWidth(everySize / 3 * 2);
            processPaint.setColor(Color.parseColor(colors[i + 2]));
            processPaint.setAlpha(178);
            rectF.set(x - centerLocation[i], x - centerLocation[i], x + centerLocation[i], x + centerLocation[i]);
            canvas.drawArc(rectF, -90, process[i] * 360 / 100, false, processPaint);

            processPaint.setStrokeWidth(everySize / 3 * 2 - dp2px(2));
            processPaint.setColor(Color.parseColor("#FFFFFF"));
            processPaint.setAlpha(100);
            canvas.drawArc(rectF, -90, process[i] * 360 / 100, false, processPaint);

            @SuppressLint("DefaultLocale")
            String mProcess = String.valueOf((int) process[i]);
            String text = mProcess + "%";
            textPaint.setColor(Color.parseColor(colors[i]));
            textPaint.getTextBounds(text, 0, text.length(), mText);
            int h = mText.height();
            canvas.drawText(text, (float) x * 0.65f, (float) x - centerLocation[i] + h / 1.5f, textPaint);

        }
    }

    private void drawDescription(Canvas canvas) {
        int x = getWidth() / 2;
        rectF.set(x / 2.8f, x + centerLocation[num - 1] + x / 3, x / 1.8f, x + centerLocation[num - 1] + x / 2f);
        rectPaint.setColor(Color.parseColor(colors[3]));
        rectPaint.setAlpha(178);
        canvas.drawRoundRect(rectF, 5, 5, rectPaint);
        rectF.set(x / 2.8f + dp2px(1), x + centerLocation[num - 1] + x / 3 + dp2px(1), x / 1.8f - dp2px(1), x + centerLocation[num - 1] + x / 2f - dp2px(1));
        rectPaint.setColor(Color.parseColor(colors[1]));
        rectPaint.setAlpha(200);
        canvas.drawRoundRect(rectF, 5, 5, rectPaint);

        textPaint.setColor(Color.parseColor("#000000"));
        textPaint.setAlpha(130);
        textPaint.setTextSize(dp2px(15));
        String sex = "男";
        textPaint.getTextBounds(sex, 0, sex.length(), mText);
        int w = mText.width();
        canvas.drawText(sex, x - w * 3, x + centerLocation[num - 1] + x / 2f - dp2px(6), textPaint);


        rectF.set(x / 2.8f + x * 0.8f, x + centerLocation[num - 1] + x / 3, x / 1.8f + x * 0.8f, x + centerLocation[num - 1] + x / 2f);
        rectPaint.setColor(Color.parseColor(colors[2]));
        rectPaint.setAlpha(178);
        canvas.drawRoundRect(rectF, 5, 5, rectPaint);
        rectF.set(x / 2.8f + x * 0.8f + dp2px(1), x + centerLocation[num - 1] + x / 3 + dp2px(1), x / 1.8f + x * 0.8f - dp2px(1), x + centerLocation[num - 1] + x / 2f - dp2px(1));
        rectPaint.setColor(Color.parseColor(colors[0]));
        rectPaint.setAlpha(200);
        canvas.drawRoundRect(rectF, 5, 5, rectPaint);

        textPaint.setColor(Color.parseColor("#000000"));
        textPaint.setAlpha(130);
        textPaint.setTextSize(dp2px(15));
        sex = "女";
        textPaint.getTextBounds(sex, 0, sex.length(), mText);
        w = mText.width();
        canvas.drawText(sex, x - w * 3 + x * 0.8f, x + centerLocation[num - 1] + x / 2f - dp2px(6), textPaint);
    }

    private void setPosition(int width) {
        float useSize = width * 0.5f * 0.75f;
        float paddingSize = width * 0.5f * 0.25f;
        everySize = useSize / num;
        float minSize = useSize / 2.5f;
        if (everySize > minSize) {
            everySize = minSize;
        }
        circlePaint.setStrokeWidth(everySize);
        processPaint.setStrokeWidth(everySize);
        centerLocation = new float[num];
        for (int i = 0; i < num; i++) {
            centerLocation[i] = paddingSize * 1.5f + everySize * i * 1.5f;
        }

    }

    private void setAnim() {
        ValueAnimator animator;
        process = new float[num];
        for (int i = 0; i < num; i++) {
            animator = ValueAnimator.ofFloat(0f, processes[i]);
            animator.setDuration((long) (time * 1000));
            animator.setRepeatCount(0);
            animator.setInterpolator(new LinearInterpolator());
            final int finalI = i;
            animator.addUpdateListener(animation -> {
                process[finalI] = (float) animation.getAnimatedValue();
                postInvalidate();
            });
            animator.start();
        }
    }

    /**
     * 设置百分比
     *
     * @param process 两个百分比。
     */

    public void setProcess(float[] process) {
        this.processes = process;
        start();
    }

    public void start() {
        isStart = true;
        setAnim();
    }

    private int dp2px(float dp) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics);
    }
}

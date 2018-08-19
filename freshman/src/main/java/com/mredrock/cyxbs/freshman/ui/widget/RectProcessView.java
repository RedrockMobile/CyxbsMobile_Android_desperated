package com.mredrock.cyxbs.freshman.ui.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.mredrock.cyxbs.freshman.R;

/*
 by Cynthia at 2018/8/13
 description : 柱形进度条自定义View
 */
public class RectProcessView extends View {

    private int num;
    private float time;
    private int max;

    private boolean isStart;

    private int[] processes;
    private float[] dashLocation;
    private float[] columnarLocation;
    private float[] current;
    private String[] colors;
    private String[] subject;
    private Paint processPaint, dashPaint;
    private TextPaint textPaint;
    private Path mPath;
    private Rect rect = new Rect();
    private RectF rectF = new RectF();

    public RectProcessView(Context context) {
        this(context, null);
    }

    public RectProcessView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RectProcessView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(attrs);
    }

    private void initAttr(AttributeSet attr) {
        TypedArray typeArray = getContext().obtainStyledAttributes(attr, R.styleable.RectProcessView);
        CharSequence[] colors = typeArray.getTextArray(R.styleable.RectProcessView_processColor);
        CharSequence[] processes = typeArray.getTextArray(R.styleable.RectProcessView_processNumber);
        time = typeArray.getFloat(R.styleable.RectProcessView_time, 2);
        num = typeArray.getInt(R.styleable.RectProcessView_number, 3);
        max = typeArray.getInt(R.styleable.RectProcessView_maxNum, 120);
        CharSequence[] subjects = typeArray.getTextArray(R.styleable.RectProcessView_subjectName);
        typeArray.recycle();
        initData(colors, processes, subjects);
        isStart = false;
    }

    //    这个应该传给动态绘制的部分
    private void initData(CharSequence[] colors, CharSequence[] process, CharSequence[] subject) {
        this.colors = new String[num * 2];
        processes = new int[num];
        this.subject = new String[num];
        for (int i = 0; i < num; i++) {
            processes[i] = Integer.parseInt(String.valueOf(process[i]));
            this.subject[i] = String.valueOf(subject[i]);
        }
        for (int i = 0; i < num * 2; i++) {
            this.colors[i] = String.valueOf(colors[i]);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize;
        if (widthMode == MeasureSpec.EXACTLY) {
            heightSize = (int) (widthSize / 6 * 5 * 1.25f);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY);
        } else if (widthMode == MeasureSpec.AT_MOST) {
            widthSize = Math.max(widthSize, dp2px(300));
            heightSize = (int) (widthSize / 6 * 5 * 1.25f);
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY);
        }
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    private void setData(int width, int height) {
        float everyWidthSize = (width - dp2px(15)) / 8;
        float everyHeight = height / 7;
        dashLocation = new float[7];
        for (int i = 0; i < 7; i++) {
            dashLocation[i] = everyHeight * (0.25f + i);
        }
        columnarLocation = new float[9];
        for (int i = 0; i < 9; i++) {
            if (i == 0)
                columnarLocation[i] = everyWidthSize;
            columnarLocation[i] = everyWidthSize * i + dp2px(15);
        }
        init(everyWidthSize);
    }

    private void init(float width) {
        processPaint = new Paint();
        processPaint.setAntiAlias(true);

        textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(dp2px(14));
        textPaint.setColor(Color.parseColor("#0083FF"));

        dashPaint = new Paint();
        dashPaint.setAntiAlias(true);
        dashPaint.setColor(Color.parseColor("#5954ACFF"));
        dashPaint.setStyle(Paint.Style.STROKE);
        dashPaint.setStrokeWidth(dp2px(0.5f));
        float fillWidth = width / 4 / 12 * 7;
        float whiteWidth = width / 4 / 12 * 5;
        dashPaint.setPathEffect(new DashPathEffect(new float[]{fillWidth, whiteWidth}, 0));

        mPath = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!isStart) {
            return;
        }
        setData(getWidth(), (int) (getHeight() / 1.25f));
        drawCoordinate(canvas);
        drawRects(canvas);
    }

    private void drawCoordinate(Canvas canvas) {
        for (int i = 0; i < 7; i++) {
            mPath.moveTo(columnarLocation[1], dashLocation[i]);
            mPath.lineTo(columnarLocation[7] + columnarLocation[1] / 2, dashLocation[i]);
            canvas.drawPath(mPath, dashPaint);
            mPath.reset();
        }
        int maxNum = max;
        int margin = max / 6;
        textPaint.setAlpha(165);
        for (int i = 0; i < 7; i++) {
            String t = String.valueOf(maxNum);
            textPaint.getTextBounds(t, 0, t.length(), rect);
            float height = rect.height();
            float width = rect.width();
            canvas.drawText(t, columnarLocation[0] * 1.5f - width / 2, dashLocation[i] + height / 2, textPaint);
            maxNum = maxNum - margin;
        }

        textPaint.setAlpha(220);
        for (int i = 0; i < num; i++) {
            textPaint.getTextBounds(subject[i], 0, subject[i].length(), rect);
            float height = rect.height();
            StaticLayout layout = new StaticLayout(subject[i], textPaint, (int) columnarLocation[1], Layout.Alignment.ALIGN_CENTER, 1.0f, 0f, true);
            canvas.translate(columnarLocation[2 * (i + 1)] - columnarLocation[0] / 2, dashLocation[6] + height / 2);
            layout.draw(canvas);
            canvas.translate(-(columnarLocation[2 * (i + 1)] - columnarLocation[0] / 2), -(dashLocation[6] + height / 2));
        }
        for (int i = 0; i < num; i++) {
            rectF.left = columnarLocation[2 * (i + 1)];
            rectF.right = columnarLocation[2 * (i + 1) + 1];
            rectF.bottom = dashLocation[6];
            rectF.top = dashLocation[6] - dp2px(5);
            processPaint.setColor(Color.parseColor(colors[2 * i]));
            canvas.drawRoundRect(rectF, 0, 0, processPaint);
        }
    }

    private void drawRects(Canvas canvas) {
        for (int i = 0; i < num; i++) {
            float pro = current[i] / max * dashLocation[6];
            rectF.left = columnarLocation[2 * (i + 1)];
            rectF.right = columnarLocation[2 * (i + 1) + 1];
            rectF.bottom = dashLocation[6];
            rectF.top = dashLocation[6] - pro;
            int[] color = {Color.parseColor(colors[2 * i + 1]), Color.parseColor(colors[2 * i])};
            float[] pos = {0f, 1f};
            LinearGradient shader = new LinearGradient(0, 0, 0, pro,
                    color, pos, Shader.TileMode.CLAMP);
            processPaint.setColor(Color.parseColor(colors[2 * i]));
            processPaint.setShader(shader);
            canvas.drawRoundRect(rectF, 5, 5, processPaint);

            String pe = String.valueOf((int) current[i]) + "人";
            textPaint.getTextBounds(pe, 0, pe.length(), rect);
            float h = rect.height();
            textPaint.setColor(Color.parseColor("#ccFF5A5A"));
            if (pe.length() < "100人".length())
                canvas.drawText(pe, columnarLocation[2 * (i + 1)] + columnarLocation[0] / 4, columnarLocation[6] - pro - h, textPaint);
            else
                canvas.drawText(pe, columnarLocation[2 * (i + 1)], columnarLocation[6] - pro - h, textPaint);
        }
    }

    public void setAnim() {
        ValueAnimator animator;
        current = new float[num];
        for (int i = 0; i < num; i++) {
            animator = ValueAnimator.ofFloat(0, processes[i]);
            animator.setDuration((long) (time * 1000));
            animator.setRepeatCount(0);
            animator.setInterpolator(new LinearInterpolator());
            final int finalI = i;
            animator.addUpdateListener(animation -> {
                current[finalI] = (float) animation.getAnimatedValue();
                postInvalidate();
            });
            animator.start();
        }
    }

    public void setSubject(String[] subject) {
        this.subject = subject;
    }

    public void setAnim(int[] processes, float time) {
        this.processes = processes;
        this.time = time;
        start();
    }

    public void start() {
        isStart = true;
        setAnim();
    }

    public void setMax(int max) {
        this.max = max;
    }

    private int dp2px(float dp) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics);
    }
}

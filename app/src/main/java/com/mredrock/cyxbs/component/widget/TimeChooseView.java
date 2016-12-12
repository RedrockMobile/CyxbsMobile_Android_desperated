package com.mredrock.cyxbs.component.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.mredrock.cyxbs.util.DensityUtils;
import com.mredrock.cyxbs.util.LogUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by ：AceMurder
 * Created on ：2016/11/11
 * Created for : CyxbsMobile_Android.
 * Enjoy it !!!
 */

public class TimeChooseView extends FrameLayout {
    private static final String TAG = "TimeChooseView";
    private final int width = (int) ((DensityUtils.getScreenWidth(getContext()) - DensityUtils.dp2px(getContext(), 50)) / 7);
    private int height = (int) DensityUtils.dp2px(getContext(), 85);
    private Context context;
    private TextView[][] chooseTextView = new TextView[7][7];
    private List<Position> positions = new ArrayList<>();
    private int startX, startY, endX, endY;
    private Paint mPaint;


    public TimeChooseView(Context context) {
        super(context);
        initPaint();

    }

    public TimeChooseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        /* 如果超大屏幕课表太短了，给它填满屏幕 */
        initPaint();
        int screeHeight = DensityUtils.getScreenHeight(context);
        if (DensityUtils.px2dp(context, screeHeight) > 700)
            height = screeHeight / 6;
        setWillNotDraw(false);
    }

    public TimeChooseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN){
            startX = (int) event.getX();
            startY = (int) event.getY();
        }

        if (event.getAction() == MotionEvent.ACTION_UP){
            endX = (int) event.getX();
            endY = (int) event.getY();
        }
        int distance = (int) Math.sqrt(Math.pow(startX - endX,2) + Math.pow(startY - endY , 2));
        if (distance <= 20){
            int x = (int) (event.getX() / getWidth() * 7);
            int y = (int) (event.getY() / getHeight() * 6);
            Log.d(LogUtils.makeLogTag(this.getClass()),x +"   "+y);
            createTextView(x,y);
        }
        return true;
    }


    public List<Position> getPositions() {
        return positions;
    }

    public void setPositions(List<Position> positions) {
        this.positions.clear();
        this.positions.addAll(positions);
        for (Position p : positions)
            createTextView(p.getX(),p.getY(),false);
    }


    private void createTextView(int x , int y){
        createTextView(x,y,true);
    }

    private void createTextView(int x , int y, boolean isAdd){
        if (chooseTextView[x][y] == null){
            chooseTextView[x][y] = new TextView(context);
            chooseTextView[x][y].setBackgroundColor(Color.parseColor("#41a2ff"));
            int mTop = getHeight() / 6 * y ;
            int mLeft = width * x;
            int mWidth = width;
            int mHeight = height;
            mHeight = getHeight() / 6;
            LayoutParams flParams = new LayoutParams((mWidth - DensityUtils.dp2px(getContext(), 1.5f)), (mHeight - DensityUtils.dp2px(getContext(), 1.5f)));
            flParams.topMargin = (mTop + DensityUtils.dp2px(getContext(), 1f));
            flParams.leftMargin = (mLeft + DensityUtils.dp2px(getContext(), 1f));
            chooseTextView[x][y].setLayoutParams(flParams);
            chooseTextView[x][y].setOnClickListener((view ->{
                removeView(chooseTextView[x][y]);
                positions.remove(new Position(x,y));
            }));
        }
        if (chooseTextView[x][y].getParent() == null){
            if (isAdd)
                positions.add(new Position(x,y));
            addView(chooseTextView[x][y]);
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 1; i < 8; i++) {
            canvas.drawLine(getWidth() / 7 * i - DensityUtils.dp2px(context, (float) (i * 1.5)), 0, getWidth() / 7 * i - DensityUtils.dp2px(context, (float) (i * 1.5)) , getHeight(), mPaint);
        }

        for (int i = 1; i < 6; i++) {
            canvas.drawLine(0,getHeight() / 6 * i, getWidth() - DensityUtils.dp2px(context , 10.5f) ,getHeight() / 6 * i, mPaint);
        }

    }

    private void initPaint(){
        mPaint = new Paint();
        mPaint.setColor(Color.parseColor("#F6F6F6"));
     //   mPaint.setColor(Color.RED);

        mPaint.setStrokeWidth(DensityUtils.dp2px(context,2));
    }
}

package com.mredrock.freshmanspecial.units;

/**
 * Created by zia on 17-8-5.
 */

public class ChartData{
    private int percentage = 0;
    private int color = CircleChart.defaultColor;
    private int strokeColor = CircleChart.defaultColor;
    private int textColor = CircleChart.defaultTextColor;
    private int backgroundStrokeColor = CircleChart.defaultStrokeColor;
    private int backgroundColor = CircleChart.defultBackgroundColor;
    private int speed = 1;
    private String text = "data";
    private float radius = 0;

    public int getStrokeColor() {
        return strokeColor;
    }

    public void setStrokeColor(int strokeColor) {
        this.strokeColor = strokeColor;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getBackgroundStrokeColor() {
        return backgroundStrokeColor;
    }

    public void setBackgroundStrokeColor(int backgroundStrokeColor) {
        this.backgroundStrokeColor = backgroundStrokeColor;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

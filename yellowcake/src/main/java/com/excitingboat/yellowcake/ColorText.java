package com.excitingboat.yellowcake;

/**
 * Created by PinkD on 2016/8/8.
 * ColorText
 */

public class ColorText {
    private int color;
    private String text;

    public ColorText(int color, String text) {
        this.color = color;
        this.text = text;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
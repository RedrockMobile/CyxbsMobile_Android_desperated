package com.mredrock.cyxbsmobile.model.social;

import com.mredrock.cyxbsmobile.model.RedrockApiWrapper;

import java.util.List;

/**
 * Created by mathiasluo on 16-4-22.
 */
public class BBDDNews extends RedrockApiWrapper<List<BBDDNewsContent>> {

    public static final int CYXW = 1;
    public static final int JWZX = 2;
    public static final int XSJZ = 3;
    public static final int XWGG = 4;
    public static final int BBDD = 5;
    public static final String LISTNEWS = "6";

    public static final int SHOT_ARTICLE = 1;
    public static final int LIST_ARTICLE = 2;
    public static final int JWZX_ARTICLE = 3;


    public String page;


}
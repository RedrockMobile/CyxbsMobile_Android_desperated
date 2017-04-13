package com.mredrock.cyxbs.model.social;

import com.mredrock.cyxbs.model.RedrockApiWrapper;

import java.util.List;

/**
 * Created by mathiasluo on 16-4-22.
 */
public class BBDDNews extends RedrockApiWrapper<List<BBDDNewsContent>> {
    public static final int BBDD = 5;
    public static final int TOPIC_ARTICLE = 6;
    public String page;
}
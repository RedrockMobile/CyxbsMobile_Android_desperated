package com.mredrock.cyxbs.model.lost;

import com.google.gson.annotations.SerializedName;

/**
 * Model of the wrapper of lost and found API <br>
 * http://hongyan.cqupt.edu.cn/laf/api/view/lost/all/1
 *
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

public class LostWrapper<T> {

    @SerializedName("total")
    public int numberOfItems;   // number of total items
    @SerializedName("per_page")
    public int numberOfItemsPerPage; // number of items in single page
    @SerializedName("current_page")
    public int currentPage; // page number of current page, the first page is 1
    @SerializedName("last_page")
    public int lastPage;    // the number of last page, require a page bigger than this will get a empty response with code 200
    @SerializedName("next_page_url")
    public String nextPageUrl;  // useless, but will be null if there is no next page
    @SerializedName("prev_page_url")
    public Object prevPageUrl;  // useless, but will be null if there is no previous page
    @SerializedName("from")
    public int from;    // the count number (not the id) of the first item
    @SerializedName("to")
    public int to;      // the count number (not the id) of the first item
    @SerializedName("data")
    public T data; // array of items

}

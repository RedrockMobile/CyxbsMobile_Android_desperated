package com.mredrock.cyxbs.model.lost;

import com.google.gson.annotations.SerializedName;

/**
 * Model of the status of lost and found API <br>
 *      for deserialization of most API exception.
 *
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

public class LostStatus {
    @SerializedName("status")
    public String status;  // The information which server return for us and we can show it to user directly.
                    // However, if you want to catch the exception, the http status code will be more valuable
}

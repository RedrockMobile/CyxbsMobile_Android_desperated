package com.mredrock.cyxbsmobile.network.service;

import com.mredrock.cyxbsmobile.model.MovieResult;
import com.mredrock.cyxbsmobile.model.Subject;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by cc on 16/1/20.
 */
public interface RedrockApiService {

    String MOVIE_URL = "https://api.douban.com/v2/movie/top250";

    @GET
    Observable<MovieResult<List<Subject>>> getTopMovie(@Url String url, @Query("start") int start, @Query("count") int count);

}

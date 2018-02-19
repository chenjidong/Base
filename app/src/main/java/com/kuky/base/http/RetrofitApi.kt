package com.kuky.base.http

import com.kuky.base.entity.News
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @author Kuky
 */
interface RetrofitApi {

    @GET("toutiao/index")
    fun getNews(@Query("type") type: String, @Query("key") key: String): Observable<News>
}
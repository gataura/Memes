package com.memes.`fun`.api.service

import com.memes.`fun`.api.model.YapxGifs
import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface YapxClient {

    @GET("API/gifs.json/?category=funny&size=all&order_by=random&access_token=e5129c344b26f4c335a51c6138b17083")
    @Headers("Content-Type: application/json")
    fun getMemes(@Query("get") offset: Int): Flowable<List<YapxGifs>>
}
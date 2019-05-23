package com.memes.`fun`.api.service

import com.memes.`fun`.api.model.Memes
import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.Path

interface ImgClient {

    @GET("gimme{lang}")
    fun getMemes(@Path("lang") lang: String?): Flowable<Memes>

}
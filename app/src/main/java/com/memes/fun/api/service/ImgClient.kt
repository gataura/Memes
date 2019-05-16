package com.memes.`fun`.api.service

import com.memes.`fun`.api.model.Memes
import io.reactivex.Flowable
import retrofit2.http.GET

interface ImgClient {

    @GET("gimme")
    fun getMemes(): Flowable<Memes>

}
package com.memes.`fun`.api.service

import com.memes.`fun`.helper.Constants.BASE_URL
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class mImgClient {

    private var builder = Retrofit
        .Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())

    private val retrofit: Retrofit by lazy {
        builder.build()
    }

    private val client: ImgClient by lazy {
        retrofit.create(ImgClient::class.java)
    }

    fun build() = client

}
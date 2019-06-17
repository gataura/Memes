package com.memes.`fun`.api.service

import com.google.gson.GsonBuilder
import com.memes.`fun`.helper.Constants.BASE_URL_YAPX
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class mGifsClient {


    private var builder = Retrofit
        .Builder()
        .baseUrl(BASE_URL_YAPX)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())

    private val retrofit: Retrofit by lazy {
        builder.build()
    }

    private val client: YapxClient by lazy {
        retrofit.create(YapxClient::class.java)
    }

    fun build() = client

}
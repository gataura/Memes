package com.memes.`fun`.api.model

class Memes (
    private var url: String = "",
    private var title: String = ""
) {

    fun getUrl(): String {
        return url
    }

    fun getTitle(): String {
        return title
    }

}
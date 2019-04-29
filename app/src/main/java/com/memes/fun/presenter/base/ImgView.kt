package com.memes.`fun`.presenter.base

interface ImgView: BaseView {

    fun onNextPage()
    fun onLoad()
    fun onError(t: Throwable?)

}
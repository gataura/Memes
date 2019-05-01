package com.memes.`fun`.presenter.base

import com.ceylonlabs.imageviewpopup.ImagePopup
import com.google.android.gms.ads.InterstitialAd
import com.memes.`fun`.database.AppDatabase

interface ImgView: BaseView {

    fun onNextPage()
    fun onLoad()
    fun onError(t: Throwable?)
    fun getImgPopup():ImagePopup
    fun getDb(): AppDatabase
    fun getAd(): InterstitialAd
}
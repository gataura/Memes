package com.memes.`fun`.presenter.base

import android.content.Intent
import android.content.SharedPreferences
import com.ceylonlabs.imageviewpopup.ImagePopup
import com.google.android.gms.ads.InterstitialAd
import com.google.firebase.analytics.FirebaseAnalytics
import com.memes.`fun`.database.AppDatabase

interface ImgView: BaseView {

    fun onNextPage()
    fun onLoad()
    fun onError(t: Throwable?)
    fun getImgPopup():ImagePopup
    fun getDb(): AppDatabase
    fun getAd(): InterstitialAd
    fun startIntent(sharingIntent: Intent)
    fun getPrefs(): String
    fun getOpenAd(): Int
    fun getAdCounter(): Int
    fun getFirebaseAn(): FirebaseAnalytics
}
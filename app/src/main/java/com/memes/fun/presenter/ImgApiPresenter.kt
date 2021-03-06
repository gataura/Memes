package com.memes.`fun`.presenter

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import com.memes.`fun`.R
import com.memes.`fun`.api.model.Memes
import com.memes.`fun`.presenter.base.ImgView
import com.memes.`fun`.adapter.MemesRowView
import com.memes.`fun`.api.model.YapxGifs
import com.memes.`fun`.api.service.mGifsClient
import com.memes.`fun`.api.service.mImgClient
import com.memes.`fun`.database.AppDatabase
import com.memes.`fun`.helper.Constants.OFFSET
import com.memes.`fun`.presenter.base.BasePresenter
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.processors.PublishProcessor
import io.reactivex.schedulers.Schedulers



class ImgApiPresenter: BasePresenter<ImgView>(), IImgPresenter {

    private var memesList = mutableListOf<Memes>()

    var memes = Memes()

    var loading = false

    var counter = 0
    var adCounter = 0

    var compositeDisposable = CompositeDisposable()
    private var pagination = PublishProcessor.create<Int>()

    private val client = mImgClient().build()


    override fun getMemesCount(): Int {
        return memesList.size
    }

    override fun onBindMemesRowViewAtPosition(position: Int, rowView: MemesRowView) {
        memes = memesList[position]
        rowView.setImage(memes.url)
        rowView.setTitle(memes.title)
        rowView.setLikes(memes.likes)
        rowView.checkLiked(memes.liked)
        isMemesInDb(memes, view?.getDb() as AppDatabase, rowView)
    }


    fun picClickAction(imageView: ImageView) {
        val imagePopup = view?.getImgPopup()
        imagePopup?.initiatePopup(imageView.drawable)
        imagePopup?.viewPopup()
        val bundle = Bundle()
        bundle.putString("imageClick", "imageClick")
        view?.getFirebaseAn()?.logEvent("imageClick", bundle)
        counterAd()
    }

    fun onShareClickAction(adapterPosition: Int) {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "LoL, look at this memes: ")
        sharingIntent.putExtra(Intent.EXTRA_TEXT, memesList[adapterPosition].url)
        val bundle = Bundle()
        bundle.putString("shareClick", "shareClick")
        view?.getFirebaseAn()?.logEvent("shareClick", bundle)
        view?.startIntent(sharingIntent)
    }

    fun onLikeIconClickAction(img: ImageView, adapterPosition: Int, likesCount: TextView) {

        if (img.tag == "liked") {
            img.setImageResource(R.drawable.like_outline)
            img.tag = "not liked"
            memesList[adapterPosition].liked = false
            likesCount.text = (memesList[adapterPosition].likes - 1).toString()
        } else {
            img.setImageResource(R.drawable.like_filled)
            img.tag = "liked"
            memesList[adapterPosition].liked = true
            likesCount.text = (memesList[adapterPosition].likes + 1).toString()
        }
        val bundle = Bundle()
        bundle.putString("likeClick", "likeClick")
        view?.getFirebaseAn()?.logEvent("likeClick", bundle)
        counterAd()

    }

    fun onSaveIconClickAction(img: ImageView, adapterPosition: Int) {
        if (img.tag == "saved") {
            img.setImageResource(R.drawable.save_icon_outline_24)
            img.tag = "not saved"
            deleteFromDb(memesList[adapterPosition], view?.getDb() as AppDatabase)
        } else {
            img.setImageResource(R.drawable.save_icon_24)
            img.tag = "saved"
            saveToDb(memesList[adapterPosition], view?.getDb() as AppDatabase)
        }
        val bundle = Bundle()
        bundle.putString("saveClick", "saveClick")
        view?.getFirebaseAn()?.logEvent("saveClick", bundle)
        counterAd()
    }

    override fun onNextPage() {
        loading = true
        for (i in 0 until 10) {
            pagination.onNext(OFFSET)
        }
        view?.onLoad()
    }

    fun loadItems() {

        val disposable: Disposable = pagination
            .onBackpressureDrop()
            .concatMap {
                loading = true
                getMemes(OFFSET)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                it.likes = (12..470).random()
                memesList.add(it)
                loading = false
                view?.onLoad()
            }, {
                view?.onError(it)
            })
        compositeDisposable.add(disposable)
        pagination.onNext(OFFSET)

    }


    @SuppressLint("CheckResult")
    fun getItemsFromDb(db: AppDatabase) {
        Observable.fromCallable { db.memesDao().getAll() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                memesList.clear()
                memesList.addAll(it)
                view?.onLoad()
            }, {
                view?.onError(it)
            })
    }

    private fun getMemes(offset: Int): Flowable<Memes> {
        return client.getMemes(view?.getPrefs())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    @SuppressLint("CheckResult")
    fun saveToDb(data: Memes, db: AppDatabase) {

        Completable.fromAction{ db.memesDao().insert(data) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({}, {
                Log.d("SaveToDb", "Again", it)
            })

    }

    @SuppressLint("CheckResult")
    fun deleteFromDb(data:Memes, db: AppDatabase) {

        Completable.fromAction{ db.memesDao().delete(data) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
            }, {
                Log.d("SaveToDb", "Again", it)
            })

    }

    @SuppressLint("CheckResult")
    fun isMemesInDb(data: Memes, db: AppDatabase, rowView: MemesRowView) {

        Observable.fromCallable { db.memesDao().memesCount(data.url) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it > 0) {
                    rowView.setIcon(R.drawable.save_icon_24, "saved")
                }
            }

    }

    fun counterAd() {
        counter++
        if (counter % view!!.getOpenAd() == 0) {
            if (view?.getAd()!!.isLoaded) {
                if (adCounter != view!!.getAdCounter()) {
                    view?.getAd()?.show()
                    adCounter++
                }
            } else {
                Log.d("TAG", "The interstitial wasn't loaded yet.")
            }

        }
    }
}
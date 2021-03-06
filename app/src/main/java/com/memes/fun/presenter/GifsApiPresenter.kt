package com.memes.`fun`.presenter

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.memes.`fun`.R
import com.memes.`fun`.presenter.base.BasePresenter
import com.memes.`fun`.presenter.base.ImgView
import com.memes.`fun`.adapter.MemesRowView
import com.memes.`fun`.api.model.YapxGifs
import com.memes.`fun`.api.service.mGifsClient
import com.memes.`fun`.database.AppDatabase
import com.memes.`fun`.helper.Constants.OFFSET
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.processors.PublishProcessor
import io.reactivex.schedulers.Schedulers



class GifsApiPresenter: BasePresenter<ImgView>(), IImgPresenter {

    private var gifsList = mutableListOf<YapxGifs>()

    var gifs = YapxGifs()

    var loading = false

    var counter = 0
    var adCounter = 0

    var compositeDisposable = CompositeDisposable()
    private var pagination = PublishProcessor.create<Int>()

    private val client = mGifsClient().build()


    override fun getMemesCount(): Int {
        return gifsList.size
    }

    override fun onBindMemesRowViewAtPosition(position: Int, rowView: MemesRowView) {
        gifs = gifsList[position]
        rowView.setImage(gifs.image_url)
        rowView.setLikes(gifs.likes)
        rowView.checkLiked(gifs.liked)
        isMemesInDb(gifs, view?.getDb() as AppDatabase, rowView)
    }


    fun picClickAction(imageView: ImageView) {
        val imagePopup = view?.getImgPopup()
        imagePopup?.initiatePopup(imageView.drawable)
        imagePopup?.viewPopup()
        counterAd()
    }

    fun onShareClickAction(adapterPosition: Int) {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "LoL, look at this memes: ")
        sharingIntent.putExtra(Intent.EXTRA_TEXT, gifsList[adapterPosition].image_url)
        view?.startIntent(sharingIntent)
        counterAd()
    }

    fun onLikeIconClickAction(img: ImageView, adapterPosition: Int, likesCount: TextView) {

        if (img.tag == "liked") {
            img.setImageResource(R.drawable.like_outline)
            img.tag = "not liked"
            gifsList[adapterPosition].liked = false
            likesCount.text = (gifsList[adapterPosition].likes - 1).toString()
        } else {
            img.setImageResource(R.drawable.like_filled)
            img.tag = "liked"
            gifsList[adapterPosition].liked = true
            likesCount.text = (gifsList[adapterPosition].likes + 1).toString()
        }

        counterAd()

    }

    fun onSaveIconClickAction(img: ImageView, adapterPosition: Int) {
        if (img.tag == "saved") {
            img.setImageResource(R.drawable.save_icon_outline_24)
            img.tag = "not saved"
            deleteFromDb(gifsList[adapterPosition], view?.getDb() as AppDatabase)
        } else {
            img.setImageResource(R.drawable.save_icon_24)
            img.tag = "saved"
            saveToDb(gifsList[adapterPosition], view?.getDb() as AppDatabase)
        }
        counterAd()
    }

    override fun onNextPage() {
        loading = true
        pagination.onNext(OFFSET)
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
                for (i in it) {
                    i.likes = (12..470).random()
                }
                gifsList.addAll(it)
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
        Observable.fromCallable { db.memesDao().getAllGifs() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                gifsList.clear()
                gifsList.addAll(it)
                view?.onLoad()
            }, {
                view?.onError(it)
            })
    }

    private fun getMemes(offset: Int): Flowable<List<YapxGifs>> {
        return client.getMemes(offset)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    @SuppressLint("CheckResult")
    fun saveToDb(data: YapxGifs, db: AppDatabase) {

        Completable.fromAction{ db.memesDao().insert(data) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({}, {
                Log.d("SaveToDb", "Again", it)
            })

    }

    @SuppressLint("CheckResult")
    fun deleteFromDb(data:YapxGifs, db: AppDatabase) {

        Completable.fromAction{ db.memesDao().delete(data) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
            }, {
                Log.d("SaveToDb", "Again", it)
            })

    }

    @SuppressLint("CheckResult")
    fun isMemesInDb(data: YapxGifs, db: AppDatabase, rowView: MemesRowView) {

        Observable.fromCallable { db.memesDao().gifsCount(data.image_url) }
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
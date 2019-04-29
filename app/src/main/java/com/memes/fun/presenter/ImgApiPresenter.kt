package com.memes.`fun`.presenter

import com.memes.`fun`.api.model.Memes
import com.memes.`fun`.presenter.base.BasePresenter
import com.memes.`fun`.presenter.base.ImgView
import com.memes.`fun`.adapter.MemesRowView
import com.memes.`fun`.api.service.mImgClient
import com.memes.`fun`.helper.Constants.OFFSET
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.processors.PublishProcessor
import io.reactivex.schedulers.Schedulers

class ImgApiPresenter: BasePresenter<ImgView>(), IImgPresenter {

    var memesList = mutableListOf<Memes>()


    var loading = false

    var compositeDisposable = CompositeDisposable()
    var pagination = PublishProcessor.create<Int>()
    private val client = mImgClient().build()

    override fun getMemesCount(): Int {
        return memesList.size
    }

    override fun onBindMemesRowViewAtPosition(position: Int, rowView: MemesRowView) {
        val memes: Memes = memesList[position]
        rowView.setImage(memes.getUrl())
        rowView.setTitle(memes.getTitle())
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
                memesList.add(it)
                loading = false
                view?.onLoad()
            }, {
                view?.onError(it)
            })
        compositeDisposable.add(disposable)
        pagination.onNext(OFFSET)

    }

    private fun getMemes(offset: Int): Flowable<Memes> {
        return client.getMemes()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}
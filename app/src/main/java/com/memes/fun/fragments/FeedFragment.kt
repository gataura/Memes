package com.memes.`fun`.fragments


import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ceylonlabs.imageviewpopup.ImagePopup
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds

import com.memes.`fun`.R
import com.memes.`fun`.adapter.MemesAdapter
import com.memes.`fun`.database.AppDatabase
import com.memes.`fun`.helper.Constants
import com.memes.`fun`.presenter.ImgApiPresenter
import com.memes.`fun`.presenter.base.ImgView

class FeedFragment : Fragment(), ImgView {

    private lateinit var presenter: ImgApiPresenter

    private lateinit var memesRecyclerView: RecyclerView
    private lateinit var memesAdapter: MemesAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var db: AppDatabase

    private var totalItemCount = 0
    private var lastVisibleItem = 0

    private lateinit var mInterstitialAd: InterstitialAd

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_feed, container, false)
        db = AppDatabase.getInstance(this.requireContext()) as AppDatabase

        presenter = ImgApiPresenter()
        presenter.bind(this)

        memesRecyclerView = view.findViewById(R.id.memes_recycler_view)
        memesRecyclerView.isNestedScrollingEnabled = false
        layoutManager = LinearLayoutManager(this.requireContext(), RecyclerView.VERTICAL, false)
        memesAdapter = MemesAdapter(presenter)
        memesRecyclerView.adapter = memesAdapter
        memesRecyclerView.layoutManager = layoutManager

        MobileAds.initialize(this.requireContext(), "ca-app-pub-3940256099942544~3347511713")

        mInterstitialAd = InterstitialAd(this.requireContext())
        mInterstitialAd.adUnitId = "ca-app-pub-3940256099942544/1033173712"
        mInterstitialAd.loadAd(AdRequest.Builder().build())

        mInterstitialAd.adListener = object: AdListener() {

            override fun onAdClosed() {
                mInterstitialAd.loadAd(AdRequest.Builder().build())
            }

        }

        setUpLoadMoreListener()
        presenter.loadItems()

        return view
    }

    private fun refreshRepositoriesList() {
        memesAdapter.notifyDataSetChanged()
    }

    private fun setUpLoadMoreListener() {
        memesRecyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                totalItemCount = memesRecyclerView.layoutManager!!.itemCount
                lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                if (!presenter.loading && totalItemCount <= (lastVisibleItem + Constants.VISIBLE_TRESHOLD)) {
                    presenter.onNextPage()
                }
            }
        })
    }


    override fun onError(t: Throwable?) {
        showToast(t.toString())
    }

    override fun onLoad() {
        refreshRepositoriesList()
    }

    override fun onNextPage() {
        presenter.onNextPage()
    }

    override fun getImgPopup(): ImagePopup {
        val imagePopup = ImagePopup(this.requireContext())
        imagePopup.windowHeight = 800
        imagePopup.windowWidth = 800
        imagePopup.backgroundColor = Color.BLACK
        imagePopup.isFullScreen = true
        imagePopup.isHideCloseIcon = true
        imagePopup.isImageOnClickClose = true
        return imagePopup
    }

    override fun getDb(): AppDatabase {
        return db
    }

    override fun getAd(): InterstitialAd {
        return mInterstitialAd
    }

    private fun showToast(text: String) {
        Toast.makeText(this.requireContext(), text, Toast.LENGTH_SHORT).show() //функция для показа Toast сообщений
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.compositeDisposable.dispose()
        presenter.unbind()
    }


}

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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.ceylonlabs.imageviewpopup.ImagePopup
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds

import com.memes.`fun`.R
import com.memes.`fun`.adapter.MemesAdapter
import com.memes.`fun`.database.AppDatabase
import com.memes.`fun`.presenter.ImgApiPresenter
import com.memes.`fun`.presenter.base.ImgView

class SavedFragment : Fragment(), ImgView {

    private lateinit var presenter: ImgApiPresenter

    private lateinit var memesRecyclerView: RecyclerView
    private lateinit var memesAdapter: MemesAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var db: AppDatabase
    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout

    private lateinit var mInterstitialAd: InterstitialAd

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_saved, container, false)
        db = AppDatabase.getInstance(this.requireContext()) as AppDatabase

        presenter = ImgApiPresenter()
        presenter.bind(this)

        mSwipeRefreshLayout = view.findViewById(R.id.saved_container)

        memesRecyclerView = view.findViewById(R.id.saved_recycler_view)
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

        presenter.getItemsFromDb(db)

        mSwipeRefreshLayout.setOnRefreshListener {
            presenter.getItemsFromDb(db)
            mSwipeRefreshLayout.isRefreshing = false
        }

        return view
    }

    override fun getDb(): AppDatabase {
        return db
    }

    private fun refreshRepositoriesList() {
        memesAdapter.notifyDataSetChanged()
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

    override fun onError(t: Throwable?) {
        showToast(t.toString())
    }

    override fun onLoad() {
        refreshRepositoriesList()
    }

    override fun onNextPage() {

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

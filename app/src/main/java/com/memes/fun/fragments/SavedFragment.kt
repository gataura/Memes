package com.memes.`fun`.fragments


import android.content.Intent
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
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.database.*

import com.memes.`fun`.R
import com.memes.`fun`.adapter.GifsAdapter
import com.memes.`fun`.adapter.MemesAdapter
import com.memes.`fun`.database.AppDatabase
import com.memes.`fun`.presenter.GifsApiPresenter
import com.memes.`fun`.presenter.ImgApiPresenter
import com.memes.`fun`.presenter.base.ImgView
import java.util.*

class SavedFragment : Fragment(), ImgView {

    private lateinit var presenter: ImgApiPresenter
    private lateinit var presenterGifs: GifsApiPresenter

    private lateinit var memesRecyclerView: RecyclerView
    private lateinit var memesAdapter: MemesAdapter
    private lateinit var gifsAdapter: GifsAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var db: AppDatabase
    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout

    private lateinit var mInterstitialAd: InterstitialAd

    lateinit var myRef: DatabaseReference
    lateinit var database: FirebaseDatabase
    lateinit var firebaseAnalytics: FirebaseAnalytics
    var dbValue: Long = 0
    var adCounter: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_saved, container, false)
        db = AppDatabase.getInstance(this.requireContext()) as AppDatabase

        FirebaseApp.initializeApp(this.requireContext())
        database = FirebaseDatabase.getInstance()
        myRef = database.reference

        myRef.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                dbValue = p0.child("openAd").value as Long
                adCounter = p0.child("adCounter").value as Long
            }

        })

        firebaseAnalytics = FirebaseAnalytics.getInstance(this.requireContext())

        MobileAds.initialize(this.requireContext(), "ca-app-pub-3940256099942544~3347511713")

        mInterstitialAd = InterstitialAd(this.requireContext())
        mInterstitialAd.adUnitId = "ca-app-pub-3940256099942544/1033173712"
        mInterstitialAd.loadAd(AdRequest.Builder().build())

        mInterstitialAd.adListener = object: AdListener() {

            override fun onAdClosed() {
                mInterstitialAd.loadAd(AdRequest.Builder().build())
            }

        }

        presenter = ImgApiPresenter()
        presenterGifs = GifsApiPresenter()

        memesAdapter = MemesAdapter(presenter)
        gifsAdapter = GifsAdapter(presenterGifs)
        if (Locale.getDefault().language == "ru") {
            presenterGifs.bind(this)
        } else {
            presenter.bind(this)
        }

        memesRecyclerView = view.findViewById(R.id.saved_recycler_view)
        memesRecyclerView.isNestedScrollingEnabled = false
        layoutManager = LinearLayoutManager(this.requireContext(), RecyclerView.VERTICAL, false)

        if (Locale.getDefault().language == "ru") {
            memesRecyclerView.adapter = gifsAdapter
            memesRecyclerView.layoutManager = layoutManager

            presenterGifs.getItemsFromDb(db)
            mSwipeRefreshLayout = view.findViewById(R.id.saved_container)
            mSwipeRefreshLayout.setOnRefreshListener {
                presenterGifs.getItemsFromDb(db)
                mSwipeRefreshLayout.isRefreshing = false
            }
        } else {
            memesRecyclerView.adapter = memesAdapter
            memesRecyclerView.layoutManager = layoutManager


            presenter.getItemsFromDb(db)
            mSwipeRefreshLayout = view.findViewById(R.id.saved_container)
            mSwipeRefreshLayout.setOnRefreshListener {
                presenter.getItemsFromDb(db)
                mSwipeRefreshLayout.isRefreshing = false
            }
        }

        return view
    }

    override fun getDb(): AppDatabase {
        return db
    }

    private fun refreshRepositoriesList() {
        if (Locale.getDefault().language == "ru") {
            gifsAdapter.notifyDataSetChanged()
        } else {
            memesAdapter.notifyDataSetChanged()
        }
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

    override fun startIntent(sharingIntent: Intent) {
        this.requireContext().startActivity(Intent.createChooser(sharingIntent, "Share via"))
    }

    private fun showToast(text: String) {
        Toast.makeText(this.requireContext(), text, Toast.LENGTH_SHORT).show() //функция для показа Toast сообщений
    }

    override fun getPrefs(): String {
        return ""
    }

    override fun getOpenAd(): Int {
        return dbValue.toInt()
    }

    override fun getAdCounter(): Int {
        return adCounter.toInt()
    }

    override fun getFirebaseAn(): FirebaseAnalytics {
        return firebaseAnalytics
    }

    override fun onDestroy() {
        super.onDestroy()
        if (Locale.getDefault().language == "ru") {
            presenterGifs.compositeDisposable.dispose()
            presenterGifs.unbind()
        } else {
            presenter.compositeDisposable.dispose()
            presenter.unbind()
        }
    }


}

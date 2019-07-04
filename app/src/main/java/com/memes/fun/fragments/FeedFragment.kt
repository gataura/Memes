package com.memes.`fun`.fragments


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.database.*

import com.memes.`fun`.R
import com.memes.`fun`.adapter.GifsAdapter
import com.memes.`fun`.adapter.MemesAdapter
import com.memes.`fun`.database.AppDatabase
import com.memes.`fun`.helper.Constants
import com.memes.`fun`.presenter.GifsApiPresenter
import com.memes.`fun`.presenter.ImgApiPresenter
import com.memes.`fun`.presenter.base.ImgView
import java.util.*

class FeedFragment : Fragment(), ImgView {

    private lateinit var presenter: ImgApiPresenter
    private lateinit var presenterGifs: GifsApiPresenter

    private lateinit var memesRecyclerView: RecyclerView
    private lateinit var memesAdapter: MemesAdapter
    private lateinit var gifsAdapter: GifsAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var db: AppDatabase

    private var totalItemCount = 0
    private var lastVisibleItem = 0

    lateinit var myRef: DatabaseReference
    lateinit var database: FirebaseDatabase
    var dbValue: Long = 0
    var adCounter: Long = 0

    lateinit var prefs: SharedPreferences

    private var lang: String = ""

    var isFirstLoad = true

    private lateinit var mInterstitialAd: InterstitialAd

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_feed, container, false)
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

        MobileAds.initialize(this.requireContext(), "ca-app-pub-9561253976720525~2978570952")

        mInterstitialAd = InterstitialAd(this.requireContext())
        mInterstitialAd.adUnitId = "ca-app-pub-9561253976720525/4135793851"
        mInterstitialAd.loadAd(AdRequest.Builder().build())

        prefs = this.requireContext().getSharedPreferences("com.memes.`fun`", Context.MODE_PRIVATE)

        if (prefs.getBoolean("firstrun", true)) {
            setLanguages()
            prefs.edit().putBoolean("firstrun", false).apply()
        } else {
            lang = prefs.getString("language", "")
        }


        prefs.edit().putString("language", lang).apply()


        mInterstitialAd.adListener = object: AdListener() {

            override fun onAdClosed() {
                mInterstitialAd.loadAd(AdRequest.Builder().build())
            }

            override fun onAdLoaded() {
//                if (isFirstLoad) {
//                    mInterstitialAd.show()
//                    isFirstLoad = false
//                }
            }

        }

        presenter = ImgApiPresenter()
        presenterGifs = GifsApiPresenter()

        memesAdapter = MemesAdapter(presenter)
        gifsAdapter = GifsAdapter(presenterGifs)

       /* if (Locale.getDefault().language == "ru") {
            presenterGifs.bind(this)
        } else {
            presenter.bind(this)
        }*/
        presenter.bind(this)

        memesRecyclerView = view.findViewById(R.id.memes_recycler_view)
        memesRecyclerView.isNestedScrollingEnabled = false
        layoutManager = LinearLayoutManager(this.requireContext(), RecyclerView.VERTICAL, false)

       /* if (Locale.getDefault().language == "ru") {
            memesRecyclerView.adapter = gifsAdapter
            memesRecyclerView.layoutManager = layoutManager

            setUpLoadMoreListenerGifs()
            presenterGifs.loadItems()
        } else {
            memesRecyclerView.adapter = memesAdapter
            memesRecyclerView.layoutManager = layoutManager


            setUpLoadMoreListener()
            presenter.loadItems()
        }*/
        memesRecyclerView.adapter = memesAdapter
        memesRecyclerView.layoutManager = layoutManager


        setUpLoadMoreListener()
        presenter.loadItems()

        return view
    }

    private fun refreshRepositoriesList() {
        /*if (Locale.getDefault().language == "ru") {
            gifsAdapter.notifyDataSetChanged()
        } else {
            memesAdapter.notifyDataSetChanged()
        }*/
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

    private fun setUpLoadMoreListenerGifs() {
        memesRecyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                totalItemCount = memesRecyclerView.layoutManager!!.itemCount
                lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                if (!presenterGifs.loading && totalItemCount <= (lastVisibleItem + Constants.VISIBLE_TRESHOLD)) {
                    presenterGifs.onNextPage()
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
        /*if (Locale.getDefault().language == "ru") {
            presenterGifs.onNextPage()
        } else {
            presenter.onNextPage()
        }*/
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

    override fun getPrefs(): String {
        return lang
    }

    override fun getOpenAd(): Int {
        return dbValue.toInt()
    }

    override fun getAdCounter(): Int {
        return adCounter.toInt()
    }

    override fun startIntent(sharingIntent: Intent) {
        this.requireContext().startActivity(Intent.createChooser(sharingIntent, "Share via"))
    }

    private fun showToast(text: String) {
        Toast.makeText(this.requireContext(), text, Toast.LENGTH_SHORT).show() //функция для показа Toast сообщений
    }

    override fun onDestroy() {
        super.onDestroy()
        /*if (Locale.getDefault().language == "ru") {
            presenterGifs.compositeDisposable.dispose()
            presenterGifs.unbind()
        } else {
            presenter.compositeDisposable.dispose()
            presenter.unbind()
        }*/
        presenter.compositeDisposable.dispose()
        presenter.unbind()
    }

    fun setLanguages() {
        when (Locale.getDefault().language) {
            "es" -> lang = "/memexico"
            "pt" -> lang = "/PORTUGALCARALHO"
            "hi" -> lang = "/IndianDankMemes"
            "in" -> lang = "/indonesia"
            "en" -> lang = ""
            "ru" -> lang = "/ANormalDayInRussia"
        }
    }


}

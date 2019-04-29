package com.memes.`fun`

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.memes.`fun`.adapter.MemesAdapter
import com.memes.`fun`.api.model.Memes
import com.memes.`fun`.helper.Constants.VISIBLE_TRESHOLD
import com.memes.`fun`.helper.find
import com.memes.`fun`.presenter.ImgApiPresenter
import com.memes.`fun`.presenter.base.ImgView

class MainActivity : AppCompatActivity(), ImgView {

    private val presenter = ImgApiPresenter()

    private lateinit var memesRecyclerView: RecyclerView
    private lateinit var memesAdapter: MemesAdapter
    private lateinit var layoutManager: LinearLayoutManager

    private var totalItemCount = 0
    private var lastVisibleItem = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter.bind(this)

        memesRecyclerView = find(R.id.memes_recycler_view)
        memesRecyclerView.isNestedScrollingEnabled = false
        layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        memesAdapter = MemesAdapter(presenter)
        memesRecyclerView.adapter = memesAdapter
        memesRecyclerView.layoutManager = layoutManager

        setUpLoadMoreListener()
        presenter.loadItems()
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

                if (!presenter.loading && totalItemCount <= (lastVisibleItem + VISIBLE_TRESHOLD)) {
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

    private fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show() //функция для показа Toast сообщений
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.compositeDisposable.dispose()
        presenter.unbind()
    }
}

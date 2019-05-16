package com.memes.`fun`.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.memes.`fun`.R
import com.memes.`fun`.presenter.GifsApiPresenter

class GifsAdapter(gifsPresenter: GifsApiPresenter): RecyclerView.Adapter<GifsViewHolder>() {

    var presenter: GifsApiPresenter = gifsPresenter

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: GifsViewHolder, position: Int) {
        presenter.onBindMemesRowViewAtPosition(position, holder)
    }

    override fun getItemCount(): Int {
        return presenter.getMemesCount()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GifsViewHolder {
        return GifsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.memes_item_view, parent, false), presenter)
    }

}
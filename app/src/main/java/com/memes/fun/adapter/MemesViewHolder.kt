package com.memes.`fun`.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.memes.`fun`.R
import com.memes.`fun`.presenter.ImgApiPresenter
import com.squareup.picasso.Picasso

class MemesViewHolder(itemView: View, presenter: ImgApiPresenter): RecyclerView.ViewHolder(itemView), MemesRowView {

    var memesImage: ImageView = itemView.findViewById(R.id.memesThumb)
    var memesTitle: TextView = itemView.findViewById(R.id.memesTitle)
    var saveIcon: ImageView = itemView.findViewById(R.id.save_icon)

    init {
        memesImage.setOnClickListener {
            presenter.picClickAction(memesImage)
        }

        saveIcon.setOnClickListener {
            presenter.onSaveIconClickAction(saveIcon, adapterPosition)
        }
    }


    override fun setImage(thumb: String) {
        Picasso.get()
            .load(thumb)
            .placeholder(R.drawable.progress_animation)
            .into(memesImage)
    }

    override fun setTitle(title: String) {
        memesTitle.text = title
    }

    override fun setIcon() {
        saveIcon.setImageResource(R.drawable.save_icon_24)
        saveIcon.tag = "saved"
    }


}
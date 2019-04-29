package com.memes.`fun`.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.memes.`fun`.R
import com.squareup.picasso.Picasso

class MemesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), MemesRowView {

    var memesImage: ImageView = itemView.findViewById(R.id.memesThumb)
    var memesTitle: TextView = itemView.findViewById(R.id.memesTitle)

    override fun setImage(thumb: String) {
        Picasso.get()
            .load(thumb)
            .placeholder(R.drawable.progress_animation)
            .into(memesImage)
    }

    override fun setTitle(title: String) {
        memesTitle.text = title
    }

}
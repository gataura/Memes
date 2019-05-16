package com.memes.`fun`.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.memes.`fun`.R
import com.memes.`fun`.presenter.GifsApiPresenter


class GifsViewHolder(itemView: View, presenter: GifsApiPresenter): RecyclerView.ViewHolder(itemView), MemesRowView {

    var memesImage: ImageView = itemView.findViewById(R.id.memesThumb)
    var memesTitle: TextView = itemView.findViewById(R.id.memesTitle)
    var saveIcon: ImageView = itemView.findViewById(R.id.save_icon)
    var shareIcon: ImageView = itemView.findViewById(R.id.share_icon)
    var likeIcon: ImageView = itemView.findViewById(R.id.like_icon)
    var likesCount: TextView = itemView.findViewById(R.id.likes_count)

    init {
        memesImage.setOnClickListener {
            presenter.picClickAction(memesImage)
        }

        saveIcon.setOnClickListener {
            presenter.onSaveIconClickAction(saveIcon, adapterPosition)
        }

        shareIcon.setOnClickListener {
            presenter.onShareClickAction(adapterPosition)
        }

        likeIcon.setOnClickListener {
            presenter.onLikeIconClickAction(likeIcon, adapterPosition, likesCount)
        }
    }

    override fun checkLiked(flag: Boolean) {
        if (flag) {
            likeIcon.setImageResource(R.drawable.like_filled)
            likeIcon.tag = "liked"
        } else {
            likeIcon.setImageResource(R.drawable.like_outline)
            likeIcon.tag = "not liked"
        }
    }

    override fun setImage(thumb: String) {
        /*Picasso.get()
            .load(thumb)
            .placeholder(R.drawable.progress_animation)
            .into(memesImage)*/

        Glide.with(itemView)
            .asGif()
            .load(thumb)
            .placeholder(R.drawable.progress_animation)
            .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
            .into(memesImage)
    }

    override fun setLikes(likes: Int) {
        likesCount.text = likes.toString()
    }

    override fun setTitle(title: String) {
        memesTitle.text = title
    }

    override fun setIcon(id: Int, status: String) {
        saveIcon.setImageResource(id)
        saveIcon.tag = status
    }


}
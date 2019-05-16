package com.memes.`fun`.adapter

interface MemesRowView {
    fun setImage(thumb: String)
    fun setTitle(title: String)
    fun setIcon(id: Int, status: String)
    fun setLikes(likes: Int)
    fun checkLiked(flag: Boolean)
}
package com.memes.`fun`.api.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "memesData")
class Memes (
    @PrimaryKey (autoGenerate = true) private var id: Int = 0,
    @ColumnInfo (name = "url") private var url: String ="",
    @ColumnInfo(name = "title") private var title: String = "",
    @ColumnInfo(name = "likes") private var likes: Int = 0,
    @ColumnInfo(name = "liked") private var liked: Boolean = false
) {

    fun getId(): Int {
        return id
    }

    fun getUrl(): String {
        return url
    }

    fun getTitle(): String {
        return title
    }

    fun getLikes(): Int {
        return likes
    }

    fun setLikes(likes: Int) {
        this.likes = likes
    }

    fun getLiked(): Boolean {
        return liked
    }

    fun setLiked(liked: Boolean) {
        this.liked = liked
    }


}
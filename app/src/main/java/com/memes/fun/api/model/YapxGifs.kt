package com.memes.`fun`.api.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gifsData")
class YapxGifs (
    @PrimaryKey (autoGenerate = true) private var id: Int = 0,
    @ColumnInfo(name = "imageId") private var image_id: Int =0,
    @ColumnInfo(name = "category") private var category: String = "",
    @ColumnInfo(name = "contentType") private var content_type: String = "",
    @ColumnInfo(name = "thumbnailUrl") private var thumbnail_url: String = "",
    @ColumnInfo(name = "imageUrl") private var image_url: String = "",
    @ColumnInfo(name = "imageSize") private var image_size: String = "",
    @ColumnInfo(name = "imageWidth") private var image_width: String = "",
    @ColumnInfo(name = "imageHeight") private var image_height: String = "",
    @ColumnInfo(name = "videoUrl") private var video_url: String = "",
    @ColumnInfo(name = "videoSize") private var video_size: String = "",
    @ColumnInfo(name = "videoWidth") private var video_width: String = "",
    @ColumnInfo(name = "videoHeight") private var video_height: String = "",
    @ColumnInfo(name = "keywords") private var keywords: String? = null,
    @ColumnInfo(name = "likes") private var likes: Int = 0,
    @ColumnInfo(name = "liked") private var liked: Boolean = false
) {

    fun getId(): Int {
        return id
    }

    fun getImage_url(): String {
        return image_url
    }

    fun getLiked(): Boolean {
        return liked
    }

    fun setLiked(liked: Boolean) {
        this.liked = liked
    }

    fun getImage_id(): Int {
        return image_id
    }

    fun getCategory(): String {
        return category
    }

    fun getContent_type(): String {
        return content_type
    }

    fun getThumbnail_url(): String {
        return thumbnail_url
    }

    fun getImage_size(): String {
        return image_size
    }

    fun getImage_width(): String {
        return image_width
    }

    fun getImage_height(): String {
        return image_height
    }

    fun getVideo_url(): String {
        return video_url
    }

    fun getVideo_size(): String {
        return video_size
    }

    fun getVideo_width(): String {
        return video_width
    }

    fun getVideo_height(): String {
        return video_height
    }

    fun getKeywords(): String? {
        return keywords
    }

    fun getLikes(): Int {
        return likes
    }

    fun setLikes(likes: Int) {
        this.likes = likes
    }


}
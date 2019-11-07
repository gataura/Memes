package com.memes.`fun`.api.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gifsData")
data class YapxGifs (
    @PrimaryKey (autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "imageId") var image_id: Int =0,
    @ColumnInfo(name = "category") var category: String = "",
    @ColumnInfo(name = "contentType") var content_type: String = "",
    @ColumnInfo(name = "thumbnailUrl") var thumbnail_url: String = "",
    @ColumnInfo(name = "imageUrl") var image_url: String = "",
    @ColumnInfo(name = "imageSize") var image_size: String = "",
    @ColumnInfo(name = "imageWidth") var image_width: String = "",
    @ColumnInfo(name = "imageHeight") var image_height: String = "",
    @ColumnInfo(name = "videoUrl") var video_url: String = "",
    @ColumnInfo(name = "videoSize") var video_size: String = "",
    @ColumnInfo(name = "videoWidth") var video_width: String = "",
    @ColumnInfo(name = "videoHeight") var video_height: String = "",
    @ColumnInfo(name = "keywords") var keywords: String? = null,
    @ColumnInfo(name = "likes") var likes: Int = 0,
    @ColumnInfo(name = "liked") var liked: Boolean = false
)
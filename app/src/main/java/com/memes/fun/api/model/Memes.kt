package com.memes.`fun`.api.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "memesData")
data class Memes (
    @PrimaryKey (autoGenerate = true) var id: Int = 0,
    @ColumnInfo (name = "url") var url: String ="",
    @ColumnInfo(name = "title") var title: String = "",
    @ColumnInfo(name = "likes") var likes: Int = 0,
    @ColumnInfo(name = "liked") var liked: Boolean = false
)
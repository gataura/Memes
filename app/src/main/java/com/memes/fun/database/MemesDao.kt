package com.memes.`fun`.database

import androidx.room.*
import com.memes.`fun`.api.model.Memes
import com.memes.`fun`.api.model.YapxGifs

@Dao
interface MemesDao {
    @Insert
    fun insert(memes: Memes)

    @Update
    fun update(memes: Memes)

    @Delete
    fun delete(memes: Memes)

    @Insert
    fun insert(gifs: YapxGifs)

    @Update
    fun update(gifs: YapxGifs)

    @Delete
    fun delete(gifs: YapxGifs)

    @Query("SELECT COUNT(*) from memesData WHERE url = :url")
    fun memesCount(url: String): Int

    @Query("SELECT * from memesData")
    fun getAll(): List<Memes>

    @Query("SELECT COUNT(*) from gifsData WHERE imageUrl = :url")
    fun gifsCount(url: String): Int

    @Query("SELECT * from gifsData")
    fun getAllGifs(): List<YapxGifs>
}
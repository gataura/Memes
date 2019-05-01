package com.memes.`fun`.database

import androidx.room.*
import com.memes.`fun`.api.model.Memes

@Dao
interface MemesDao {
    @Insert
    fun insert(memes: Memes)

    @Update
    fun update(memes: Memes)

    @Delete
    fun delete(memes: Memes)

    @Query("SELECT COUNT(*) from memesData WHERE url = :url")
    fun memesCount(url: String): Int

    @Query("SELECT * from memesData")
    fun getAll(): List<Memes>
}
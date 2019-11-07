package com.memes.`fun`.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.memes.`fun`.api.model.Memes
import com.memes.`fun`.api.model.YapxGifs

@Database(entities = [Memes::class, YapxGifs::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {


    abstract fun memesDao(): MemesDao

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        AppDatabase::class.java, "memes.db")
                        .build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }

    }
}
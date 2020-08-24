package com.service.codingtest.manager

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.service.codingtest.db.FavoriteDao
import com.service.codingtest.db.ImageDao
import com.service.codingtest.db.ImageRemoteKeyDao
import com.service.codingtest.model.response.FavoriteEntity

@Database(entities = [FavoriteEntity::class], version = 1)
abstract class AppDB : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
    abstract fun imageDao(): ImageDao
    abstract fun remoteKeys(): ImageRemoteKeyDao

    companion object {

        private var appDB: AppDB? = null

        fun getInstance(context: Context): AppDB {
            if (appDB == null) {
                synchronized(AppDB::class.java) {
                    appDB = Room.databaseBuilder(context, AppDB::class.java, "AppDB.db").allowMainThreadQueries().build()
                }
            }
            return appDB!!
        }
    }
}
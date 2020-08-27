package com.service.codingtest.manager

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.service.codingtest.db.FavoriteDao
import com.service.codingtest.db.ItemsDao
import com.service.codingtest.db.ImageRemoteKeyDao
import com.service.codingtest.model.response.FavoriteEntity
import com.service.codingtest.model.response.ImageRemoteKey
import com.service.codingtest.model.response.ItemsEntity

@Database(entities = [ItemsEntity::class, ImageRemoteKey::class, FavoriteEntity::class], version = 1)
abstract class AppDB : RoomDatabase() {
    abstract fun imageDao(): ItemsDao
    abstract fun remoteKeys(): ImageRemoteKeyDao
    abstract fun favoriteDao(): FavoriteDao

    companion object {

        private var appDB: AppDB? = null

        fun getInstance(context: Context): AppDB {
            if (appDB == null) {
                synchronized(AppDB::class.java) {
                    appDB = Room.databaseBuilder(context, AppDB::class.java, "AppDB.db")
//                        .addCallback(CALLBACK)
                        .allowMainThreadQueries().build()
                }
            }
            return appDB!!
        }

        private val CALLBACK = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)

//                db.execSQL("CREATE TRIGGER ...")
            }
        }
    }
}
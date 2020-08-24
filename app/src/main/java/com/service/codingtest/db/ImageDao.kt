package com.service.codingtest.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.service.codingtest.model.response.Items

@Dao
interface ImageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(items: Items)

    @Delete
    fun delete(items: Items)

    @Update
    fun update(items: Items): Int

//    @Query("SELECT * FROM Favorite ORDER BY saveTime ASC")
//    fun loadAllSortSaveTime(): LiveData<List<FavoriteEntity>>
//
//    @Query("SELECT * FROM Favorite ORDER BY rate DESC")
//    fun loadAllSortRate(): LiveData<List<FavoriteEntity>>

    @Query("SELECT * FROM Image")
    fun loadAll(): LiveData<List<Items>>

    @Query("SELECT EXISTS(SELECT * FROM Image WHERE id=:id)")
    fun exist(id: Int): Boolean

    @Query("DELETE FROM Image WHERE searchWord = :searchWord")
    suspend fun deleteBySubreddit(searchWord: String)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(posts: List<Items>)
}

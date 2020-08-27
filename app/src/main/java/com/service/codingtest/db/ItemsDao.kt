package com.service.codingtest.db

import androidx.paging.PagingSource
import androidx.room.*
import com.service.codingtest.model.response.ItemsEntity

@Dao
interface ItemsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(items: ItemsEntity)

    @Delete
    fun delete(items: ItemsEntity)

    @Update
    fun update(items: ItemsEntity): Int

//    @Query("SELECT * FROM Favorite ORDER BY saveTime ASC")
//    fun loadAllSortSaveTime(): LiveData<List<FavoriteEntity>>
//
//    @Query("SELECT * FROM Favorite ORDER BY rate DESC")
//    fun loadAllSortRate(): LiveData<List<FavoriteEntity>>

    @Query("SELECT * FROM Items WHERE searchWord = :searchWord")
    fun loadAll(searchWord: String): PagingSource<Int, ItemsEntity>

    @Query("SELECT * FROM Items")
    fun loadAll2(): List<ItemsEntity>

    @Query("SELECT EXISTS(SELECT * FROM Items WHERE id=:id)")
    fun exist(id: Int): Boolean

    @Query("DELETE FROM Items WHERE searchWord = :searchWord")
    suspend fun deleteBySubreddit(searchWord: String)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(posts: List<ItemsEntity>)
}

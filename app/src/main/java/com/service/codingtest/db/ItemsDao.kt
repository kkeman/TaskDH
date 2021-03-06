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
    suspend fun update(items: ItemsEntity)

    @Query("SELECT * FROM Items WHERE searchWord = :searchWord")
    fun loadAll(searchWord: String): PagingSource<Int, ItemsEntity>

    @Query("SELECT EXISTS(SELECT * FROM Items WHERE id=:id)")
    fun exist(id: Int): Boolean

    @Query("DELETE FROM Items WHERE searchWord = :searchWord")
    suspend fun deleteBySubreddit(searchWord: String)

    @Query("UPDATE Items SET isFavorite = :isFavorite WHERE id = :id")
    fun updateisFavorite(id: Int, isFavorite: Boolean)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(posts: List<ItemsEntity>)
}

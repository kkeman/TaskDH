package com.service.codingtest.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.LoadType.*
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.service.codingtest.db.ImageRemoteKeyDao
import com.service.codingtest.db.ItemsDao
import com.service.codingtest.manager.AppDB
import com.service.codingtest.model.response.ImageRemoteKey
import com.service.codingtest.model.response.ItemsEntity
import com.service.codingtest.network.ImageAPI
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class PageKeyedRemoteMediator(
    private val db: AppDB,
    private val imageAPI: ImageAPI,
    private val query: String
) : RemoteMediator<Int, ItemsEntity>() {
    private val imageDao: ItemsDao = db.imageDao()
    private val remoteKeyDao: ImageRemoteKeyDao = db.remoteKeys()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ItemsEntity>
    ): MediatorResult {
        try {
            var page = when (loadType) {
                REFRESH -> 0
                PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                APPEND -> {
                    val remoteKey = db.withTransaction {
                        remoteKeyDao.remoteKeyBySearchWord(query)
                    }

                    if (remoteKey.pageCount == null || remoteKey.pageCount == 0) {
                        return MediatorResult.Success(endOfPaginationReached = true)
                    }

                    remoteKey.pageCount
                }
            }

            val data = imageAPI.getAPI(
                query = query,
                page = page++,
            )

            var items = data.items

            items = items.map {
                it.searchWord = query
                it
            }

            db.withTransaction {
                if (loadType == REFRESH) {
                    imageDao.deleteBySubreddit(query)
                    remoteKeyDao.deleteBySearchWord(query)
                }

                remoteKeyDao.insert(ImageRemoteKey(query, page))
                imageDao.insertAll(items)
            }

            return MediatorResult.Success(endOfPaginationReached = items.isEmpty())
        } catch (e: IOException) {
            return MediatorResult.Error(e)
        } catch (e: HttpException) {
            return MediatorResult.Error(e)
        }
    }
}

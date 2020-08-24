package com.service.codingtest.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.LoadType.*
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.service.codingtest.manager.AppDB
import com.service.codingtest.db.ImageDao
import com.service.codingtest.db.ImageRemoteKeyDao
import com.service.codingtest.model.response.ImageRemoteKey
import com.service.codingtest.model.response.Items
import com.service.codingtest.network.ImageAPI
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class PageKeyedRemoteMediator(
    private val db: AppDB,
    private val imageAPI: ImageAPI,
    private val query: String
) : RemoteMediator<Int, Items>() {
    private val imageDao: ImageDao = db.imageDao()
    private val remoteKeyDao: ImageRemoteKeyDao = db.remoteKeys()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Items>
    ): MediatorResult {
        try {
            // Get the closest item from PagingState that we want to load data around.
            val page = when (loadType) {
                REFRESH -> null
                PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                APPEND -> {
                    // Query DB for SubredditRemoteKey for the subreddit.
                    // SubredditRemoteKey is a wrapper object we use to keep track of page keys we
                    // receive from the Reddit API to fetch the next or previous page.
                    val remoteKey = db.withTransaction {
                        remoteKeyDao.remoteKeyBySearchWord(query)
                    }

                    // We must explicitly check if the page key is null when appending, since the
                    // Reddit API informs the end of the list by returning null for page key, but
                    // passing a null key to Reddit API will fetch the initial page.
                    if (remoteKey.pageCount == null || remoteKey.pageCount == 0) {
                        return MediatorResult.Success(endOfPaginationReached = true)
                    }

                    remoteKey.pageCount
                }
            }

            val data = imageAPI.getAPI(
                query = query,
                page = page,
            )

            val items = data.items

            db.withTransaction {
                if (loadType == REFRESH) {
//                    imageDao.deleteBySubreddit(query)
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

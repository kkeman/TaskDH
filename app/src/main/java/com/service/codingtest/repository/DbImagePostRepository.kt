package com.service.codingtest.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.service.codingtest.manager.AppDB
import com.service.codingtest.manager.ImagePagingSource
import com.service.codingtest.network.ImageAPI

class DbImagePostRepository(val db: AppDB, private val imageAPI: ImageAPI) : ImageRepository {
    override fun postsOfSubDocument(
        query: String,
        pageSize: Int
    ) = Pager(
        config = PagingConfig(pageSize = pageSize),
        remoteMediator = PageKeyedRemoteMediator(db, imageAPI, query)) {


//        ImagePagingSource(
//            httpClient = imageAPI,
//            query = query,
//        )

        db.imageDao().loadAll2()
        db.imageDao().loadAll(query)
    }.flow
}

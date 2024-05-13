package rs.djokafioka.mvvmunsplash.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import rs.djokafioka.mvvmunsplash.api.UnsplashApi
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Djordje on 22.8.2022..
 */
@Singleton
class UnsplashRepository @Inject constructor(private val unsplashApi: UnsplashApi) {
    fun getSearchResults(query: String) =
        Pager(
            //20 ce biti prosledjeno metodi load u UnsplashPagingSource.kt
            config = PagingConfig(
                pageSize = 20,
                maxSize = 100,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { UnsplashPagingSource(unsplashApi, query) }
        ).liveData //moze i flow
}
package rs.djokafioka.mvvmunsplash.data

import androidx.paging.PagingSource
import retrofit2.HttpException
import rs.djokafioka.mvvmunsplash.api.UnsplashApi
import java.io.IOException

/**
 * Created by Djordje on 22.8.2022..
 */

private const val UNSPLASH_STARTING_PAGE_INDEX = 1

class UnsplashPagingSource(
    private val unsplashApi: UnsplashApi,
    private val query: String //ovo se prosledjuje dinamicki, zato ne koristimo Dagger da radimo inject, vec cemo sami da je instanciramo
) : PagingSource<Int, UnsplashPhoto>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UnsplashPhoto> {
        val position = params.key ?: UNSPLASH_STARTING_PAGE_INDEX

        return try {
            val response = unsplashApi.searchPhotos(query, position, params.loadSize)
            val photoList = response.results

            LoadResult.Page(
                data = photoList,
                prevKey = if (position == UNSPLASH_STARTING_PAGE_INDEX) null else position - 1,
                nextKey = if (photoList.isEmpty()) null else position + 1
            )
        } catch (exception: IOException) {
            //If there is no internet
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            //If something went wrong when calling the API
            LoadResult.Error(exception)
        }
    }
}
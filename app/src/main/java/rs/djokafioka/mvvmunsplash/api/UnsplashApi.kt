package rs.djokafioka.mvvmunsplash.api

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import rs.djokafioka.mvvmunsplash.BuildConfig

/**
 * Created by Djordje on 18.8.2022..
 */
interface UnsplashApi {
    companion object {
        const val BASE_URL = "https://api.unsplash.com/"
        const val CLIENT_ID = BuildConfig.UNSPLASH_ACCESS_KEY
    }

    @Headers("Accept-Version: v1", "Authorization: Client-ID $CLIENT_ID")
    @GET("search/photos")
    suspend fun searchPhotos(
        @Query ("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): UnsplashResponse
}
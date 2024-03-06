package rs.djokafioka.mvvmunsplash.api

import rs.djokafioka.mvvmunsplash.data.UnsplashPhoto

/**
 * Created by Djordje on 18.8.2022..
 */
data class UnsplashResponse(
    val results: List<UnsplashPhoto>
)
package rs.djokafioka.mvvmunsplash.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Djordje on 18.8.2022..
 */
@Parcelize
data class UnsplashPhoto(
    val id: String,
    val description: String?,
    val urls: UnsplashPhotoUrl,
    val user: UnsplashUser
) : Parcelable {

    @Parcelize
    data class UnsplashPhotoUrl(
        val raw: String,
        val full: String,
        val regular: String,
        val small: String,
        val thumb: String
    ) : Parcelable

    @Parcelize
    data class UnsplashUser(
        val name: String,
        val username: String
    ) : Parcelable {
        val attributionUrl get() = "https://unsplash.com/$username?utm_source=DjokaFiokaImageSearchApp&utm_medium=referral"
    }
}
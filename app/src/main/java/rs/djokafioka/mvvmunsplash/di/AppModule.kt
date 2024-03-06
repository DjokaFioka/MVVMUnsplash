package rs.djokafioka.mvvmunsplash.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import rs.djokafioka.mvvmunsplash.api.UnsplashApi
import javax.inject.Singleton

/**
 * Created by Djordje on 22.8.2022..
 */
@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Provides //Tells Dagger how to create this object
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(UnsplashApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideUnsplashApi(retrofit: Retrofit): UnsplashApi =
        retrofit.create(UnsplashApi::class.java)

}
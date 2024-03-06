package rs.djokafioka.mvvmunsplash.ui.gallery

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import rs.djokafioka.mvvmunsplash.data.UnsplashRepository
import java.security.PrivateKey

/**
 * Created by Djordje on 22.8.2022..
 */
class GalleryViewModel @ViewModelInject constructor (
    private val repository: UnsplashRepository,
    @Assisted state: SavedStateHandle //@Assisted is Dagger
    ) : ViewModel() {

    private val currentQuery = state.getLiveData(CURRENT_QUERY, DEFAULT_QUERY)
    //private val currentQuery = MutableLiveData(DEFAULT_QUERY) //Ovako je bilo dok nismo ubacili SavedStateHandle da pokrijemo process death

    val photos = currentQuery.switchMap { newQuery ->
        repository.getSearchResults(newQuery).cachedIn(viewModelScope) //cached in nam treba da ne bi pukla applikacija npr. kod rotacije, jer ne moze da vrati podatke sa iste strane
    }

    fun searchPhotos(query: String) {
        currentQuery.value = query
    }

    companion object {
        private const val DEFAULT_QUERY = "cars"
        private const val CURRENT_QUERY = "current_query"
    }
}
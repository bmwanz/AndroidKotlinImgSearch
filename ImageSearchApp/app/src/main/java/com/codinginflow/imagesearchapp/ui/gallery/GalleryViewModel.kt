package com.codinginflow.imagesearchapp.ui.gallery

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.codinginflow.imagesearchapp.data.UnsplashRepository

class GalleryViewModel @ViewModelInject constructor(
    private val repository: UnsplashRepository,
    @Assisted state: SavedStateHandle
) : ViewModel() {
    // view models need special treatment -> ViewModelInject

    private val currentQuery = state.getLiveData(CURRENT_QUERY, DEFAULT_QUERY)

    // will execute when currentQuery livedata value changes
    val photos = currentQuery.switchMap { queryString ->
        repository.getSearchResults(queryString)
            .cachedIn(viewModelScope) // prevent crash when rotating
    }

    fun searchPhotos(query: String) {
        currentQuery.value = query
    }

    companion object {
        private const val DEFAULT_QUERY = "cats"
        private const val CURRENT_QUERY = "current_query"
    }
}

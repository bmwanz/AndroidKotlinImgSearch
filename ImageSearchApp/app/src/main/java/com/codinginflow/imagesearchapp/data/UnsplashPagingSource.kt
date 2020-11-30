package com.codinginflow.imagesearchapp.data

import androidx.paging.PagingSource
import com.codinginflow.imagesearchapp.api.UnsplashApi
import retrofit2.HttpException
import java.io.IOException

private const val UNSPLASH_STARTING_PAGE_INDEX = 1

// not injected, don't know query at compile time (duh)

class UnsplashPagingSource(
    private val unsplashApi: UnsplashApi,
    private val query: String
) : PagingSource<Int, UnsplashPhoto>() {

    // PagingSource <Page Number, Data Filled in Pages>

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UnsplashPhoto> {
        // what page are we on? if null (first time), load first page
        val position = params.key ?: UNSPLASH_STARTING_PAGE_INDEX

        return try {
            val response = unsplashApi.searchPhotos(query, position, params.loadSize)
            val photos = response.results

            LoadResult.Page(
                data = photos,
                prevKey = if (position == UNSPLASH_STARTING_PAGE_INDEX) null else position - 1,
                nextKey = if (photos.isEmpty()) null else position + 1
            )
        } catch (exception: IOException) {
            // internet connection error
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            // something wrong server side, ex: forgot to pass Unsplash Key or no endpoint
            LoadResult.Error(exception)
        }
    }
}
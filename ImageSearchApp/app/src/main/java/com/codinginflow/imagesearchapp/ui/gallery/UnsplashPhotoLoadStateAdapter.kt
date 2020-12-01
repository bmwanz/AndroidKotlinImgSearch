package com.codinginflow.imagesearchapp.ui.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codinginflow.imagesearchapp.databinding.UnsplashPhotoLoadStateFooterBinding

// pass in retry function that has no arguments and returns nothing (unit)
class UnsplashPhotoLoadStateAdapter(private val retry: () -> Unit) : LoadStateAdapter<UnsplashPhotoLoadStateAdapter.LoadStateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        val binding = UnsplashPhotoLoadStateFooterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false)

        return LoadStateViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        // loadState, are we loading? use to show progress bar
        holder.bind(loadState)

        // do NOT set onclick listeners here
        // listeners will be set over and over
        // viewholders are only created a few items at a time
    }

    inner class LoadStateViewHolder(private val binding: UnsplashPhotoLoadStateFooterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.buttonRetry.setOnClickListener {
                // inner class to access retry function
                retry.invoke()
            }
        }

        fun bind(loadState: LoadState) {

            binding.apply {
                // visible if loadstate is an instance of LoadState.Loading,
                // meaning we're loading new items
                progressBar.isVisible = loadState is LoadState.Loading

                buttonRetry.isVisible = loadState !is LoadState.Loading
                textViewError.isVisible = loadState !is LoadState.Loading
            }
        }
    }
}
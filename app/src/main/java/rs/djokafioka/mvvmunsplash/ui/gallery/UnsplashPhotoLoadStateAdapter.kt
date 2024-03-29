package rs.djokafioka.mvvmunsplash.ui.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import rs.djokafioka.mvvmunsplash.databinding.UnsplashPhotoLoadStateFooterBinding

/**
 * Created by Djordje on 25.8.2022..
 */
class UnsplashPhotoLoadStateAdapter(private val retry: () ->  Unit) : LoadStateAdapter<UnsplashPhotoLoadStateAdapter.LoadStateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        val binding = UnsplashPhotoLoadStateFooterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LoadStateViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }


    inner class LoadStateViewHolder(private val binding: UnsplashPhotoLoadStateFooterBinding) : RecyclerView.ViewHolder(binding.root) {
        //Tight coupling between this inner class and the adapter,
        // but ok, because they belong together
        //Another option is to forward the retry function to the constructor
        // of the inner class which then wouldn't be declared as inner
        init {
            binding.buttonRetry.setOnClickListener{
                retry.invoke()
            }
        }

        fun bind(loadState: LoadState) {
            binding.apply {
                progressBar.isVisible = loadState is LoadState.Loading
                buttonRetry.isVisible = loadState !is LoadState.Loading
                textViewError.isVisible = loadState !is LoadState.Loading
            }
        }
    }
}
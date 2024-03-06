package rs.djokafioka.mvvmunsplash.ui.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import rs.djokafioka.mvvmunsplash.R
import rs.djokafioka.mvvmunsplash.data.UnsplashPhoto
import rs.djokafioka.mvvmunsplash.databinding.ItemUnsplashPhotoBinding

/**
 * Created by Djordje on 22.8.2022..
 */
class UnsplashPhotoAdapter(private val listener: OnItemClickListener) : PagingDataAdapter<UnsplashPhoto, UnsplashPhotoAdapter.PhotoViewHolder>(
    PHOTO_COMPARATOR
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val binding =
            ItemUnsplashPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return PhotoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val currentPhoto = getItem(position)

        if (currentPhoto != null) {
            holder.bind(currentPhoto)
        }
    }

    inner class PhotoViewHolder(private val binding: ItemUnsplashPhotoBinding) :
        //Tight coupling
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    if (item != null)
                        listener.onItemClick(item)
                }
            }
        }

        fun bind(photo: UnsplashPhoto) {
            binding.apply {
                Glide.with(itemView)
                    .load(photo.urls.regular)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_error)
                    .into(imageView)

                textViewUserName.text = photo.user.username
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(photo: UnsplashPhoto)
    }

    companion object {
        private val PHOTO_COMPARATOR = object : DiffUtil.ItemCallback<UnsplashPhoto>() {
            override fun areItemsTheSame(oldItem: UnsplashPhoto, newItem: UnsplashPhoto): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: UnsplashPhoto,
                newItem: UnsplashPhoto
            ): Boolean {
                return oldItem == newItem //posto je UnsplashPhoto dataclass poredjenje se radi za sve property koje su u konstruktoru
            }
        }
    }


}
package mai.project.galleryapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import mai.project.galleryapp.databinding.ItemPhotoBinding
import mai.project.galleryapp.model.Photo

class PhotoAdapter : ListAdapter<Photo, ViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        return PhotoViewHolder.from(parent)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item = getItem(position)
        if (viewHolder is PhotoViewHolder) {
            viewHolder.bind(item)
        }
    }

    private class PhotoViewHolder(
        private val binding: ItemPhotoBinding
    ) : ViewHolder(binding.root) {

        fun bind(
            item: Photo
        ) = with(binding) {
            Glide.with(imgPhoto.context)
                .load(item.uri)
                .centerCrop()
                .into(imgPhoto)
        }

        companion object {
            fun from(parent: ViewGroup): PhotoViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ItemPhotoBinding.inflate(inflater, parent, false)
                return PhotoViewHolder(binding)
            }
        }
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<Photo>() {
            override fun areItemsTheSame(
                oldItem: Photo,
                newItem: Photo
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: Photo,
                newItem: Photo
            ): Boolean = oldItem == newItem
        }
    }
}
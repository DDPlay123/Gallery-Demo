package mai.project.galleryapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import mai.project.galleryapp.databinding.ItemAlbumBinding
import mai.project.galleryapp.model.Album

class AlbumAdapter : ListAdapter<Album, ViewHolder>(diffUtil) {

    var onItemClick: ((Album) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        return AlbumViewHolder.from(parent)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item = getItem(position)
        if (viewHolder is AlbumViewHolder) {
            viewHolder.bind(item, onItemClick)
        }
    }

    private class AlbumViewHolder(
        private val binding: ItemAlbumBinding
    ) : ViewHolder(binding.root) {

        fun bind(
            item: Album,
            onItemClick: ((Album) -> Unit)?
        ) = with(binding) {
            tvName.text = item.bucketName
            Glide.with(imgPhoto.context)
                .load(item.coverPhotoUri)
                .centerCrop()
                .into(imgPhoto)

            root.setOnClickListener { onItemClick?.invoke(item) }
        }

        companion object {
            fun from(parent: ViewGroup): AlbumViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ItemAlbumBinding.inflate(inflater, parent, false)
                return AlbumViewHolder(binding)
            }
        }
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<Album>() {
            override fun areItemsTheSame(
                oldItem: Album,
                newItem: Album
            ): Boolean = oldItem.bucketId == newItem.bucketId

            override fun areContentsTheSame(
                oldItem: Album,
                newItem: Album
            ): Boolean = oldItem == newItem
        }
    }
}
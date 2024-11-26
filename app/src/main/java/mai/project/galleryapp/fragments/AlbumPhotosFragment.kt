package mai.project.galleryapp.fragments

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import mai.project.galleryapp.adapters.PhotoAdapter
import mai.project.galleryapp.base.BaseFragment
import mai.project.galleryapp.databinding.FragmentAlbumPhotosBinding
import mai.project.galleryapp.utils.AlbumUtil
import java.util.Locale

class AlbumPhotosFragment : BaseFragment<FragmentAlbumPhotosBinding>(
    FragmentAlbumPhotosBinding::inflate
) {
    private val args: AlbumPhotosFragmentArgs by navArgs()

    /**
     * 照片 adapter
     */
    private val photoAdapter by lazy { PhotoAdapter() }

    override fun FragmentAlbumPhotosBinding.destroy() {
        photoAdapter.submitList(null)
    }

    override fun FragmentAlbumPhotosBinding.initialize(view: View, savedInstanceState: Bundle?) {
        rvPhotos.adapter = photoAdapter
        loadPhotos()
    }

    override fun FragmentAlbumPhotosBinding.setListener() {
        imgBack.setOnClickListener { navigateUp() }
    }

    /**
     *  載入圖片
     */
    private fun loadPhotos() = AlbumUtil.loadPhotosFromAlbum(requireContext(), args.bucketId).let { photos ->
        binding.tvTitle.text = String.format(Locale.getDefault(), "%s (%d)", args.bucketName, photos.size)
        photoAdapter.submitList(photos)
    }
}
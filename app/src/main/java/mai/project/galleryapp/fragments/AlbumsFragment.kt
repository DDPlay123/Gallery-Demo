package mai.project.galleryapp.fragments

import android.os.Build
import android.os.Bundle
import android.view.View
import mai.project.galleryapp.R
import mai.project.galleryapp.adapters.AlbumAdapter
import mai.project.galleryapp.base.BaseFragment
import mai.project.galleryapp.databinding.FragmentAlbumsBinding
import mai.project.galleryapp.utils.AlbumUtil
import mai.project.galleryapp.utils.Method.allowPermission
import mai.project.galleryapp.utils.PERMISSION_MEDIA_VISUAL_USER_SELECTED
import mai.project.galleryapp.utils.media_permission
import java.util.Locale

class AlbumsFragment : BaseFragment<FragmentAlbumsBinding>(
    FragmentAlbumsBinding::inflate
) {
    override val useActivityOnBackPressed: Boolean = true

    /**
     * 相簿 adapter
     */
    private val albumAdapter by lazy { AlbumAdapter() }

    override fun FragmentAlbumsBinding.destroy() {
        albumAdapter.submitList(null)
    }

    override fun FragmentAlbumsBinding.initialize(view: View, savedInstanceState: Bundle?) {
        rvAlbums.adapter = albumAdapter
        checkPermission()
    }

    override fun FragmentAlbumsBinding.setListener() {
        albumAdapter.onItemClick = { album ->
            navigate(
                AlbumsFragmentDirections.actionAlbumsFragmentToAlbumPhotosFragment(
                    bucketId = album.bucketId,
                    bucketName = album.bucketName
                )
            )
        }
    }

    /**
     *  檢查權限
     */
    private fun checkPermission() {
        val allAllow = media_permission.all { requireContext().allowPermission(it) }
        val canLoad = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            allAllow || requireContext().allowPermission(PERMISSION_MEDIA_VISUAL_USER_SELECTED)
        } else {
            allAllow
        }
        if (canLoad) loadAlbums()
    }

    /**
     *  載入相簿
     */
    private fun loadAlbums() = AlbumUtil.loadAlbums(requireContext()).let { albums ->
        binding.tvTitle.text = String.format(Locale.getDefault(), "%s (%d)", getString(R.string.albums), albums.size)
        albumAdapter.submitList(albums)
    }
}
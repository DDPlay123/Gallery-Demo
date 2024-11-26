package mai.project.galleryapp.fragments

import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import mai.project.galleryapp.MainActivity
import mai.project.galleryapp.R
import mai.project.galleryapp.adapters.PhotoAdapter
import mai.project.galleryapp.base.BaseFragment
import mai.project.galleryapp.databinding.FragmentPhotosBinding
import mai.project.galleryapp.utils.AlbumUtil
import mai.project.galleryapp.utils.Method
import mai.project.galleryapp.utils.Method.openSetting
import mai.project.galleryapp.utils.PERMISSION_MEDIA_IMAGES
import mai.project.galleryapp.utils.PERMISSION_MEDIA_VISUAL_USER_SELECTED
import mai.project.galleryapp.utils.media_permission
import java.util.Locale

class PhotosFragment : BaseFragment<FragmentPhotosBinding>(
    FragmentPhotosBinding::inflate
) {
    override val useActivityOnBackPressed: Boolean = true

    /**
     * 照片 adapter
     */
    private val photoAdapter by lazy { PhotoAdapter() }

    /**
     * 請求檔案權限
     */
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            Method.logD("權限請求結果：$permissions")
            val deniedPermissions = permissions.filterValues { !it }.keys
            when {
                // 如果使用者沒有選擇『全部允許』，PERMISSION_MEDIA_IMAGES 會判定為 denied
                permissions.containsKey(PERMISSION_MEDIA_VISUAL_USER_SELECTED) &&
                        deniedPermissions.contains(PERMISSION_MEDIA_IMAGES) -> {
                    // 加載部分授權的照片
                    loadPhotos()
                }

                // 任一必要權限被拒絕
                deniedPermissions.isNotEmpty() -> {
                    Method.logE("權限被拒絕：$deniedPermissions")
                    (activity as? MainActivity)?.showSnackBar(
                        message = getString(R.string.permission_denied),
                        actionText = getString(R.string.open),
                        doSomething = { requireActivity().openSetting() }
                    )
                }

                // 全部權限都已授予
                else -> loadPhotos()
            }
        }

    override fun FragmentPhotosBinding.destroy() {
        photoAdapter.submitList(null)
    }

    override fun FragmentPhotosBinding.initialize(view: View, savedInstanceState: Bundle?) {
        rvPhotos.adapter = photoAdapter
        requestPermissionLauncher.launch(media_permission)
    }

    override fun FragmentPhotosBinding.setListener() {}

    /**
     *  載入圖片
     */
    private fun loadPhotos() = AlbumUtil.loadPhotosFromAlbum(requireContext()).let { photos ->
        binding.tvTitle.text = String.format(Locale.getDefault(), "%s (%d)", getString(R.string.photos), photos.size)
        photoAdapter.submitList(photos)
    }
}
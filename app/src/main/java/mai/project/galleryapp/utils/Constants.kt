package mai.project.galleryapp.utils

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi

/**
 * 讀取外部檔案權限
 *
 * - 一般來說只有在 SDK 33 不含以下才會用到。
 */
const val PERMISSION_READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE

/**
 * 讀取相片權限
 *
 * - SDK 33 含以上才使用
 */
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
const val PERMISSION_MEDIA_IMAGES = Manifest.permission.READ_MEDIA_IMAGES

/**
 * 讀取使用者選擇的媒體權限
 *
 * - SDK 34 含以上才使用
 */
@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
const val PERMISSION_MEDIA_VISUAL_USER_SELECTED = Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED

/**
 * 相片 / 影片相關媒體權限
 *
 * - SDK >= 34 請求讀取相片、影片、使用者選擇的媒體權限
 * - SDK >= 33 請求讀取相片、影片權限
 * - SDK < 33 請求讀取外部檔案權限
 *
 * @return 讀取相片、影片、使用者選擇的媒體權限、或讀取相片、影片權限、或讀取外部檔案權限
 */
val media_permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
    arrayOf(PERMISSION_MEDIA_IMAGES, PERMISSION_MEDIA_VISUAL_USER_SELECTED)
} else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    arrayOf(PERMISSION_MEDIA_IMAGES)
} else {
    arrayOf(PERMISSION_READ_EXTERNAL_STORAGE)
}
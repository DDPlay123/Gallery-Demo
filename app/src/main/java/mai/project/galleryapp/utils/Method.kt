package mai.project.galleryapp.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.Settings
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import mai.project.galleryapp.BuildConfig

/**
 * 常用方法
 */
object Method {

    /**
     * Debug Log
     */
    fun logD(
        message: String,
        tag: String? = null
    ) {
        if (BuildConfig.DEBUG) {
            Log.d(tag ?: getClassName(), message)
        }
    }

    /**
     * Error Log
     */
    fun logE(
        message: String,
        tr: Throwable? = null,
        tag: String? = null
    ) {
        if (BuildConfig.DEBUG) {
            Log.e(tag ?: getClassName(), message, tr)
        }
    }

    /**
     * 開啟設定
     */
    fun Activity.openSetting() {
        try {
            with(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)) {
                data = "package:${packageName}".toUri()
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(this)
            }
        } catch (e: Exception) {
            logE(message = "openSetting", tr = e)
        }
    }

    /**
     * 檢查權限
     */
    fun Context.allowPermission(
        permission: String
    ): Boolean {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * 取得類別名稱
     */
    private fun getClassName(): String {
        val className = Throwable().stackTrace[3].className
        return className.substringAfterLast('.')
    }
}
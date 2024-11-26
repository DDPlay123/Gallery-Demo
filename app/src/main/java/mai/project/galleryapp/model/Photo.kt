package mai.project.galleryapp.model

import android.net.Uri

data class Photo(
    val id: Long,
    val name: String,
    val date: Long,
    val data: String,
    val uri: Uri
)

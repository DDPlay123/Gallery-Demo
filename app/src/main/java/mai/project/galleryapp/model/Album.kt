package mai.project.galleryapp.model

import android.net.Uri

data class Album(
    val bucketId: String,
    val bucketName: String,
    val coverPhotoUri: Uri
)
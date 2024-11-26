package mai.project.galleryapp.utils

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import mai.project.galleryapp.model.Album
import mai.project.galleryapp.model.Photo

/**
 * 相簿工具
 */
object AlbumUtil {

    /**
     * 載入相簿
     */
    fun loadAlbums(
        context: Context
    ): List<Album> {
        val projection = arrayOf(
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media._ID
        )
        val uri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val selection = null
        val sortOrder = "${MediaStore.Images.Media.BUCKET_DISPLAY_NAME} ASC"
        val cursor: Cursor? = context.contentResolver.query(
            uri, projection, selection, null, sortOrder
        )

        val albums = mutableListOf<Album>()
        val albumSet = mutableSetOf<String>() // 用於避免重複相簿

        cursor?.use {
            val bucketIdColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID)
            val bucketNameColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
            val idColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media._ID)

            while (it.moveToNext()) {
                val bucketId = it.getString(bucketIdColumn)
                if (albumSet.contains(bucketId)) continue // 如果已經處理過此相簿，則跳過
                albumSet.add(bucketId)

                val bucketName = it.getString(bucketNameColumn)
                val coverPhotoUri = Uri.withAppendedPath(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    it.getLong(idColumn).toString()
                )
                albums.add(Album(bucketId, bucketName, coverPhotoUri))
            }
        }
        return albums
    }

    /**
     * 透過相簿Id 載入相片集
     *
     * @param bucketId 相簿Id，如果為 null 則載入全部相片
     */
    fun loadPhotosFromAlbum(
        context: Context,
        bucketId: String? = null
    ): List<Photo> {
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATE_TAKEN,
            MediaStore.Images.Media.DATA
        )
        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val selection = "${MediaStore.Images.Media.BUCKET_ID} = ?"
        val selectionArgs = arrayOf(bucketId)
        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"
        val cursor = if (bucketId != null) {
            context.contentResolver.query(uri, projection, selection, selectionArgs, sortOrder)
        } else {
            context.contentResolver.query(uri, projection, null, null, sortOrder)
        }

        val photos = mutableListOf<Photo>()
        cursor?.use {
            val idColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val nameColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
            val dateColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN)
            val dataColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            while (it.moveToNext()) {
                val id = it.getLong(idColumn)
                val name = it.getString(nameColumn)
                val date = it.getLong(dateColumn)
                val data = it.getString(dataColumn)
                val photoUri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id.toString())
                photos.add(Photo(id, name, date, data, photoUri))
            }
        }
        return photos
    }
}
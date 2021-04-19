package com.bakulabs.twittervideodownloader.api

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.buffer
import okio.sink
import timber.log.Timber
import java.io.File

sealed class DownloadResult {
    object Success : DownloadResult()
    data class Error(val message: String) : DownloadResult()
}

class VideoRepository(private val context: Context) {
    private val ok = OkHttpClient()
    private val collection =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) MediaStore.Video.Media.getContentUri(
            MediaStore.VOLUME_EXTERNAL
        ) else MediaStore.Video.Media.EXTERNAL_CONTENT_URI

    suspend fun download(url: String, name: String): Flow<DownloadResult> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) downloadQ(
            url,
            name
        )
        else downloadLegacy(url, name)
    }

    private suspend fun downloadQ(url: String, name: String): Flow<DownloadResult> {
        return flow {
            try {
                val response = ok.newCall(Request.Builder().url(url).build()).execute()
                if (response.isSuccessful) {
                    val values = ContentValues().apply {
                        put(MediaStore.Video.Media.DISPLAY_NAME, name)
                        put(MediaStore.Video.Media.RELATIVE_PATH, "Movies/Twitter Videos")
                        put(MediaStore.Video.Media.MIME_TYPE, "video/mp4")
                        put(MediaStore.Video.Media.IS_PENDING, 2)
                    }

                    val resolver = context.contentResolver
                    val uri = resolver.insert(collection, values)

                    uri?.let {
                        resolver.openOutputStream(uri)?.use { outputStream ->
                            val sink = outputStream.sink().buffer()

                            response.body?.source()?.let {
                                sink.writeAll(it)
                            }
                            sink.close()
                        }

                        values.clear()
                        values.put(MediaStore.Video.Media.IS_PENDING, 0)
                        resolver.update(uri, values, null, null)
                        emit(DownloadResult.Success)
                    } ?: throw RuntimeException("MediaStore failed for some reason")
                } else {
                    emit(DownloadResult.Error("Error"))
                    Timber.e("OkHttp failed for some reason")
                    throw RuntimeException("OkHttp failed for some reason")
                }
            } catch (exception: Exception) {
                Timber.e(exception.toString())
                emit(DownloadResult.Error("Error"))
            }
        }.flowOn(Dispatchers.Default)
    }

    private suspend fun downloadLegacy(
        url: String,
        name: String
    ): Flow<DownloadResult> {
        return flow {
            try {
                val dir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_MOVIES + "/Twitter Videos"
                )
                dir.mkdirs()
                val file = File(
                    dir,
                    "$name.mp4"
                )
                file.createNewFile()
                val response = ok.newCall(Request.Builder().url(url).build()).execute()

                if (response.isSuccessful) {
                    val sink = file.sink().buffer()

                    response.body?.source()?.let { sink.writeAll(it) }
                    sink.close()
                    emit(DownloadResult.Success)
                } else {
                    throw RuntimeException("OkHttp failed for some reason")
                }
            } catch (exception: Exception) {
                Timber.e(exception.toString())
                emit(DownloadResult.Error("Error"))

            }
        }.flowOn(Dispatchers.Default)
    }
}
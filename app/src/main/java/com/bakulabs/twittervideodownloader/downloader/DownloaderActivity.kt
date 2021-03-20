package com.bakulabs.twittervideodownloader.downloader

import android.content.ClipDescription.MIMETYPE_TEXT_PLAIN
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import com.bakulabs.twittervideodownloader.ui.theme.DownloaderTheme

class DownloaderActivity : AppCompatActivity() {
    private val viewModel by viewModels<DownloaderViewModel>()

    private fun getClipboardText(): String {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        return if(clipboard.hasPrimaryClip() && clipboard.primaryClipDescription?.hasMimeType(MIMETYPE_TEXT_PLAIN) == true) {
            clipboard.primaryClip!!.getItemAt(0).text.toString()
        } else {
            ""
        }
    }

    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DownloaderTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    DownloaderScreen(
                        variants = viewModel.variants,
                        getVariants = {viewModel.getVariants(it)},
                        downloadVariant = {viewModel.downloadVariant(it)},
                        getClipboardText = {getClipboardText()}
                    )
                }
            }
        }
    }
}
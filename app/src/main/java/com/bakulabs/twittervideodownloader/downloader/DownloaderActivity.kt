package com.bakulabs.twittervideodownloader.downloader

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import com.bakulabs.twittervideodownloader.ui.theme.DownloaderTheme

class DownloaderActivity : AppCompatActivity() {
    private val downloaderViewModel by viewModels<DownloaderViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DownloaderTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    DownloaderActivityScreen(viewModel = downloaderViewModel)
                }
            }
        }
    }
}

@Composable fun DownloaderActivityScreen(viewModel: DownloaderViewModel) {
    DownloaderScreen(
        variants = viewModel.variants,
        onFetchVariants = {viewModel.fetchVariants(it)},
        onDownloadVariant = {viewModel.downloadVariant(it)}
    )
}
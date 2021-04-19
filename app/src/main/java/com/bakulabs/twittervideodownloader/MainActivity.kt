package com.bakulabs.twittervideodownloader

import android.content.ClipDescription.MIMETYPE_TEXT_PLAIN
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.lifecycle.ViewModelProvider
import com.bakulabs.twittervideodownloader.api.VideoRepository
import com.bakulabs.twittervideodownloader.ui.home.HomeScreen
import com.bakulabs.twittervideodownloader.ui.home.HomeViewModel
import com.bakulabs.twittervideodownloader.ui.home.HomeViewModelFactory
import com.bakulabs.twittervideodownloader.ui.theme.DownloaderTheme
import kotlinx.coroutines.InternalCoroutinesApi
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    private fun getClipboardText(): String {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        return if (clipboard.hasPrimaryClip() && clipboard.primaryClipDescription?.hasMimeType(
                MIMETYPE_TEXT_PLAIN
            ) == true
        ) {
            clipboard.primaryClip!!.getItemAt(0).text.toString()
        } else {
            ""
        }
    }

    @InternalCoroutinesApi
    @ExperimentalComposeUiApi
    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.plant(Timber.DebugTree())

        val videoRepository = VideoRepository(applicationContext)

        val viewModelFactory = HomeViewModelFactory(videoRepository)
        val viewModel = ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)

        setContent {
            DownloaderTheme {
                Surface(color = MaterialTheme.colors.background) {
                    HomeActivityScreen(
                        viewModel = viewModel,
                        getClipboardText = { getClipboardText() }
                    )
                }
            }
        }
    }
}

@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Composable
private fun HomeActivityScreen(viewModel: HomeViewModel, getClipboardText: () -> String) {
    HomeScreen(
        isLoading = viewModel.isLoading,
        showSnackBar = viewModel.showSnackBar,
        snackBarMessageId = viewModel.snackBarMessageId,
        onDismissSnackBar = viewModel::dismissSnackBar,
        showSheet = viewModel.showSheet,
        hideSheet = viewModel.hideSheet,
        onDismissSheet = viewModel::dismissSheet,
        variants = viewModel.variants,
        getVariants = viewModel::getVariants,
        downloadVariant = viewModel::downloadVariant,
        getClipboardText = getClipboardText
    )
}
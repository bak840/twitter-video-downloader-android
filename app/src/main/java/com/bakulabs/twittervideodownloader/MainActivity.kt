package com.bakulabs.twittervideodownloader

import android.content.ClipDescription.MIMETYPE_TEXT_PLAIN
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
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

    private fun openVariant(uri: Uri) {
        val target = Intent(Intent.ACTION_VIEW)
        target.setDataAndType(uri, "video/mp4")
        target.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        val intent = Intent.createChooser(target, getString(R.string.open_file_intent))
        startActivity(intent)
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

        var initUrl = "https://twitter.com/bak840/status/1362860958092316675"
        if (intent?.action == Intent.ACTION_SEND) {
            initUrl = intent.getStringExtra(Intent.EXTRA_TEXT).toString()
        }

        setContent {
            DownloaderTheme {
                Surface(color = MaterialTheme.colors.background) {
                    HomeActivityScreen(
                        initUrl = initUrl,
                        viewModel = viewModel,
                        getClipboardText = this::getClipboardText,
                        openVariant = this::openVariant
                    )
                }
            }
        }
    }
}

@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Composable
private fun HomeActivityScreen(
    initUrl: String,
    viewModel: HomeViewModel,
    getClipboardText: () -> String,
    openVariant: (uri: Uri) -> Unit
) {
    HomeScreen(
        initUrl = initUrl,
        isLoading = viewModel.isLoading,
        isSheetShowing = viewModel.isSheetShowing,
        isSheetHiding = viewModel.isSheetHiding,
        onDismissSheet = viewModel::dismissSheet,
        isVariantSnackBarShowing = viewModel.isVariantSnackBarShowing,
        variantUri = viewModel.variantUri,
        openVariant = openVariant,
        onDismissVariantSnackBar = viewModel::dismissVariantSnackBar,
        isErrorSnackBarShowing = viewModel.isErrorSnackBarShowing,
        errorResId = viewModel.errorResId,
        onDismissErrorSnackBar = viewModel::dismissErrorSnackBar,
        variants = viewModel.variants,
        getVariants = viewModel::getVariants,
        downloadVariant = viewModel::downloadVariant,
        getClipboardText = getClipboardText
    )
}
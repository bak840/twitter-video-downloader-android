package com.bakulabs.twittervideodownloader

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
import androidx.compose.ui.ExperimentalComposeUiApi
import com.bakulabs.twittervideodownloader.ui.home.HomeScreen
import com.bakulabs.twittervideodownloader.ui.home.HomeViewModel
import com.bakulabs.twittervideodownloader.ui.theme.DownloaderTheme
import kotlinx.coroutines.InternalCoroutinesApi
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<HomeViewModel>()

    private fun getClipboardText(): String {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        return if(clipboard.hasPrimaryClip() && clipboard.primaryClipDescription?.hasMimeType(MIMETYPE_TEXT_PLAIN) == true) {
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
        setContent {
            DownloaderTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    HomeScreen(
                        viewModel = viewModel,
                        getClipboardText = {getClipboardText()}
                    )
                }
            }
        }
    }
}
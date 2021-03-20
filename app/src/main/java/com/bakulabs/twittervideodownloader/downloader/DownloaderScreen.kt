package com.bakulabs.twittervideodownloader.downloader

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bakulabs.twittervideodownloader.R
import com.bakulabs.twittervideodownloader.domain.Variant
import com.bakulabs.twittervideodownloader.ui.theme.DownloaderTheme
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@Composable
fun DownloaderScreen(
    variants: List<Variant>,
    getVariants: (String) -> Unit,
    downloadVariant: (String) -> Unit,
    getClipboardText: () -> String
) {
    val state = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()

    ModalBottomSheetLayout(
        sheetState = state,
        sheetContent = {
            Column(Modifier.padding(8.dp)) {
                Text(text = stringResource(R.string.videos_sheet_title))
                Spacer(modifier = Modifier.height(16.dp))
                for (variant in variants) {
                    Row(
                        modifier = Modifier.padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = variant.definition)
                        Spacer(modifier = Modifier.weight(1f))
                        Text(text = variant.size)
                        Spacer(modifier = Modifier.width(8.dp))
                        IconButton(onClick = { downloadVariant(variant.url) }) {
                            Icon(imageVector = Icons.Default.FileDownload, contentDescription = null)
                        }
                    }
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(R.string.home_title)) }
                )
            }
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                val (url, setUrl) = remember { mutableStateOf("") }

                OutlinedTextField(
                    value = url,
                    onValueChange = { setUrl(it) },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text(text = stringResource(R.string.url_field_placeholder)) },
                    singleLine = true,
                    trailingIcon = {
                        IconButton(onClick = { setUrl("") }) {
                            Icon(
                                Icons.Default.Clear,
                                contentDescription = stringResource(R.string.url_field_clear),
                                tint = Color.Black
                            )
                        }
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Button(onClick = { setUrl(getClipboardText()) }) {
                        Text(text = stringResource(R.string.paste_button_text))
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Button(
                        onClick = {
                            getVariants(url)
                            scope.launch { state.show() }
                        }
                    ) {
                        Text(text = stringResource(R.string.button_download_text))
                    }
                }
            }
        }
    }
}

@ExperimentalMaterialApi
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    DownloaderTheme {
        DownloaderScreen(listOf(Variant("", "1920x1080", "36 kB")), {}, {}, { "" })
    }
}
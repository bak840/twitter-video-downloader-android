package com.bakulabs.twittervideodownloader.downloader

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.bakulabs.twittervideodownloader.R
import com.bakulabs.twittervideodownloader.domain.Variant
import com.bakulabs.twittervideodownloader.ui.theme.DownloaderTheme

@Composable
fun DownloaderScreen(
    variants: List<Variant>,
    onFetchVariants: (String) -> Unit,
    onDownloadVariant: (String) -> Unit
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.home_title)) }
            )
        }
    ) {
        Column {
            Row(
            ) {
                val (url, setUrl) = remember { mutableStateOf("") }

                OutlinedTextField(value = url, onValueChange = {
                    setUrl(it)
                })
                Button(onClick = { onFetchVariants(url) }) {
                    Icon(imageVector = Icons.Default.Refresh, contentDescription = null)
                }
            }

            if(variants.isEmpty()) {
                Text(text = stringResource(R.string.instructions))
            }
            else {
                Column {
                    Text(text = stringResource(R.string.variants_section_title))
                    for (variant in variants) {
                        VariantRow(variant = variant, onDownloadVariant = { onDownloadVariant(it) })
                    }
                }
            }
        }
    }
}

@Composable
fun VariantRow(variant: Variant, onDownloadVariant: (String) -> Unit) {
    Row {
        Text(text = variant.definition)
        Text(text = variant.size)
        Button(onClick = { onDownloadVariant(variant.url) }) {
            Icon(imageVector = Icons.Default.Download, contentDescription = null)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    DownloaderTheme {
        DownloaderScreen(listOf(Variant("", "1920x1080", "36 kB")), {}, {})
    }
}
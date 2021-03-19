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

@Composable
fun DownloaderScreen(
    variants: List<Variant>,
    onFetchVariants: (String) -> Unit,
    onDownloadVariant: (String) -> Unit,
    getClipboardText: () -> String
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.home_title)) }
            )
        }
    ) {
        Column {
            UrlRow(onFetchVariants, getClipboardText)

            if (variants.isEmpty()) {
                Text(text = stringResource(R.string.instructions))
            } else {
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
private fun UrlRow(
    onFetchVariants: (String) -> Unit,
    getClipboardText: () -> String,
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
            Button(onClick = { onFetchVariants(url) }) {
                Text(text = stringResource(R.string.button_download_text))
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun UrlRowPreview() {
    Surface(color = Color.White) {
        UrlRow({}, { "" })
    }
}

@Composable
fun VariantRow(variant: Variant, onDownloadVariant: (String) -> Unit) {
    Row {
        Text(text = variant.definition)
        Text(text = variant.size)
        Button(onClick = { onDownloadVariant(variant.url) }) {
            Icon(imageVector = Icons.Default.FileDownload, contentDescription = null)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    DownloaderTheme {
        DownloaderScreen(listOf(Variant("", "1920x1080", "36 kB")), {}, {}, { "" })
    }
}
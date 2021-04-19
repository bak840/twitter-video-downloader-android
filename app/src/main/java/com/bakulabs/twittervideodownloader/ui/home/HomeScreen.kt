package com.bakulabs.twittervideodownloader.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bakulabs.twittervideodownloader.R
import com.bakulabs.twittervideodownloader.domain.Variant
import com.bakulabs.twittervideodownloader.ui.theme.DownloaderTheme
import kotlinx.coroutines.InternalCoroutinesApi

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun HomeScreen(
    isLoading: Boolean,
    showSnackBar: Boolean,
    snackBarMessageId: Int,
    onDismissSnackBar: () -> Unit,
    showSheet: Boolean,
    hideSheet: Boolean,
    onDismissSheet: () -> Unit,
    variants: List<Variant>,
    getVariants: (id: String) -> Unit,
    downloadVariant: (variant: Variant) -> Unit,
    getClipboardText: () -> String,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    val scaffoldState = rememberScaffoldState()
    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)

    val onDismissSnackBarState by rememberUpdatedState(newValue = onDismissSnackBar)
    val onDismissSheetState by rememberUpdatedState(newValue = onDismissSheet)

    if (showSnackBar) {
        val snackBarMessage = stringResource(id = snackBarMessageId)
        LaunchedEffect(showSnackBar) {
            try {
                scaffoldState.snackbarHostState.showSnackbar(snackBarMessage)
            } finally {
                onDismissSnackBarState()
            }
        }
    }

    if (showSheet) {
        LaunchedEffect(variants, showSheet) {
            try {
                sheetState.show()

            } finally {
                onDismissSheetState()
            }
        }
    }

    if (hideSheet) {
        LaunchedEffect(hideSheet) {
            sheetState.hide()
        }
    }

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            Column(Modifier.padding(8.dp)) {
                Text(text = stringResource(R.string.videos_sheet_title))
                Spacer(modifier = Modifier.height(16.dp))
                variants.forEach { variant ->
                    Row(
                        modifier = Modifier.padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = variant.definition)
                        Spacer(modifier = Modifier.weight(1f))
                        Text(text = variant.size)
                        Spacer(modifier = Modifier.width(8.dp))
                        IconButton(onClick = { downloadVariant(variant) }) {
                            Icon(
                                imageVector = Icons.Default.FileDownload,
                                contentDescription = null
                            )
                        }
                    }
                }
            }
        }
    ) {
        Scaffold(
            scaffoldState = scaffoldState,
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(R.string.home_title)) }
                )
            }
        ) {
            LoadingScreen(isLoading = isLoading) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    val testUrl = "https://twitter.com/bak840/status/1362860958092316675"
                    val (url, setUrl) = remember { mutableStateOf(testUrl) }

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
                                keyboardController?.hide()
                                getVariants(url)
                            }
                        ) {
                            Text(text = stringResource(R.string.button_download_text))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LoadingScreen(
    isLoading: Boolean,
    content: @Composable () -> Unit
) = if (isLoading
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = stringResource(R.string.loading_text))
            CircularProgressIndicator()
        }
    }
} else {
    content()
}

@InternalCoroutinesApi
@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Preview(showBackground = true)
@Composable
fun DefaultPreview() = DownloaderTheme {
    HomeScreen(
        isLoading = false,
        showSnackBar = false,
        snackBarMessageId = 0,
        onDismissSnackBar = {},
        showSheet = false,
        hideSheet = false,
        onDismissSheet = {},
        variants = listOf(),
        getVariants = {},
        downloadVariant = {}) {
        ""
    }
}

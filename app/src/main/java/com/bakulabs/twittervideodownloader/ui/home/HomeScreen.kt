package com.bakulabs.twittervideodownloader.ui.home

import android.net.Uri
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
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
    url: String,
    updateUrl: (newValue: String) -> Unit,
    variants: List<Variant>,
    getVariants: () -> Unit,
    downloadVariant: (variant: Variant) -> Unit,
    isLoading: Boolean,
    isSheetShowing: Boolean,
    isSheetHiding: Boolean,
    onDismissSheet: () -> Unit,
    isVariantSnackBarShowing: Boolean,
    variantUri: Uri,
    openVariant: (uri: Uri) -> Unit,
    onDismissVariantSnackBar: () -> Unit,
    isErrorSnackBarShowing: Boolean,
    errorResId: Int,
    onDismissErrorSnackBar: () -> Unit,
    getClipboardText: () -> String,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    val scaffoldState = rememberScaffoldState()
    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)

    val onDismissSheetState by rememberUpdatedState(newValue = onDismissSheet)
    val onDismissVariantSnackBarState by rememberUpdatedState(newValue = onDismissVariantSnackBar)
    val onDismissErrorSnackBarState by rememberUpdatedState(newValue = onDismissErrorSnackBar)

    if (isSheetShowing) {
        LaunchedEffect(variants, isSheetShowing) {
            try {
                sheetState.show()

            } finally {
                onDismissSheetState()
            }
        }
    }

    if (isSheetHiding) {
        LaunchedEffect(isSheetHiding) {
            sheetState.hide()
        }
    }

    if (isVariantSnackBarShowing) {
        val snackBarMessage = stringResource(R.string.download_successful)
        val openActionText = stringResource(R.string.button_open_file_text)
        LaunchedEffect(isVariantSnackBarShowing) {
            try {
                when (scaffoldState.snackbarHostState.showSnackbar(
                    snackBarMessage,
                    openActionText
                )) {
                    SnackbarResult.Dismissed -> {}
                    SnackbarResult.ActionPerformed -> openVariant(variantUri)
                }
            } finally {
                onDismissVariantSnackBarState()
            }
        }
    }

    if (isErrorSnackBarShowing) {
        val snackBarMessage = stringResource(id = errorResId)
        LaunchedEffect(isErrorSnackBarShowing) {
            try {
                scaffoldState.snackbarHostState.showSnackbar(snackBarMessage)
            } finally {
                onDismissErrorSnackBarState()
            }
        }
    }

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            VariantsSheet(variants, downloadVariant)
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
                    modifier = Modifier.padding(
                        top = 32.dp,
                        start = 16.dp,
                        bottom = 16.dp,
                        end = 16.dp
                    )
                ) {

                    UrlView(url, updateUrl, getClipboardText, keyboardController, getVariants)
                }
            }
        }
    }
}

@ExperimentalComposeUiApi
@Composable
private fun UrlView(
    url: String,
    updateUrl: (newValue: String) -> Unit,
    getClipboardText: () -> String,
    keyboardController: SoftwareKeyboardController?,
    getVariants: () -> Unit
) {
    OutlinedTextField(
        value = url,
        onValueChange = { updateUrl(it) },
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(text = stringResource(R.string.placeholder_url)) },
        singleLine = true,
        trailingIcon = {
            IconButton(onClick = { updateUrl("") }) {
                Icon(
                    Icons.Default.Clear,
                    contentDescription = stringResource(R.string.button_clear_url_text),
                    tint = if (isSystemInDarkTheme()) Color.White else Color.Black
                )
            }
        }
    )
    Spacer(modifier = Modifier.height(16.dp))
    Row(
        modifier = Modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Button(onClick = { updateUrl(getClipboardText()) }) {
            Text(text = stringResource(R.string.button_paste_text))
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = {
                keyboardController?.hide()
                getVariants()
            }
        ) {
            Text(text = stringResource(R.string.button_download_text))
        }
    }
}

@Composable
private fun VariantsSheet(
    variants: List<Variant>,
    downloadVariant: (variant: Variant) -> Unit
) {
    Column(Modifier.padding(vertical = 8.dp, horizontal = 16.dp)) {
        Text(text = stringResource(R.string.title_variants_sheet))
        Spacer(modifier = Modifier.height(16.dp))
        variants.forEach { variant ->
            VariantRow(variant, downloadVariant)
        }
    }
}

@Composable
private fun VariantRow(
    variant: Variant,
    downloadVariant: (variant: Variant) -> Unit
) {
    Row(
        modifier = Modifier.padding(bottom = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = variant.definition)
        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = { downloadVariant(variant) }) {
            Icon(
                imageVector = Icons.Default.FileDownload,
                contentDescription = null
            )
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
        url = "",
        updateUrl = {},
        variants = listOf(),
        getVariants = {},
        downloadVariant = {},
        isLoading = false,
        isSheetShowing = false,
        isSheetHiding = false,
        onDismissSheet = {},
        isVariantSnackBarShowing = false,
        variantUri = Uri.Builder().build(),
        openVariant = {},
        onDismissVariantSnackBar = {},
        isErrorSnackBarShowing = false,
        errorResId = 0,
        onDismissErrorSnackBar = {},
        ) {
        ""
    }
}

package com.muzafferatmaca.androidhealthconnectcompose.presentation.component

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.muzafferatmaca.androidhealthconnectcompose.R
import com.muzafferatmaca.androidhealthconnectcompose.presentation.theme.AndroidHealthConnectComposeTheme

/**
 * Created by Muzaffer Atmaca on 4.06.2024 at 16:41
 */
@Composable
fun NotInstalledMessage() {
    val tag = stringResource(id = R.string.not_installed_tag)
    val url = Uri.parse(stringResource(id = R.string.market_url))
        .buildUpon()
        .appendQueryParameter("id", stringResource(id = R.string.health_connect_package))
        .appendQueryParameter("url", stringResource(id = R.string.onboarding_url))
        .build()

    val context = LocalContext.current

    val notInstalledText = stringResource(id = R.string.not_installed_description)
    val notInstalledLinkText = stringResource(id = R.string.not_installed_link_text)
    val unavailableText = buildAnnotatedString {
        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onBackground)) {
            append(notInstalledText)
            append("\n\n")
        }
        pushStringAnnotation(tag = tag, annotation = url.toString())
        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
            append(notInstalledLinkText)
        }
    }
    ClickableText(
        text = unavailableText,
        style = TextStyle(textAlign = TextAlign.Justify)
    ) { offset ->
        unavailableText.getStringAnnotations(tag = tag, start = offset, end = offset)
            .firstOrNull()?.let {
                context.startActivity(
                    Intent(Intent.ACTION_VIEW, Uri.parse(it.item))
                )
            }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
private fun NotInstalledMessagePrev() {
    AndroidHealthConnectComposeTheme {
        NotInstalledMessage()
    }
}
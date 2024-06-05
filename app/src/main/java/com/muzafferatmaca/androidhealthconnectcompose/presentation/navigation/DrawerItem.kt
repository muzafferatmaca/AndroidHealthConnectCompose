package com.muzafferatmaca.androidhealthconnectcompose.presentation.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.muzafferatmaca.androidhealthconnectcompose.presentation.theme.AndroidHealthConnectComposeTheme

/**
 * Created by Muzaffer Atmaca on 4.06.2024 at 19:12
 */
@Composable
fun DrawerItem(
    item: Screen,
    selected: Boolean,
    onItemClick: (Screen) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = { onItemClick(item) })
            .height(48.dp)
            .padding(start = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$item",
            style = MaterialTheme.typography.headlineSmall,
            color = if (selected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onBackground
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DrawerItemPreview() {
    AndroidHealthConnectComposeTheme {
        DrawerItem(
            item = Screen.ExerciseSessions,
            selected = true,
            onItemClick = {}
        )
    }
}
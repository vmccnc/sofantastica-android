package com.furniture.duet.ui.more

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.furniture.duet.R

@Composable
fun MoreScreen(
    onLogOut: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(dimensionResource(R.dimen.margin_16))) {
        TextButton(onClick = onLogOut) {
            Icon(Icons.Default.ExitToApp, contentDescription = null)
            Text("Sign Out")
        }
    }
}
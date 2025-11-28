package com.enesay.android_task.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.enesay.android_task.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultTopBar(
    onSearchClicked: () -> Unit,
    onQrClicked : () -> Unit
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        actions = {

            Row(horizontalArrangement = Arrangement.End){
                IconButton(onClick = onSearchClicked) {

                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = stringResource(R.string.search_desc)
                    )
                }
                IconButton(onClick = onQrClicked) {
                    Icon(
                        imageVector = Icons.Default.QrCode2,
                        contentDescription = stringResource(R.string.qr_scan_desc)
                    )
                }
            }
        }
    )
}
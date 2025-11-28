package com.enesay.android_task.ui.components


import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.enesay.android_task.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTopBar(
    text: String,
    onTextChange: (String) -> Unit,
    onCloseClicked: () -> Unit,
    onQrClicked: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    TopAppBar(
        title = {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                value = text,
                onValueChange = onTextChange,
                placeholder = {
                    Text(
                        text = stringResource(R.string.search_hint),
                        color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f)
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    cursorColor = MaterialTheme.colorScheme.secondary,
                    focusedTextColor = MaterialTheme.colorScheme.secondary,
                    unfocusedTextColor = MaterialTheme.colorScheme.secondary,
                    focusedBorderColor = MaterialTheme.colorScheme.secondary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                ),
                textStyle = MaterialTheme.typography.titleMedium,
                trailingIcon = {
                    IconButton(onClick = onQrClicked) {
                        Icon(
                            imageVector = Icons.Default.QrCode2,
                            contentDescription = stringResource(R.string.qr_scan_desc)
                        )
                    }
                }
            )
        },
        navigationIcon = {

        },
        actions = {
            IconButton(
                modifier = Modifier.padding(horizontal = 3.dp),
                onClick = {
                    onTextChange("")
                    onCloseClicked()
                },
                colors = IconButtonColors(
                    containerColor = Color.LightGray,
                    contentColor = Color.Black,
                    disabledContainerColor = Color.Black,
                    disabledContentColor = Color.Black
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(R.string.close_search_desc),
                    tint = MaterialTheme.colorScheme.secondary,
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White,
        )
    )
}
package com.enesay.android_task.utils

import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt

fun String.toColor(): Color {
    return try {
        Color(this.toColorInt())
    } catch (e: Exception) {
        Color.LightGray
    }
}

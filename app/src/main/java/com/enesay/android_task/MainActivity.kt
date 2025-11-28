package com.enesay.android_task

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.enesay.android_task.ui.screens.TaskList.TaskListScreen
import com.enesay.android_task.ui.theme.AndroidtaskTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidtaskTheme {
                   TaskListScreen()
            }
        }
    }
}
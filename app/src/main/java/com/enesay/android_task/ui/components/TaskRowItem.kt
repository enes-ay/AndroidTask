package com.enesay.android_task.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.enesay.android_task.domain.model.TaskModel
import com.enesay.android_task.utils.toColor

@Composable
fun TaskRowItem(task: TaskModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = task.colorCode.toColor()
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            task.title.let { Text(it, style = MaterialTheme.typography.titleMedium) }
            task.task.let { Text(it, style = MaterialTheme.typography.bodyMedium) }
            task.description.let { Text(it, style = MaterialTheme.typography.bodySmall) }
        }
    }
}

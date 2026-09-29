package com.volunteernews24.app.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.volunteernews24.app.ui.theme.VNRed

@Composable
fun PaginationControls(
    currentPage: Int,
    onPreviousPage: () -> Unit,
    onNextPage: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = onPreviousPage,
            enabled = currentPage > 1,
            colors = ButtonDefaults.buttonColors(containerColor = VNRed)
        ) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Previous")
            Spacer(modifier = Modifier.width(4.dp))
            Text("পূর্ববর্তী")
        }

        Text(
            text = "পৃষ্ঠা $currentPage",
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Button(
            onClick = onNextPage,
            colors = ButtonDefaults.buttonColors(containerColor = VNRed)
        ) {
            Text("পরবর্তী")
            Spacer(modifier = Modifier.width(4.dp))
            Icon(Icons.Default.ArrowForward, contentDescription = "Next")
        }
    }
}

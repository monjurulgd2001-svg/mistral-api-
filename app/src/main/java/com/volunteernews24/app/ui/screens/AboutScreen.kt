package com.volunteernews24.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.clickable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.volunteernews24.app.ui.theme.VNRed
import com.volunteernews24.app.data.repository.AppThemeColor
import com.volunteernews24.app.data.repository.AppThemeMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    currentTheme: AppThemeColor,
    currentMode: AppThemeMode,
    onThemeChange: (AppThemeColor) -> Unit,
    onModeChange: (AppThemeMode) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("আমাদের সম্পর্কে", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "VolunteerNews24",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = VNRed,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            Text(
                text = "সত্য ও বস্তনিষ্ঠ সংবাদ প্রকাশে অঙ্গীকারবদ্ধ",
                fontSize = 16.sp,
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "পরিচালনা পর্ষদ",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = VNRed,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    
                    InfoRow(
                        icon = Icons.Default.Person,
                        title = "চেয়ারম্যান",
                        value = "এম. রশিদ আলী"
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    InfoRow(
                        icon = Icons.Default.Person,
                        title = "নির্বাহী সম্পাদক",
                        value = "সিরাজুম মুনিরা"
                    )
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "যোগাযোগ",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = VNRed,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    
                    InfoRow(
                        icon = Icons.Default.LocationOn,
                        title = "বার্তা ও বাণিজ্যিক কার্যালয়",
                        value = "জেলা পরিষদ সুপার মার্কেট, ৩য় তলা, রুম-৩১৭, কুড়িগ্রাম।"
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    InfoRow(
                        icon = Icons.Default.Phone,
                        title = "মোবাইল",
                        value = "01723438687"
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    InfoRow(
                        icon = Icons.Default.Email,
                        title = "ই-মেইল",
                        value = "volunteernews642@gmail.com"
                    )
                }
            }
            
            // Theme Section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "ডার্ক মোড",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Switch(
                        checked = currentMode == AppThemeMode.DARK,
                        onCheckedChange = { isChecked ->
                            onModeChange(if (isChecked) AppThemeMode.DARK else AppThemeMode.LIGHT)
                        }
                    )
                }
            }
        }
    }
}


@Composable
fun InfoRow(
    icon: ImageVector,
    title: String,
    value: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = VNRed,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = title,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

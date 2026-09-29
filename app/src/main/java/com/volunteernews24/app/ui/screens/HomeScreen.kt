package com.volunteernews24.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.volunteernews24.app.R
import com.volunteernews24.app.data.model.Article
import com.volunteernews24.app.ui.components.*
import com.volunteernews24.app.ui.theme.VNRed
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    isLoading: Boolean,
    isOffline: Boolean, // Kept for compatibility, though we are online-only now
    articles: List<Article>,
    onRefresh: () -> Unit,
    onArticleClick: (Article) -> Unit,
    onBookmarkClick: (String) -> Unit,
    onAboutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isRefreshing by remember { mutableStateOf(false) }

    LaunchedEffect(isLoading) {
        if (!isLoading) {
            isRefreshing = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "VolunteerNews24",
                            fontWeight = FontWeight.Bold,
                            color = VNRed,
                            fontSize = 20.sp
                        )
                        Text(
                            text = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("bn", "BD")).format(Date()),
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onAboutClick) {
                        Icon(Icons.Default.Info, contentDescription = "About Us", tint = VNRed)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { paddingValues ->
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = {
                isRefreshing = true
                onRefresh()
            },
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(top = 8.dp, bottom = 100.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (isOffline) {
                    item {
                        OfflineBanner()
                    }
                }

                if (isLoading && articles.isEmpty()) {
                    items(5) {
                        ArticleCardShimmer()
                    }
                } else if (articles.isNotEmpty()) {
                    
                    // Split articles into Latest, Popular, and Running for sections
                    val latestNews = articles.take(5)
                    val popularNews = articles.drop(5).take(5)
                    val runningNews = articles.drop(10)

                    // 1. Running News (Scrolling Row)
                    if (runningNews.isNotEmpty()) {
                        item {
                            SectionHeader(title = "চলমান সংবাদ")
                        }
                        item {
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                contentPadding = PaddingValues(vertical = 4.dp)
                            ) {
                                items(runningNews.take(5)) { article ->
                                    CompactArticleCard(
                                        article = article,
                                        onClick = { onArticleClick(article) }
                                    )
                                }
                            }
                        }
                    }

                    // 2. Latest News
                    if (latestNews.isNotEmpty()) {
                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            SectionHeader(title = "সর্বশেষ সংবাদ")
                        }
                        items(latestNews) { article ->
                            ArticleCard(
                                article = article,
                                onClick = { onArticleClick(article) },
                                onBookmarkClick = { onBookmarkClick(article.url) }
                            )
                        }
                    }

                    // 3. Popular News
                    if (popularNews.isNotEmpty()) {
                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            SectionHeader(title = "জনপ্রিয় সংবাদ")
                        }
                        items(popularNews) { article ->
                            ArticleCard(
                                article = article,
                                onClick = { onArticleClick(article) },
                                onBookmarkClick = { onBookmarkClick(article.url) }
                            )
                        }
                    }
                    
                } else if (!isLoading) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(R.string.no_articles),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

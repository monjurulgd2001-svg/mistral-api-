package com.volunteernews24.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.basicMarquee
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
    currentPage: Int,
    onNextPage: () -> Unit,
    onPreviousPage: () -> Unit,
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
                        AsyncImage(
                            model = "https://www.volunteernews24.com/wp-content/uploads/2021/04/Volunteer-News-Logo-1.png",
                            contentDescription = "VolunteerNews24 Logo",
                            modifier = Modifier
                                .height(48.dp)
                                .padding(bottom = 4.dp),
                            contentScale = ContentScale.Fit
                        )
                        Text(
                            text = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("bn", "BD")).format(Date()),
                            fontSize = 12.sp,
                            color = androidx.compose.ui.graphics.Color.DarkGray
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onAboutClick) {
                        Icon(Icons.Default.Info, contentDescription = "About Us", tint = VNRed)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = androidx.compose.ui.graphics.Color.White,
                    titleContentColor = androidx.compose.ui.graphics.Color.Black
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
                    
                    // Split articles into Featured, Latest, Popular, and Running
                    val featuredArticle = articles.firstOrNull { it.imageUrl.isNotBlank() }
                    val remainingArticles = if (featuredArticle != null) articles.filter { it.url != featuredArticle.url } else articles

                    val latestNews = remainingArticles.take(5)
                    val popularNews = remainingArticles.drop(5).take(5)
                    val runningNews = remainingArticles.drop(10)

                    // 0. Featured News Section
                    if (featuredArticle != null) {
                        item {
                            FeaturedArticleCard(
                                article = featuredArticle,
                                onClick = { onArticleClick(featuredArticle) }
                            )
                        }
                    }

                    // 1. Running News (Scrolling Marquee for Headlines)
                        if (runningNews.isNotEmpty()) {
                            item {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(MaterialTheme.colorScheme.surfaceVariant, shape = MaterialTheme.shapes.small)
                                        .padding(8.dp)
                                ) {
                                    Text(
                                        text = "শিরোনাম:",
                                        fontWeight = FontWeight.Bold,
                                        color = VNRed,
                                        modifier = Modifier.padding(end = 8.dp)
                                    )
                                    Text(
                                        text = runningNews.joinToString(" • ") { it.title },
                                        modifier = Modifier.basicMarquee(
                                            iterations = Int.MAX_VALUE,
                                            velocity = 30.dp
                                        ),
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
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
                                .height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(R.string.no_articles),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                // 4. Pagination (Always show if not loading so user can go back)
                if (!isLoading) {
                    item {
                        PaginationControls(
                            currentPage = currentPage,
                            onPreviousPage = onPreviousPage,
                            onNextPage = onNextPage
                        )
                    }
                }
            }
        }
    }
}

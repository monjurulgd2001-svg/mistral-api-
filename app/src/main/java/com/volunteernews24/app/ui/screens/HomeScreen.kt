package com.volunteernews24.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.activity.compose.BackHandler
import android.widget.Toast
import coil.compose.AsyncImage
import com.volunteernews24.app.R
import com.volunteernews24.app.data.model.Article
import com.volunteernews24.app.data.repository.WeatherInfo
import com.volunteernews24.app.ui.components.*
import com.volunteernews24.app.ui.theme.VNRed
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    isLoading: Boolean,
    isRefreshing: Boolean,
    isOffline: Boolean,
    articles: List<Article>,
    categories: List<com.volunteernews24.app.data.model.Category>,
    currentPage: Int,
    onNextPage: () -> Unit,
    onPreviousPage: () -> Unit,
    onRefresh: () -> Unit,
    weatherInfo: WeatherInfo?,
    searchQuery: String,
    searchResults: List<Article>,
    onSearchQueryChange: (String) -> Unit,
    onArticleClick: (Article) -> Unit,
    onCategoryClick: (com.volunteernews24.app.data.model.Category) -> Unit,
    onBookmarkClick: (String) -> Unit,
    onAboutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var backPressedTime by remember { mutableLongStateOf(0L) }
    val context = LocalContext.current

    BackHandler(enabled = true) {
        if (System.currentTimeMillis() - backPressedTime < 2000) {
            (context as? android.app.Activity)?.finish()
        } else {
            backPressedTime = System.currentTimeMillis()
            Toast.makeText(context, "অ্যাপ থেকে বের হতে আবার ব্যাক প্রেস করুন", Toast.LENGTH_SHORT).show()
        }
    }


    Scaffold(
        topBar = {
            Column(
                modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface)
            ) {
                // Black Top Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(androidx.compose.ui.graphics.Color(0xFF151515))
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = SimpleDateFormat("EEEE, dd MMMM, yyyy", Locale("bn", "BD")).format(Date()),
                        fontSize = 11.sp,
                        color = androidx.compose.ui.graphics.Color.LightGray
                    )
                    
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (weatherInfo != null) {
                            Text(
                                text = "${weatherInfo.city} | ${weatherInfo.temperatureCelsius}°C ☁️",
                                fontSize = 11.sp,
                                color = androidx.compose.ui.graphics.Color.LightGray
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                        
                        IconButton(
                            onClick = onAboutClick,
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                Icons.Filled.Info,
                                contentDescription = "About",
                                tint = androidx.compose.ui.graphics.Color.LightGray,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }

                // Logo
                val context = androidx.compose.ui.platform.LocalContext.current
                val imageLoader = remember {
                    coil.ImageLoader.Builder(context)
                        .components { add(coil.decode.SvgDecoder.Factory()) }
                        .build()
                }
                Box(
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp, bottom = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = R.raw.new_logo,
                        imageLoader = imageLoader,
                        contentDescription = "VolunteerNews24 Logo",
                        modifier = Modifier
                            .fillMaxWidth(0.85f) // Increased from 0.65f
                            .height(90.dp), // Increased from 65.dp
                        contentScale = ContentScale.Fit
                    )
                }
                HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
            }
        }
    ) { paddingValues ->
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = { onRefresh() },
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
                // Search Bar
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = onSearchQueryChange,
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text(stringResource(R.string.search_hint)) },
                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(30.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = androidx.compose.ui.graphics.Color.Transparent
                            )
                        )
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        // Category Horizontal List
                        if (categories.isNotEmpty()) {
                            LazyRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(categories) { category ->
                                    FilterChip(
                                        selected = false,
                                        onClick = { onCategoryClick(category) },
                                        label = { Text(category.name, fontWeight = FontWeight.Bold) },
                                        shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
                                        colors = FilterChipDefaults.filterChipColors(
                                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                            labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    )
                                }
                            }
                        }
                    }
                }

                if (isOffline) {
                    item {
                        OfflineBanner()
                    }
                }

                if (isLoading && articles.isEmpty()) {
                    items(5) {
                        ArticleCardShimmer()
                    }
                } else if (searchQuery.isNotBlank()) {
                    if (searchResults.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "কোনো সংবাদ পাওয়া যায়নি",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    } else {
                        items(searchResults) { article ->
                            ArticleCard(
                                article = article,
                                onClick = { onArticleClick(article) },
                                onBookmarkClick = { onBookmarkClick(article.url) }
                            )
                        }
                    }
                } else if (articles.isNotEmpty()) {
                    
                    if (currentPage == 1) {
                        // Split articles into Featured, Latest, Popular, and Running
                        val featuredArticles = articles.filter { it.imageUrl.isNotBlank() }.take(3)
                        val remainingArticles = articles.filterNot { featuredArticles.contains(it) }

                        val latestNews = remainingArticles.take(5)
                        val popularNews = remainingArticles.drop(5).take(5)
                        val runningNews = remainingArticles.drop(10)

                        // 0. Breaking News Stories (Instagram Style)
                        val storyArticles = articles.filter { it.imageUrl.isNotBlank() }.take(8)
                        if (storyArticles.isNotEmpty()) {
                            item(key = "stories_row") {
                                LazyRow(
                                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    items(storyArticles) { article ->
                                        StoryAvatar(
                                            article = article,
                                            onClick = { onArticleClick(article) }
                                        )
                                    }
                                }
                            }
                        }

                        // 1. Featured News Slider
                        if (featuredArticles.isNotEmpty()) {
                            item(key = "featured_slider") {
                                FeaturedNewsSlider(
                                    articles = featuredArticles,
                                    onArticleClick = onArticleClick
                                )
                            }
                        }

                        // 1. Running News (Breaking News Ticker)
                        if (runningNews.isNotEmpty()) {
                            item {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(androidx.compose.ui.graphics.Color(0xFFFFF0F0), shape = MaterialTheme.shapes.small)
                                        .padding(4.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .background(VNRed, shape = androidx.compose.foundation.shape.RoundedCornerShape(4.dp))
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = "তাজা খবর",
                                            fontWeight = FontWeight.Bold,
                                            color = androidx.compose.ui.graphics.Color.White,
                                            fontSize = 12.sp
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = runningNews.joinToString(" • ") { it.title },
                                        modifier = Modifier.basicMarquee(
                                            iterations = Int.MAX_VALUE,
                                            velocity = 35.dp
                                        ),
                                        color = VNRed,
                                        fontWeight = FontWeight.Medium
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
                    } else {
                        // For Page 2 and above, just display a flat list of articles
                        items(articles) { article ->
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

                // 4. Pagination (Always show if not loading so user can go back, but hide during search)
                if (!isLoading && searchQuery.isBlank()) {
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

/**
 * Self-contained featured news slider composable.
 * Keeping rememberPagerState and LaunchedEffect here (valid @Composable context)
 * avoids the "@Composable invocations can only happen from @Composable function" error
 * that occurs when they are placed directly inside a LazyListScope lambda.
 */
@Composable
fun FeaturedNewsSlider(
    articles: List<com.volunteernews24.app.data.model.Article>,
    onArticleClick: (com.volunteernews24.app.data.model.Article) -> Unit
) {
    val pagerState = androidx.compose.foundation.pager.rememberPagerState(
        pageCount = { articles.size }
    )

    // Infinite auto-cycle loop — restarts only when article count changes
    LaunchedEffect(articles.size) {
        while (true) {
            kotlinx.coroutines.delay(3500)
            if (articles.size > 1) {
                val next = (pagerState.currentPage + 1) % articles.size
                try {
                    pagerState.animateScrollToPage(
                        next,
                        animationSpec = androidx.compose.animation.core.tween(
                            durationMillis = 600,
                            easing = androidx.compose.animation.core.FastOutSlowInEasing
                        )
                    )
                } catch (_: Exception) {
                    // User swiped mid-animation — continue loop
                }
            }
        }
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        androidx.compose.foundation.pager.HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth().height(230.dp),
            pageSpacing = 8.dp,
            beyondViewportPageCount = 1
        ) { page ->
            val article = articles[page]
            com.volunteernews24.app.ui.components.FeaturedArticleCard(
                article = article,
                onClick = { onArticleClick(article) }
            )
        }

        // Animated pill indicators
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(articles.size) { iteration ->
                val isSelected = pagerState.currentPage == iteration
                val dotWidth by androidx.compose.animation.core.animateDpAsState(
                    targetValue = if (isSelected) 20.dp else 6.dp,
                    animationSpec = androidx.compose.animation.core.tween(300),
                    label = "dotWidth_$iteration"
                )
                Box(
                    modifier = Modifier
                        .padding(horizontal = 3.dp, vertical = 4.dp)
                        .height(6.dp)
                        .width(dotWidth)
                        .background(
                            if (isSelected) com.volunteernews24.app.ui.theme.VNRed
                            else androidx.compose.ui.graphics.Color.LightGray,
                            androidx.compose.foundation.shape.RoundedCornerShape(3.dp)
                        )
                )
            }
        }
    }
}

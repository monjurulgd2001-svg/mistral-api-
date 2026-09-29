package com.volunteernews24.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.volunteernews24.app.data.model.Article
import com.volunteernews24.app.ui.components.ArticleCard
import com.volunteernews24.app.ui.components.ArticleCardShimmer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(
    categoryName: String,
    categoryUrl: String,
    articles: List<Article>,
    currentPage: Int,
    isLoading: Boolean,
    onLoadCategory: (String) -> Unit,
    onNextPage: () -> Unit,
    onPreviousPage: () -> Unit,
    onArticleClick: (Article) -> Unit,
    onBookmarkClick: (String) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(categoryUrl) {
        onLoadCategory(categoryUrl)
    }

    Column(modifier = modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text(text = categoryName, fontWeight = FontWeight.Bold) },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface,
                titleContentColor = MaterialTheme.colorScheme.onSurface
            )
        )

        if (isLoading && articles.isEmpty()) {
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(5) { ArticleCardShimmer() }
            }
        } else if (articles.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(articles) { article ->
                    ArticleCard(
                        article = article,
                        onClick = { onArticleClick(article) },
                        onBookmarkClick = { onBookmarkClick(article.url) }
                    )
                }
            }
        } else if (!isLoading) {
            Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(text = "কোনো সংবাদ পাওয়া যায়নি", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }

        if (!isLoading) {
            com.volunteernews24.app.ui.components.PaginationControls(
                currentPage = currentPage,
                onPreviousPage = onPreviousPage,
                onNextPage = onNextPage,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

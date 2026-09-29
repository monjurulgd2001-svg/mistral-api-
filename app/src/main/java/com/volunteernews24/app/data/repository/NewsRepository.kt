package com.volunteernews24.app.data.repository

import com.volunteernews24.app.data.model.Article
import com.volunteernews24.app.data.model.Category
import com.volunteernews24.app.data.model.Ebook
import com.volunteernews24.app.data.remote.NewsScraper
import kotlinx.coroutines.flow.first

class NewsRepository(
    private val bookmarkManager: BookmarkManager
) {
    // ─── Articles ───────────────────────────────────────────────────

    suspend fun getHomeArticles(page: Int = 1): List<Article> {
        val remoteArticles = NewsScraper.fetchHomeArticles(page)
        return applyBookmarks(remoteArticles)
    }

    suspend fun getCategoryArticles(categoryUrl: String, page: Int = 1): List<Article> {
        val remoteArticles = NewsScraper.fetchCategoryArticles(categoryUrl, page)
        return applyBookmarks(remoteArticles)
    }

    suspend fun getArticleDetail(url: String): Article? {
        val detail = NewsScraper.fetchArticleDetail(url)
        return detail?.let { applyBookmarks(listOf(it)).first() }
    }

    suspend fun searchArticles(query: String): List<Article> {
        // Mock search using getHomeArticles for real-time since site might not have API
        val remote = getHomeArticles(1)
        val lowerQuery = query.lowercase()
        return remote.filter { 
            it.title.lowercase().contains(lowerQuery) || it.excerpt.lowercase().contains(lowerQuery) 
        }
    }

    private suspend fun applyBookmarks(articles: List<Article>): List<Article> {
        val bookmarkedUrls = bookmarkManager.bookmarkedArticles.first().map { it.url }.toSet()
        return articles.map { it.copy(isBookmarked = bookmarkedUrls.contains(it.url)) }
    }

    // ─── Categories ─────────────────────────────────────────────────

    suspend fun fetchCategories(): List<Category> = NewsScraper.fetchCategories()

    // ─── Ebooks (Smart Green Library) ───────────────────────────────

    suspend fun fetchEbooks(): List<Ebook> = NewsScraper.fetchEbooks()

    // ─── Bookmarks ──────────────────────────────────────────────────
    
    fun getBookmarkedArticles() = bookmarkManager.bookmarkedArticles

    suspend fun toggleBookmark(article: Article): Boolean {
        return bookmarkManager.toggleBookmark(article)
    }
}

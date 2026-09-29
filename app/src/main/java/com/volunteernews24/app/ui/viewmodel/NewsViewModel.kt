package com.volunteernews24.app.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.volunteernews24.app.VolunteerNewsApp
import com.volunteernews24.app.data.model.Article
import com.volunteernews24.app.data.model.Category
import com.volunteernews24.app.data.model.Ebook
import com.volunteernews24.app.data.repository.InboxManager
import com.volunteernews24.app.data.repository.InboxMessage
import com.volunteernews24.app.data.repository.NewsRepository
import com.volunteernews24.app.data.repository.WeatherInfo
import com.volunteernews24.app.data.repository.WeatherManager
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class UiState(
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val error: String? = null,
    val articles: List<Article> = emptyList(),
    val bookmarkedArticles: List<Article> = emptyList(),
    val categoryArticles: List<Article> = emptyList(),
    val searchResults: List<Article> = emptyList(),
    val categories: List<Category> = emptyList(),
    val ebooks: List<Ebook> = emptyList(),
    val inboxMessages: List<InboxMessage> = emptyList(),
    val currentArticle: Article? = null,
    val isArticleLoading: Boolean = false,
    val isOffline: Boolean = false,
    val bookmarkMessage: String? = null,
    val homePage: Int = 1,
    val categoryPage: Int = 1,
    val weatherInfo: WeatherInfo? = null
)

class NewsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = NewsRepository(com.volunteernews24.app.data.repository.BookmarkManager(application))
    private val inboxManager = InboxManager(application)

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        observeBookmarks()
        loadHomeArticles()
        loadCategories()
        loadWeather()
        observeInbox()
    }

    private fun observeInbox() {
        viewModelScope.launch {
            inboxManager.inboxMessages.collect { messages ->
                _uiState.update { it.copy(inboxMessages = messages) }
            }
        }
    }

    fun clearInbox() {
        viewModelScope.launch {
            inboxManager.clearMessages()
        }
    }

    private fun loadWeather() {
        viewModelScope.launch {
            val weather = WeatherManager.fetchCurrentWeather()
            _uiState.update { it.copy(weatherInfo = weather) }
        }
    }

    private fun observeBookmarks() {
        viewModelScope.launch {
            repository.getBookmarkedArticles().collect { bookmarked ->
                _uiState.update { it.copy(bookmarkedArticles = bookmarked) }
            }
        }
    }

    // ─── Network Fetching (Real-time Online) ────────────────────────

    fun loadHomeArticles(page: Int = 1) {
        if (page < 1) return
        viewModelScope.launch {
            if (page == 1) {
                _uiState.update { it.copy(isLoading = true, error = null, homePage = page, articles = emptyList()) }
            } else {
                _uiState.update { it.copy(isLoading = true, error = null, homePage = page) }
            }
            try {
                val newArticles = repository.getHomeArticles(page)
                _uiState.update { 
                    val currentArticles = if (page == 1) emptyList() else it.articles
                    val combinedArticles = (currentArticles + newArticles).distinctBy { it.url }
                    it.copy(isLoading = false, isOffline = false, articles = combinedArticles)
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isOffline = true,
                        error = "Please check your internet connection."
                    )
                }
            }
        }
    }
    
    fun nextHomePage() {
        loadHomeArticles(_uiState.value.homePage + 1)
    }
    
    fun previousHomePage() {
        if (_uiState.value.homePage > 1) {
            loadHomeArticles(_uiState.value.homePage - 1)
        }
    }

    fun refreshArticles() {
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true, error = null) }
            try {
                // Always refresh from page 1 with fresh data
                val freshArticles = repository.getHomeArticles(1)
                _uiState.update {
                    it.copy(
                        isRefreshing = false,
                        isOffline = false,
                        error = null,
                        articles = freshArticles,
                        homePage = 1
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isRefreshing = false, isOffline = true) }
            }
        }
    }

    fun loadCategories() {
        viewModelScope.launch {
            try {
                val categories = repository.fetchCategories()
                _uiState.update { it.copy(categories = categories) }
            } catch (e: Exception) {
                // Fail silently
            }
        }
    }

    fun loadCategoryArticles(categoryUrl: String, page: Int = 1) {
        if (page < 1) return
        viewModelScope.launch {
            if (page == 1) {
                _uiState.update { it.copy(isLoading = true, categoryArticles = emptyList(), categoryPage = page) }
            } else {
                _uiState.update { it.copy(isLoading = true, categoryPage = page) }
            }
            try {
                val newArticles = repository.getCategoryArticles(categoryUrl, page)
                _uiState.update { 
                    val currentArticles = if (page == 1) emptyList() else it.categoryArticles
                    val combinedArticles = (currentArticles + newArticles).distinctBy { it.url }
                    it.copy(isLoading = false, categoryArticles = combinedArticles)
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = "Failed to load category.") }
            }
        }
    }

    fun nextCategoryPage(categoryUrl: String) {
        loadCategoryArticles(categoryUrl, _uiState.value.categoryPage + 1)
    }

    fun previousCategoryPage(categoryUrl: String) {
        if (_uiState.value.categoryPage > 1) {
            loadCategoryArticles(categoryUrl, _uiState.value.categoryPage - 1)
        }
    }

    fun loadArticleDetail(url: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isArticleLoading = true, currentArticle = null) }
            try {
                val article = repository.getArticleDetail(url)
                _uiState.update { it.copy(isArticleLoading = false, currentArticle = article) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isArticleLoading = false, error = "Failed to load article.") }
            }
        }
    }

    fun loadEbooks() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val ebooks = repository.fetchEbooks()
                _uiState.update { it.copy(isLoading = false, ebooks = ebooks) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    // ─── User Actions ───────────────────────────────────────────────

    fun toggleBookmark(articleUrl: String) {
        viewModelScope.launch {
            // Find full article from lists
            val articleToBookmark = _uiState.value.articles.find { it.url == articleUrl }
                ?: _uiState.value.categoryArticles.find { it.url == articleUrl }
                ?: _uiState.value.currentArticle?.takeIf { it.url == articleUrl }
                ?: _uiState.value.searchResults.find { it.url == articleUrl }
                ?: _uiState.value.bookmarkedArticles.find { it.url == articleUrl }

            articleToBookmark?.let { article ->
                val isNowBookmarked = repository.toggleBookmark(article)
                _uiState.update {
                    it.copy(
                        bookmarkMessage = if (isNowBookmarked) "সংবাদ সংরক্ষিত হয়েছে"
                        else "সংবাদ সংরক্ষণ বাতিল হয়েছে"
                    )
                }
                
                // Update local lists immediately for snappier UI
                val updatedArticles = _uiState.value.articles.map { if(it.url == articleUrl) it.copy(isBookmarked = isNowBookmarked) else it }
                val updatedCategoryArticles = _uiState.value.categoryArticles.map { if(it.url == articleUrl) it.copy(isBookmarked = isNowBookmarked) else it }
                
                _uiState.update {
                    it.copy(
                        articles = updatedArticles,
                        categoryArticles = updatedCategoryArticles,
                        currentArticle = if (it.currentArticle?.url == articleUrl) it.currentArticle.copy(isBookmarked = isNowBookmarked) else it.currentArticle
                    )
                }
            }
        }
    }

    fun clearBookmarkMessage() {
        _uiState.update { it.copy(bookmarkMessage = null) }
    }

    fun searchArticles(query: String) {
        if (query.isBlank()) {
            _uiState.update { it.copy(searchResults = emptyList()) }
            return
        }
        viewModelScope.launch {
            val results = repository.searchArticles(query)
            _uiState.update { it.copy(searchResults = results) }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}

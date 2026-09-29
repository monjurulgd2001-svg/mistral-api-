package com.volunteernews24.app.data.model

/**
 * Represents a news article scraped from volunteernews24.com.
 * Serves as the domain model.
 */
data class Article(
    val url: String,
    val title: String,
    val excerpt: String = "",
    val content: String = "",
    val imageUrl: String = "",
    val category: String = "",
    val date: String = "",
    val author: String = "",
    val isBookmarked: Boolean = false,
    val fetchedAt: Long = System.currentTimeMillis()
)

/**
 * Represents a news category from the site navigation.
 */
data class Category(
    val name: String,
    val slug: String,
    val url: String,
    val subcategories: List<Category> = emptyList()
)

/**
 * Represents an ebook/document in the Smart Green Library.
 */
data class Ebook(
    val pdfUrl: String,
    val title: String,
    val coverImageUrl: String = "",
    val fetchedAt: Long = System.currentTimeMillis()
)

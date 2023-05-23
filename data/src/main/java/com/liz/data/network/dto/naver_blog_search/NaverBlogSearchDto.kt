package com.liz.data.network.dto.naver_blog_search

data class NaverBlogSearchDto(
    val lastBuildDate: String?,
    val total: Int,
    val start: Int,
    val display: Int,
    val items: List<NaverBlogSearchItemDto?>?
)
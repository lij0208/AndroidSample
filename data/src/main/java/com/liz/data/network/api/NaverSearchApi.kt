package com.liz.data.network.api

import com.liz.data.network.dto.naver_blog_search.NaverBlogSearchDto
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface NaverSearchApi {

    @GET("/v1/search/blog.json")
    suspend fun searchBlog(
        @Header("X-Naver-Client-Id") id: String,
        @Header("X-Naver-Client-Secret") password: String,
        @Query("query") query: String,
        @Query("display") display: Int,
        @Query("start") start: Int,
        @Query("sort") sort: String
    ) : NaverBlogSearchDto
}
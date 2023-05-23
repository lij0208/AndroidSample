package com.liz.data.network.dto.mapper

import com.liz.data.network.dto.naver_blog_search.NaverBlogSearchItemDto
import com.liz.data.network.dto.mapper.base.ListMapper
import com.liz.domain.entity.SearchItemEntity
import javax.inject.Inject

class SearchItemMapper @Inject constructor() : ListMapper<NaverBlogSearchItemDto, SearchItemEntity> {

    override fun map(input: List<NaverBlogSearchItemDto?>?): List<SearchItemEntity> {
        return input?.filterNotNull()?.map {
            SearchItemEntity(
                title = it.title ?: "",
                link = it.link ?: "",
                description = it.description ?: "",
                bloggername = it.bloggername ?: "",
                bloggerlink = it.bloggerlink ?: "",
                postdate = it.postdate ?: "",
            )
        } ?: emptyList()
    }
}
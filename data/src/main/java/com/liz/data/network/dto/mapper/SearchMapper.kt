package com.liz.data.network.dto.mapper

import com.liz.data.network.dto.naver_blog_search.NaverBlogSearchDto
import com.liz.data.network.dto.mapper.base.Mapper
import com.liz.domain.entity.SearchEntity
import javax.inject.Inject

class SearchMapper @Inject constructor(
    private val searchItemMapper: SearchItemMapper
) : Mapper<NaverBlogSearchDto, SearchEntity> {

    override fun map(input: NaverBlogSearchDto?): SearchEntity {
        return SearchEntity(
            lastbuilddate = input?.lastBuildDate ?: "",
            total = input?.total ?: 0,
            start = input?.start ?: -1,
            display = input?.display ?: 0,
            itemsEntity = searchItemMapper.map(input?.items)
        )
    }
}
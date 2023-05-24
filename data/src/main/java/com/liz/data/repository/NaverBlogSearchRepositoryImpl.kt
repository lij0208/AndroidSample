package com.liz.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.liz.data.datasource.NaverBlogSearchDataSource
import com.liz.data.network.api.NaverSearchApi
import com.liz.data.network.dto.mapper.SearchMapper
import com.liz.domain.entity.SearchItemEntity
import com.liz.domain.param.SearchParam
import com.liz.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NaverBlogSearchRepositoryImpl @Inject constructor(
    private val api: NaverSearchApi,
    private val mapper: SearchMapper
) : SearchRepository {

    override fun search(param: SearchParam): Flow<PagingData<SearchItemEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = param.display,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                NaverBlogSearchDataSource(param, api, mapper)
            }
        ).flow
    }

}
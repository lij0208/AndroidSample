package com.liz.data.repository

import com.liz.data.BuildConfig
import com.liz.data.network.api.NaverSearchApi
import com.liz.data.network.dto.mapper.SearchMapper
import com.liz.domain.entity.SearchEntity
import com.liz.domain.param.SearchParam
import com.liz.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val api: NaverSearchApi,
    private val mapper: SearchMapper
) : SearchRepository {

    override fun search(param: SearchParam): Flow<SearchEntity> {
        return flow {
            val entity = param.run {
                mapper.map(
                    api.searchBlog(
                        BuildConfig.naver_client_id,
                        BuildConfig.naver_client_secret,
                        query, display, start, sort
                    )
                )
            }
            emit(entity)
        }
    }
}
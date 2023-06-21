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

class NaverBlogSearchRepositoryImpl @Inject constructor(
    private val api: NaverSearchApi,
    private val mapper: SearchMapper
) : SearchRepository {

    override fun search(param: SearchParam): Flow<SearchEntity> {
        return flow {
            val response = api.searchNaverBlog(
                BuildConfig.naver_client_id,
                BuildConfig.naver_client_secret,
                param.query,
                param.display,
                param.start,
                param.sort
            ).let {
                mapper.map(it)
            }
            emit(response)
        }
    }

}
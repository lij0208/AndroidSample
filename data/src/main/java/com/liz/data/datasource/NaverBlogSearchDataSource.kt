package com.liz.data.datasource

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.liz.data.BuildConfig
import com.liz.data.network.api.NaverSearchApi
import com.liz.data.network.dto.mapper.SearchMapper
import com.liz.domain.common.Constant.TAG
import com.liz.domain.entity.SearchItemEntity
import com.liz.domain.param.SearchParam
import javax.inject.Inject

class NaverBlogSearchDataSource @Inject constructor(
    private val param: SearchParam,
    private val api: NaverSearchApi,
    private val mapper: SearchMapper
) : PagingSource<Int, SearchItemEntity>() {

    override fun getRefreshKey(state: PagingState<Int, SearchItemEntity>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SearchItemEntity> {
        val position = params.key ?: param.start
        Log.d(TAG + "DATA", "position: $position / query: ${param.query}")
        return try {
            val response = api.searchNaverBlog(
                BuildConfig.naver_client_id,
                BuildConfig.naver_client_secret,
                param.query,
                if (position == param.start) param.display * FIRST_DISPLAY_MULTIPLE else param.display,
                position,
                param.sort
            ).let {
                mapper.map(it)
            }
            val repos = response.itemsEntity
            LoadResult.Page(
                data = repos,
                prevKey = if (position == param.start) null else position - 1,
                nextKey = if (response.display < param.display) null else position + (params.loadSize / param.display)
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    companion object {
        private const val FIRST_DISPLAY_MULTIPLE = 3
    }
}
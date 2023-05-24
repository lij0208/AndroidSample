package com.liz.domain.repository

import androidx.paging.PagingData
import com.liz.domain.entity.SearchItemEntity
import com.liz.domain.param.SearchParam
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    fun search(param: SearchParam) : Flow<PagingData<SearchItemEntity>>
}

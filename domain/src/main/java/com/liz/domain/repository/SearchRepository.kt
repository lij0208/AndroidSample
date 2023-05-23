package com.liz.domain.repository

import com.liz.domain.entity.SearchEntity
import com.liz.domain.param.SearchParam
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    fun search(param: SearchParam) : Flow<SearchEntity>
}

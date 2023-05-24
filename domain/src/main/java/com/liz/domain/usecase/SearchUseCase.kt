package com.liz.domain.usecase

import androidx.paging.PagingData
import com.liz.domain.di.IoDispatcher
import com.liz.domain.entity.SearchItemEntity
import com.liz.domain.param.SearchParam
import com.liz.domain.repository.SearchRepository
import com.liz.domain.usecase.common.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchUseCase @Inject constructor(
    @IoDispatcher dispatcher: CoroutineDispatcher,
    private val repository: SearchRepository
) : FlowUseCase<SearchParam, PagingData<SearchItemEntity>>(dispatcher) {

    override fun execute(parameters: SearchParam): Flow<PagingData<SearchItemEntity>> {
        return repository.search(parameters)
    }
}
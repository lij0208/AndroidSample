package com.liz.domain.usecase

import com.liz.domain.di.IoDispatcher
import com.liz.domain.entity.SearchEntity
import com.liz.domain.param.SearchParam
import com.liz.domain.repository.SearchRepository
import com.liz.domain.usecase.common.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchUseCase @Inject constructor(
    @IoDispatcher dispatcher: CoroutineDispatcher,
    private val repository: SearchRepository
) : FlowUseCase<SearchParam, SearchEntity>(dispatcher) {

    override fun execute(parameters: SearchParam): Flow<SearchEntity> {
        return repository.search(parameters)
    }
}
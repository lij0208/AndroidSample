package com.liz.presentation.ui.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.liz.domain.common.Constant
import com.liz.domain.param.SearchParam
import com.liz.domain.usecase.SearchUseCase
import com.liz.presentation.ui.search.actiondata.SearchAction
import com.liz.presentation.ui.search.viewdata.SearchState
import com.liz.presentation.ui.search.viewdata.SearchUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchUseCase: SearchUseCase,
    private val converter: SearchConverter
) : ViewModel() {

    private val _action = MutableSharedFlow<SearchAction>()
    val action = _action

    private val _uiState = MutableSharedFlow<SearchUi>()
    val uiState: StateFlow<SearchUi> = _uiState.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        SearchUi()
    )

    private var job: Job? = null
    private var tempQuery: String = ""

    private fun updateUi(ui: SearchUi) {
        viewModelScope.launch {
            _uiState.emit(ui)
        }
    }

    private fun updateAction(action: SearchAction) {
        viewModelScope.launch {
            _action.emit(action)
        }
    }

    private fun search() {
        viewModelScope.launch {
            Log.d(Constant.TAG, tempQuery)
            if (tempQuery.isBlank()) {
                updateEmptyQuery()
            } else {
                updateQuery()
                searchUseCase(
                    SearchParam(
                        tempQuery,
                        DISPLAY_PER_COUNT,
                        START_POSITION,
                        INIT_SORT
                    )
                ).cachedIn(viewModelScope)
                    .collectLatest {
                        updateUi(converter.convert(uiState.value, it))
                    }
            }
        }
    }

    private fun updateEmptyQuery() {
        updateUi(
            uiState.value.copy(
                state = SearchState.CLEAR_QUERY,
                viewData = uiState.value.viewData.copy(
                    list = null,
                    page = START_POSITION,
                    query = tempQuery
                )
            )
        )
    }

    private fun updateQuery() {
        updateUi(
            uiState.value.copy(
                state = SearchState.UPDATE_QUERY,
                viewData = uiState.value.viewData.copy(
                    page = START_POSITION,
                    query = tempQuery
                )
            )
        )
    }

    fun updateTempQuery(query: String?) {
        tempQuery = query ?: ""
    }

    fun searchAfterDelay() {
        if (job == null) {
            job = viewModelScope.launch {
                delay(THROTTLE_SEC)
                if (uiState.value.viewData.query != tempQuery) {
                    search()
                }
                job = null
            }
        }
    }

    fun refresh() {
        job?.cancel()
        job = null
        search()
    }

    fun updateError() {
        updateUi(
            uiState.value.copy(
                state = SearchState.ERROR
            )
        )
    }

    fun updateLoading(isLoading: Boolean) {
        updateAction(SearchAction.UpdateLoading(isLoading))
    }


    override fun onCleared() {
        job?.cancel()
        job = null
        super.onCleared()
    }

    companion object {
        private const val DISPLAY_PER_COUNT = 20
        private const val START_POSITION = 1
        private const val INIT_SORT = "sim"
        private const val THROTTLE_SEC = 1000L
    }
}
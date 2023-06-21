package com.liz.presentation.ui.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.liz.domain.common.Constant
import com.liz.domain.param.SearchParam
import com.liz.domain.usecase.SearchUseCase
import com.liz.presentation.ui.search.actiondata.SearchAction
import com.liz.presentation.ui.search.viewdata.END_POSITION
import com.liz.presentation.ui.search.viewdata.START_POSITION
import com.liz.presentation.ui.search.viewdata.SearchState
import com.liz.presentation.ui.search.viewdata.SearchUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
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

    private fun search(page: Int) {
        viewModelScope.launch {
            val query = tempQuery
            Log.d(Constant.TAG, tempQuery)
            if (query.isBlank()) {
                updateEmptyQuery(query)
            } else {
                searchUseCase(createSearchParam(query, page)).onEach {
                    updateUi(converter.convert(uiState.value, query, it))
                }.catch { error ->
                    updateError()
                }.collect()
            }
        }
    }

    private fun createSearchParam(query: String, page: Int) = SearchParam(
        query,
        SearchConverter.DISPLAY_PER_COUNT,
        page,
        SearchConverter.INIT_SORT
    )

    private fun updateEmptyQuery(query: String) {
        updateUi(
            uiState.value.copy(
                state = SearchState.CLEAR_QUERY,
                viewData = uiState.value.viewData.copy(
                    list = null,
                    page = END_POSITION,
                    query = query
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
                    updateUi(
                        uiState.value.copy(
                            state = SearchState.LOADING,
                            viewData = uiState.value.viewData.copy(
                                page = START_POSITION,
                                list = null
                            )
                        )
                    )
                    search(START_POSITION)
                }
                job = null
            }
        }
    }

    fun loadMore() {
        search(uiState.value.viewData.page)
    }

    fun refresh() {
        job?.cancel()
        job = null
        updateUi(
            uiState.value.copy(
                state = SearchState.LOADING,
                viewData = uiState.value.viewData.copy(
                    page = START_POSITION,
                    list = null
                )
            )
        )
        search(START_POSITION)
    }

    private fun updateError() {
        updateUi(
            uiState.value.copy(
                state = SearchState.ERROR
            )
        )
    }

    override fun onCleared() {
        job?.cancel()
        job = null
        super.onCleared()
    }

    companion object {
        private const val THROTTLE_SEC = 1000L
    }
}
package com.liz.presentation.ui.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.liz.domain.param.SearchParam
import com.liz.domain.usecase.SearchUseCase
import com.liz.presentation.common.Constant
import com.liz.presentation.ui.search.actiondata.SearchAction
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

    private fun update(ui: SearchUi) {
        viewModelScope.launch {
            _uiState.emit(ui)
        }
    }

    fun search() {
        viewModelScope.launch {
            Log.d(Constant.TAG, uiState.value.viewData.query)
            if (uiState.value.viewData.query.isBlank()) {
                update(
                    uiState.value.copy(
                        state = SearchState.SUCCESS,
                        viewData = uiState.value.viewData.copy(
                            list = emptyList(),
                            page = 0
                        )
                    )
                )
            } else {
                searchUseCase(
                    SearchParam(
                        uiState.value.viewData.query,
                        DISPLAY_PER_COUNT,
                        uiState.value.viewData.page + 1,
                        INIT_SORT
                    )
                ).onEach {
                    update(converter.convert(uiState.value, it))
                }.catch { error ->
                    error.printStackTrace()
                }.collect()
            }
        }
    }

    fun updateQuery(query: String?) {
        update(
            uiState.value.copy(
                viewData = uiState.value.viewData.copy(
                    query = query ?: ""
                )
            )
        )
    }

    fun searchAfterDelay() {
        if (job == null) {
            job = viewModelScope.launch {
                delay(THROTTLE_SEC)
                job = null
                search()
            }
        }
    }

    companion object {
        private const val DISPLAY_PER_COUNT = 10
        private const val INIT_SORT = "sim"
        private const val THROTTLE_SEC = 1000L
    }
}
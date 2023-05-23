package com.liz.presentation.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.liz.domain.param.SearchParam
import com.liz.domain.usecase.SearchUseCase
import com.liz.presentation.ui.search.viewdata.SearchUi
import dagger.hilt.android.lifecycle.HiltViewModel
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

    private val _uiState = MutableSharedFlow<SearchUi>()
    val uiState: StateFlow<SearchUi> = _uiState.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        SearchUi()
    )

    private fun update(ui: SearchUi) {
        viewModelScope.launch {
            _uiState.emit(ui)
        }
    }

    fun search(query: String) {
        viewModelScope.launch {
            searchUseCase(
                SearchParam(
                    query,
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

    companion object {
        private const val DISPLAY_PER_COUNT = 10
        private const val INIT_SORT = "sim"
    }
}
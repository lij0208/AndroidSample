package com.liz.presentationn.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.liz.domain.param.SearchParam
import com.liz.domain.usecase.SearchUseCase
import com.liz.presentationn.ui.search.adapter.SearchRecyclerData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchUseCase: SearchUseCase,
    private val converter: SearchConverter
) : ViewModel() {

    private val _stateUi = MutableLiveData<List<SearchRecyclerData>>()
    val stateUi: LiveData<List<SearchRecyclerData>> = _stateUi

    init {
        search()
    }

    private fun search() {
        viewModelScope.launch {
            searchUseCase(
                SearchParam(
                    "안드로이드",
                    10,
                    1,
                    "sim"
                )
            ).onEach {
                _stateUi.value = converter.convert(it)
            }.catch { error ->
                error.printStackTrace()
            }.collect()
        }
    }
}
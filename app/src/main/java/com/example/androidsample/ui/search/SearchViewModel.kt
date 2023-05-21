package com.example.androidsample.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor() : ViewModel() {

    private val _stateUi = MutableLiveData<List<String>>().apply {
        value = listOf(
            "1",
            "2",
            "3"
        )
    }
    val stateUi: LiveData<List<String>> = _stateUi
}
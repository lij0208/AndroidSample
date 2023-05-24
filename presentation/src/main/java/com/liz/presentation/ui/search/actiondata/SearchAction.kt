package com.liz.presentation.ui.search.actiondata

sealed class SearchAction {
    data class UpdateLoading(
        val isLoading: Boolean
    ) : SearchAction()
}
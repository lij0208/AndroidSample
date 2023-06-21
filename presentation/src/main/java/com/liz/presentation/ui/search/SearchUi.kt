package com.liz.presentation.ui.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.liz.presentation.common.ui.appbar.AppBar
import com.liz.presentation.ui.search.adapter.SearchRecyclerData
import com.liz.presentation.ui.search.viewdata.END_POSITION
import com.liz.presentation.ui.search.viewdata.SearchState
import com.liz.presentation.ui.search.viewdata.SearchUi
import kotlinx.coroutines.launch


@Preview
@Composable
private fun ShowSearchUi() {
    SearchUi(DrawerState(initialValue = DrawerValue.Closed))
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SearchUi(
    drawerState: DrawerState,
    searchViewModel: SearchViewModel = hiltViewModel(),
) {
    val searchQuery = remember { mutableStateOf("") }
    val searchUi by searchViewModel.uiState.collectAsStateWithLifecycle()
    val isRefreshing = searchUi.state == SearchState.LOADING
    val pullRefreshState = rememberPullRefreshState(
        isRefreshing,
        { searchViewModel.refresh() }
    )
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    Scaffold(topBar = {
        AppBar(
            drawerState = drawerState
        )
    }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
        ) {
            SearchBar(
                onSearchQueryChanged = { query ->
                    searchViewModel.updateTempQuery(query)
                    searchViewModel.searchAfterDelay()
                    scope.launch {
                        listState.scrollToItem(0)
                    }
                }, searchQuery = searchQuery
            )
            Box(
                modifier = Modifier
                    .pullRefresh(pullRefreshState)
                    .align(CenterHorizontally)
            ) {
                SearchBody(
                    searchUi = searchUi,
                    state = listState,
                    onLoadMore = {
                        searchViewModel.loadMore()
                    },
                    isLastPage = searchUi.viewData.page == END_POSITION
                )
                PullRefreshIndicator(
                    refreshing = isRefreshing,
                    state = pullRefreshState,
                    modifier = Modifier.align(Alignment.TopCenter)
                )
            }
        }
    }
}

@Preview
@Composable
private fun ShowSearchBar() {
    SearchBar({}, remember { mutableStateOf("") })
}

@Composable
private fun SearchBar(
    onSearchQueryChanged: (String) -> Unit, searchQuery: MutableState<String>
) {
    val focusRequester = remember { FocusRequester() }
    TextField(
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
        ),
        leadingIcon = {
            Icon(
                imageVector = Icons.Rounded.Search, contentDescription = null
            )
        },
        trailingIcon = {
            IconButton(onClick = {
                searchQuery.value = ""
                onSearchQueryChanged("")
            }) {
                Icon(
                    imageVector = Icons.Rounded.Close, contentDescription = null
                )
            }
        },
        value = searchQuery.value,
        onValueChange = {
            searchQuery.value = it
            onSearchQueryChanged(it)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .focusRequester(focusRequester),
    )
}

@Composable
private fun SearchBody(
    searchUi: SearchUi,
    isLastPage: Boolean,
    state: LazyListState,
    onLoadMore: () -> Unit,
) {
    searchUi.viewData.list?.let { list ->
        val lastIndex = list.lastIndex
        LazyColumn(
            state = state,
            modifier = Modifier.fillMaxSize()
        ) {
            itemsIndexed(list) { index, item ->
                if (!isLastPage && index == lastIndex) {
                    SideEffect {
                        onLoadMore()
                    }
                }
                when (item) {
                    is SearchRecyclerData.TitleItemData -> {
                        Text(
                            text = "${index + 1}. ${item.viewData.title}",
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}
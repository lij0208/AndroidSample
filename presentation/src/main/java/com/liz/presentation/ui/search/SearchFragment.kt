package com.liz.presentation.ui.search

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import com.liz.domain.common.Constant.TAG
import com.liz.presentation.common.base.fragment.BaseFragment
import com.liz.presentation.databinding.FragmentSearchBinding
import com.liz.presentation.ui.search.actiondata.SearchAction
import com.liz.presentation.ui.search.adapter.SearchAdapter
import com.liz.presentation.ui.search.viewdata.SearchState
import com.liz.presentation.ui.search.viewdata.SearchUi
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>() {

    private val viewModel: SearchViewModel by viewModels()

    override fun bindView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSearchBinding {
        return FragmentSearchBinding.inflate(inflater, container, false)
    }

    override fun initViews() {
        collect()
    }

    private fun collect() {
        collectAction()
        collectUiState()
    }

    private fun collectAction() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.action.onEach {
                Log.d(TAG, "collectAction: $it")
                when (it) {
                    is SearchAction.UpdateLoading -> updateLoading(it)
                }
            }.catch { error ->
                error.printStackTrace()
            }.collect()
        }
    }

    private fun updateLoading(action: SearchAction.UpdateLoading) {
        binding.swiperefresh.isRefreshing = action.isLoading
    }

    private fun collectUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collectLatest {
                    Log.d(TAG, "collectUiState: $it")
                    when (it.state) {
                        SearchState.INITIAL -> {
                            initRecyclerView()
                            initSearchView()
                            initRefreshView()
                        }

                        SearchState.CLEAR_QUERY,
                        SearchState.SUCCESS -> success(it)

                        SearchState.ERROR -> error()
                        else -> Unit
                    }
                }
            }
        }
    }

    private fun error() {
        binding.error.visibility = View.VISIBLE
    }

    private suspend fun success(ui: SearchUi) {
        binding.error.visibility = View.GONE
        ui.viewData.list?.let { list ->
            (binding.recyclerView.adapter as? SearchAdapter)?.submitData(list)
        } ?: (binding.recyclerView.adapter as? SearchAdapter)?.submitData(PagingData.empty())
    }

    private fun initRecyclerView() {
        viewLifecycleOwner.lifecycleScope.launch {
            val adapter = SearchAdapter()
            binding.recyclerView.adapter = adapter
            adapter.loadStateFlow.collectLatest { loadState ->
                isLoading(loadState)?.let {
                    viewModel.updateLoading(true)
                } ?: viewModel.updateLoading(false)

                isError(loadState)?.let {
                    viewModel.updateError()
                }
            }
        }
    }

    private fun isLoading(loadState: CombinedLoadStates) =
        (loadState.source.append as? LoadState.Loading
            ?: loadState.source.prepend as? LoadState.Loading
            ?: loadState.source.refresh as? LoadState.Loading
            ?: loadState.append as? LoadState.Loading
            ?: loadState.prepend as? LoadState.Loading
            ?: loadState.refresh as? LoadState.Loading)

    private fun isError(loadState: CombinedLoadStates) =
        (loadState.source.append as? LoadState.Error
            ?: loadState.source.prepend as? LoadState.Error
            ?: loadState.source.refresh as? LoadState.Error
            ?: loadState.append as? LoadState.Error
            ?: loadState.prepend as? LoadState.Error
            ?: loadState.refresh as? LoadState.Error)

    private fun initSearchView() {
        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.updateTempQuery(newText)
                viewModel.searchAfterDelay()
                return false
            }
        })
    }

    private fun initRefreshView() {
        binding.swiperefresh.setOnRefreshListener {
            viewModel.refresh()
        }
    }
}
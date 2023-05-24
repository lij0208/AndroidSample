package com.liz.presentation.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.PagingData
import com.liz.presentation.common.base.fragment.BaseFragment
import com.liz.presentation.databinding.FragmentSearchBinding
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
//                when (it) {
//                }
            }.catch { error ->
                error.printStackTrace()
            }.collect()
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun collectUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collectLatest {
                    when (it.state) {
                        SearchState.INITIAL -> {
                            initRecyclerView()
                            initSearchView()
                            initRefreshView()
                        }
                        SearchState.CLEAR_QUERY,
                        SearchState.SUCCESS -> success(it)
                        else -> Unit
                    }
                }
            }
        }
    }

    private suspend fun success(ui: SearchUi) {
        binding.swiperefresh.isRefreshing = false
        ui.viewData.list?.let { list ->
            (binding.recyclerView.adapter as? SearchAdapter)?.submitData(list)
        } ?: (binding.recyclerView.adapter as? SearchAdapter)?.submitData(PagingData.empty())
    }

    private fun initRecyclerView() {
        binding.recyclerView.adapter = SearchAdapter()
    }

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
            binding.swiperefresh.isRefreshing = true
            viewModel.refresh()
        }
    }
}
package com.liz.presentation.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.liz.presentation.common.base.fragment.BaseFragment
import com.liz.presentation.databinding.FragmentSearchBinding
import com.liz.presentation.ui.search.actiondata.SearchAction
import com.liz.presentation.ui.search.adapter.SearchAdapter
import com.liz.presentation.ui.search.viewdata.SearchState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
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
                when(it) {
                    is SearchAction.EmptyQuery -> showToast("검색어를 입력해주세요")
                }
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
                    when(it.state) {
                        SearchState.INITIAL -> {
                            initRecyclerView()
                            initSearchView()
                        }
                        SearchState.SUCCESS -> {
                            (binding.recyclerView.adapter as? SearchAdapter)?.submitList(it.viewData.list)
                        }
                    }
                }
            }
        }
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
                viewLifecycleOwner.lifecycleScope.launch {
                    delay(1000)
                    viewModel.search(
                        newText
                    )
                }
                return false
            }
        })
    }

}
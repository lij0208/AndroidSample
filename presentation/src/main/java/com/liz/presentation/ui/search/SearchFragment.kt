package com.liz.presentation.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.liz.presentation.common.base.fragment.BaseFragment
import com.liz.presentation.ui.search.adapter.SearchAdapter
import com.liz.presentation.databinding.FragmentSearchBinding
import com.liz.presentation.ui.search.viewdata.SearchState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
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
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collectLatest {
                    when(it.state) {
                        SearchState.INITIAL -> {
                            initRecyclerView()
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

}
package com.example.androidsample.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.androidsample.ui.search.adapter.SearchAdapter
import dagger.hilt.android.AndroidEntryPoint
import liz.android.sample.databinding.FragmentSearchBinding

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val searchViewModel = ViewModelProvider(this)[SearchViewModel::class.java]

        _binding = FragmentSearchBinding.inflate(inflater, container, false)
//        val root: View = binding.root

//        val textView: TextView = binding.textHome
        initRecyclerView()
        searchViewModel.stateUi.observe(viewLifecycleOwner) {
            (binding.recyclerView.adapter as? SearchAdapter)?.submitList(it)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initRecyclerView() {
        binding.recyclerView.adapter = SearchAdapter()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
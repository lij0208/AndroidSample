package com.liz.presentation.common.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

abstract class BaseFragment<T : ViewDataBinding> : Fragment() {

    private var _binding: T? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = bindView(inflater, container).also {
            it.lifecycleOwner = viewLifecycleOwner
        }
        initViews()
        return binding.root
    }

    abstract fun bindView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): T

    abstract fun initViews()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
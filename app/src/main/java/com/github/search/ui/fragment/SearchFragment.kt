package com.github.search.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager

import com.github.search.databinding.FragmentSearchBinding
import com.github.search.utils.hideKeyboard
import com.github.search.utils.showToast

class SearchFragment : Fragment() {
    private val viewModel: SearchViewModel by viewModels()
    private lateinit var binding: FragmentSearchBinding
    private var adapter: SearchAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews() // Initialize views and layout manager for RecyclerView
        setupViewModel() // Initialize the ViewModel
    }

    private fun initViews() {
        adapter = SearchAdapter(viewModel.languageCountResult.value?.list ?: listOf(),
            viewModel.languageCountResult.value?.totalCount ?: 0)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext()) // Set the layout manager for RecyclerView
        binding.recyclerView.adapter = adapter // Set the adapter for RecyclerView
    }

    private fun setupViewModel() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        // Observe the button enabled state from the ViewModel and update the UI accordingly
        viewModel.isButtonEnabled.observe(viewLifecycleOwner) {
            binding.searchButton.isEnabled = it
        }

        // Observe the search error from the ViewModel and show a toast message if there is an error
        viewModel.searchError.observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) {
                showToast(it)
            }
        }

        // Observe the language count result from the ViewModel and update the adapter with new data
        viewModel.languageCountResult.observe(viewLifecycleOwner) {
            adapter?.reload(it?.list ?: listOf(), it?.totalCount ?: 0)
        }

        // Observe the loading  from the ViewModel and update the ui
        viewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) {
                hideKeyboard()
            }
        }
    }
}


package com.example.teste.presentation.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.teste.R
import com.example.teste.databinding.FragmentHistoryBinding
import com.example.teste.framework.data.Repositories
import com.example.teste.framework.util.FileUtil
import com.example.teste.main.adapter.MainAdapter
import com.example.teste.main.data.response.ListDataWords
import com.example.teste.main.viewmodel.MainViewModel
import com.example.teste.main.viewstate.MainViewState
import com.example.teste.presentation.MainActivity
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding: FragmentHistoryBinding get() = _binding!!
    private lateinit var adapter: MainAdapter

    private val viewModel: MainViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentHistoryBinding.inflate(
        inflater,
        container,
        false
    ).apply {
        _binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.txtReturn.text = getString(R.string.history)
        viewModel.getHistoryWords()
        setupObserver()
    }

    private fun setupAdapter(isFavorite: List<Repositories>) {
        adapter = MainAdapter { dataWords ->
            viewModel.getWord(dataWords)
        }

        adapter.setList(isFavorite)
        binding.apply {
            recyclerViewReturn.adapter = adapter
            val gridLayoutManager = GridLayoutManager(requireContext(), 3)
            recyclerViewReturn.layoutManager = gridLayoutManager
        }
    }

    private fun setupObserver() {
        viewModel.listCallApi.observe(viewLifecycleOwner) {
            val bundle = Bundle().apply {
                putSerializable(FileUtil.KEY_BUNDLE, it)
            }
            findNavController().navigate(R.id.wordDetails, bundle)
        }

        viewModel.listRepositories.observe(viewLifecycleOwner) {
            setupAdapter(it)
        }

        viewModel.viewState.observe(viewLifecycleOwner){
            when (it){
                is MainViewState.Loading -> showLoading(it.show)
                is MainViewState.Error -> findNavController().navigate(R.id.error)
                else -> {}
            }
        }
    }

    override fun onStop() {
        super.onStop()
        viewModel.listCallApi = MutableLiveData()
    }

    private fun showLoading(show: Boolean) {
        (activity as MainActivity).showLoading(show)
    }
}
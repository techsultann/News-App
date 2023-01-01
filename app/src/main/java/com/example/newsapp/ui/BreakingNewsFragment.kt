package com.example.newsapp.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.MainActivity
import com.example.newsapp.R
import com.example.newsapp.adapters.NewsAdapter
import com.example.newsapp.databinding.FragmentBreakingNewsBinding
import com.example.newsapp.util.Resource


class BreakingNewsFragment : Fragment() {

    private var _binding: FragmentBreakingNewsBinding? = null
    private val binding get() =_binding!!
    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter

    val TAG = "Breaking News Fragment"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentBreakingNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel as NewsViewModel
        setupRecyclerView()

        // Showing articles in the web view of article fragment
        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(R.id.action_breakingNewsFragment_to_articleFragment, bundle)
        }

        viewModel.breakingNews.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let {
                        newsAdapter.differ.submitList(it.articles)
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let {
                        Log.e(TAG, "An error occurred: $it")
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }

            }
        })
    }

    private fun hideProgressBar() {

        binding.paginationProgressBar.visibility = View.INVISIBLE
    }
    private fun showProgressBar() {

        binding.paginationProgressBar.visibility = View.VISIBLE
    }


    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
        binding.rvBreakingNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
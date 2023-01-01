package com.example.newsapp.ui

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.MainActivity
import com.example.newsapp.R
import com.example.newsapp.adapters.NewsAdapter
import com.example.newsapp.databinding.FragmentSavedNewsBinding
import com.example.newsapp.databinding.FragmentSearchNewsBinding
import com.example.newsapp.util.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SearchNewsFragment : Fragment() {

    private var _binding: FragmentSearchNewsBinding? = null
    private val binding get() =_binding!!
    private lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSearchNewsBinding.inflate(inflater, container, false)
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
            findNavController().navigate(R.id.action_searchNewsFragment_to_articleFragment, bundle)
        }


        //Adding a little delay to the search queries
        var job : Job? = null
        binding.etSearch.addTextChangedListener {
            job?.cancel()
            job = MainScope().launch {
                delay(500L) // You can add a constant in the search and call it here
                it?.let {
                    if (it.toString().isNotEmpty()) {
                        viewModel.searchNews(it.toString())
                    }
                }
            }
        }


        viewModel.searchNews.observe(viewLifecycleOwner, Observer { response ->
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
                        Log.e(ContentValues.TAG, "An error occured: $it")
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
        binding.rvSearchNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}

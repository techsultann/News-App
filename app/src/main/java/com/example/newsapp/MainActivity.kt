package com.example.newsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.newsapp.databinding.ActivityMainBinding
import com.example.newsapp.db.ArticleDatabase
import com.example.newsapp.repository.NewsRepository
import com.example.newsapp.ui.NewsViewModel
import com.example.newsapp.ui.NewsViewModelProviderFactory
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var viewModel: ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val newsRepository =  NewsRepository(ArticleDatabase.getDatabase(this))
        val viewModelProviderFactory = NewsViewModelProviderFactory(newsRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory)[NewsViewModel::class.java]

        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.newsNavHostFragment) as NavHostFragment? ?: return

        val navController = host.navController

        setupBottomNavMenu(navController)

    }

    private fun setupBottomNavMenu(navController: NavController) {

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav?.setupWithNavController(navController)
    }
}
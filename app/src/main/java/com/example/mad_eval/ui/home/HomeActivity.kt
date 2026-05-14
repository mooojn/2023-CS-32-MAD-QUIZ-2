package com.example.mad_eval.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mad_eval.Constants
import com.example.mad_eval.R
import com.example.mad_eval.data.remote.RetrofitClient
import com.example.mad_eval.data.repository.NewsRepository
import com.example.mad_eval.databinding.ActivityHomeBinding
import com.example.mad_eval.ui.detail.DetailActivity

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val repository = NewsRepository(RetrofitClient.apiService)
                @Suppress("UNCHECKED_CAST")
                return HomeViewModel(repository) as T
            }
        }
        viewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]

        setupToolbar()
        setupRecyclerView()
        setupCountrySpinner()
        setupSwipeRefresh()
        observeViewModel()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter { article ->
            startActivity(
                Intent(this, DetailActivity::class.java).apply {
                    putExtra(DetailActivity.EXTRA_ARTICLE, article)
                }
            )
        }

        binding.recyclerViewNews.apply {
            layoutManager = LinearLayoutManager(this@HomeActivity)
            adapter = newsAdapter
        }
    }

    private fun setupCountrySpinner() {
        val countryNames = Constants.SUPPORTED_COUNTRIES.keys.toList()
        val spinnerAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            countryNames
        ).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        binding.spinnerCountry.adapter = spinnerAdapter
        binding.spinnerCountry.setSelection(countryNames.indexOfFirst { it == "US" })

        binding.spinnerCountry.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedName = countryNames.getOrNull(position) ?: "US"
                val code = Constants.SUPPORTED_COUNTRIES[selectedName] ?: "us"
                if (viewModel.selectedCountry.value != code) {
                    viewModel.fetchNews(code)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) = Unit
        }
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refresh()
        }
    }

    private fun observeViewModel() {
        viewModel.headlines.observe(this) { result ->
            binding.swipeRefreshLayout.isRefreshing = false
            when (result) {
                is NewsRepository.Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.recyclerViewNews.visibility = View.GONE
                    binding.textError.visibility = View.GONE
                    binding.textEmpty.visibility = View.GONE
                }

                is NewsRepository.Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val articles = result.articles
                    if (articles.isEmpty()) {
                        binding.recyclerViewNews.visibility = View.GONE
                        binding.textError.visibility = View.GONE
                        binding.textEmpty.visibility = View.VISIBLE
                    } else {
                        binding.recyclerViewNews.visibility = View.VISIBLE
                        binding.textError.visibility = View.GONE
                        binding.textEmpty.visibility = View.GONE
                        newsAdapter.submitList(articles)
                    }
                }

                is NewsRepository.Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.recyclerViewNews.visibility = View.GONE
                    binding.textEmpty.visibility = View.GONE
                    binding.textError.visibility = View.VISIBLE
                    binding.textError.text = result.message
                    Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_refresh -> {
                binding.swipeRefreshLayout.isRefreshing = true
                viewModel.refresh()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}

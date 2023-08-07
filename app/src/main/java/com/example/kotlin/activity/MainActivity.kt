package com.example.kotlin.activity

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlin.adapter.CarsListingAdapter
import com.example.kotlin.databinding.ActivityMainBinding
import com.example.kotlin.viewModel.ListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val listViewModel by lazy{ ViewModelProvider(this,defaultViewModelProviderFactory)[ListViewModel::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerview.layoutManager = LinearLayoutManager(this)

        val adapter = CarsListingAdapter(this@MainActivity)
        binding.recyclerview.adapter = adapter

        // Collect latest paging data from the ViewModel
        lifecycleScope.launch {
            listViewModel.getAllProducts()
            listViewModel.carList?.collectLatest { pagingData ->
                adapter.submitData(pagingData)
            }
        }

        // Handle loading and error states
        adapter.addLoadStateListener { loadState ->
            // Handle loading states here
            when (loadState.refresh) {
                is LoadState.Loading -> {
                    Log.d("LoadState", "Loading state: Loading...")
                }

                is LoadState.Error -> {
                    Log.d("LoadState", "Error state: Error loading data.")
                }

                else -> {
                    Log.d("LoadState", "Loading state: Not loading")
                }
            }

            // Handle end of pagination (end of data)
            if (loadState.append.endOfPaginationReached) {
                Log.d("LoadState", "End of pagination: All pages have been loaded.")
            }
        }
    }
    private fun showErrorMessage(errorMessage: String) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
    }

}
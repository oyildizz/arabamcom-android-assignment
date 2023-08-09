package com.example.kotlin.activity

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlin.R
import com.example.kotlin.adapter.CarsListingAdapter
import com.example.kotlin.databinding.ActivityMainBinding
import com.example.kotlin.util.Util.MAX_YEAR
import com.example.kotlin.util.Util.MIN_YEAR
import com.example.kotlin.viewModel.ListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val listViewModel by lazy {
        ViewModelProvider(
            this,
            defaultViewModelProviderFactory
        )[ListViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerview.layoutManager = LinearLayoutManager(this)

        val adapter = CarsListingAdapter(this@MainActivity)
        binding.recyclerview.adapter = adapter

        // Collect latest paging data from the ViewModel
        getFromApi(adapter)

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
        binding.buttonSort.setOnClickListener {
            showSortOptions(adapter)
        }
        binding.buttonFilter.setOnClickListener {
            showFilterDialog(adapter)
        }
    }

    private fun showSortOptions(adapter: CarsListingAdapter) {
        val popupMenu = PopupMenu(this, binding.buttonSort)
        popupMenu.menuInflater.inflate(R.menu.sort_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.price_sort_ascending -> {
                    listViewModel.sort = 0
                    listViewModel.sortDirection = 0 // Artan--eskiden yeniye
                }

                R.id.price_sort_descending -> {
                    listViewModel.sort = 0
                    listViewModel.sortDirection = 1 // Azalan--en yeniler
                }

                R.id.date_sort_ascending -> {
                    listViewModel.sort = 1
                    listViewModel.sortDirection = 0
                }

                R.id.date_sort_descending -> {
                    listViewModel.sort = 1
                    listViewModel.sortDirection = 1
                }

                R.id.year_sort_descending -> {
                    listViewModel.sort = 2
                    listViewModel.sortDirection = 1
                }

                R.id.year_sort_ascending -> {
                    listViewModel.sort = 2
                    listViewModel.sortDirection = 0
                }

            }
            getFromApi(adapter)
            true
        }
        popupMenu.show()
    }

    private fun getFromApi(adapter: CarsListingAdapter) {
        lifecycleScope.launch {
            listViewModel.getAllProducts()
            listViewModel.carList?.collectLatest { pagingData ->
                adapter.submitData(pagingData)
            }
        }
    }

    private fun showFilterDialog(adapter: CarsListingAdapter) {
        val dialogView = layoutInflater.inflate(R.layout.filter_layout, null)

        val editTextMinYear = dialogView.findViewById<EditText>(R.id.editTextMinYear)
        val editTextMaxYear = dialogView.findViewById<EditText>(R.id.editTextMaxYear)
        val buttonOk = dialogView.findViewById<Button>(R.id.buttonOk)
        val buttonRemoveFilter = dialogView.findViewById<Button>(R.id.buttonRemoveFilter)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        listViewModel.minYear.let { editTextMinYear.setText(it.toString()) }
        listViewModel.maxYear.let { editTextMaxYear.setText(it.toString()) }

        buttonOk.setOnClickListener {
            val inputMinYear = editTextMinYear.text.toString().toIntOrNull()
            val inputMaxYear = editTextMaxYear.text.toString().toIntOrNull()

            if (inputMinYear != null || inputMaxYear != null) {
                if (inputMinYear != null) {
                    listViewModel.minYear = inputMinYear
                }
                // Sadece boş değilse güncelle
                if (inputMaxYear != null) {
                    listViewModel.maxYear = inputMaxYear
                }
                if (inputMinYear != null) {
                    listViewModel.minYear = inputMinYear
                }
                getFromApi(adapter)
            }
            dialog.dismiss()
        }

        buttonRemoveFilter.setOnClickListener {
            listViewModel.minYear = MIN_YEAR
            listViewModel.maxYear = MAX_YEAR
            getFromApi(adapter)
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showErrorMessage(errorMessage: String) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
    }

}
package br.com.emdev.addressfinder.presentation.search

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.emdev.addressfinder.R
import br.com.emdev.addressfinder.data.response.AddressResponse
import br.com.emdev.addressfinder.databinding.ActivityMainBinding
import br.com.emdev.addressfinder.presentation.adapter.AddressAdapter
import br.com.emdev.addressfinder.presentation.base.BaseActivity
import br.com.emdev.addressfinder.utils.Functions
import com.google.android.material.textfield.TextInputEditText
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchActivity : BaseActivity() {
    private val searchViewModel: SearchViewModel by viewModel()
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)

        setupToolbar(binding.toolbarInclude.toolbarInclude, R.string.search_title)
        setupInputChangedListener()
        setupButtonSearchClickedListener()
        setupObservers()

    }

    private fun setupObservers() {

        searchViewModel.searchStateEvent.observe(this, { searchState ->
            when (searchState) {
                is SearchViewModel.SearchState.SearchZipCodeSuccess -> {
                    val list = listOf<AddressResponse>(searchState.address)
                    binding.rvAddress.let {
                        it.layoutManager = LinearLayoutManager(this)
                        it.adapter = AddressAdapter(list)
                    }
                    binding.tvBy.visibility=View.VISIBLE
                    Log.i("AddressByZipCode->", searchState.address.toString())

                }
                is SearchViewModel.SearchState.SearchStreetSuccess -> {
                    Log.i("AddressByStreet->", searchState.address.toString())

                    val addressList = searchState.address
                    binding.rvAddress.let {
                        it.layoutManager = LinearLayoutManager(this)
                        it.adapter = AddressAdapter(addressList)
                    }
                    binding.tvBy.visibility=View.VISIBLE

                }
                is SearchViewModel.SearchState.SearchError -> {
                    val builder = AlertDialog.Builder(
                            this@SearchActivity, R.style.Theme_AppCompat_Dialog_Alert
                    )
                    builder.setTitle("Atenção")
                    builder.setMessage(getString(searchState.error.second))
                    builder.setPositiveButton("OK") { dialog, id ->
                        dialog.dismiss()
                    }
                    builder.create().show()
                }
                is SearchViewModel.SearchState.InvalidationFields -> {
                    Log.i("fields", searchState.fields.toString())
                    val validationFieds: Map<String, TextInputEditText> = initValidationFields()
                    searchState.fields.forEach { fieldError ->
                        validationFieds[fieldError.first]?.error = getString(fieldError.second)
                    }
                }
                is SearchViewModel.SearchState.NotFound -> {
                    val builder = AlertDialog.Builder(
                            this@SearchActivity, R.style.Theme_AppCompat_Dialog_Alert
                    )
                    builder.setTitle("Atenção")
                    builder.setMessage(getString(searchState.error.second))
                    builder.setPositiveButton("OK") { dialog, id ->
                        dialog.dismiss()
                    }
                    builder.create().show()
                }

            }

        })
        searchViewModel.isLoading.observe(this, {
            showProgress(it)
        })

    }


    private fun setupButtonSearchClickedListener() {
        binding.btnSearchAddress.setOnClickListener {
            Functions().hideSoftKeyboard(this,binding.edtInputValue)
            searchViewModel.getAddress()
        }
    }

    private fun initValidationFields() = mapOf(

            SearchViewModel.CEP_NOT_FOUND.first to binding.edtInputValue,
            SearchViewModel.INPUT_EMPTY.first to binding.edtInputValue,
            SearchViewModel.INCORRET_SIZE_ZIP_CODE.first to binding.edtInputValue,
            SearchViewModel.INCORRET_SIZE_STREET.first to binding.edtInputValue
    )

    private fun setupInputChangedListener() {
        binding.edtInputValue.addTextChangedListener {
            searchViewModel.onUserChangedInputValue(it.toString())
        }

    }

    private fun showProgress(progress: Boolean) {
        if (progress) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

}
package br.com.emdev.addressfinder.presentation.search

import android.content.Context
import android.util.Log
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.emdev.addressfinder.R
import br.com.emdev.addressfinder.data.response.AddressResponse
import br.com.emdev.addressfinder.repository.AddressRepository
import br.com.emdev.addressfinder.utils.AppResult
import kotlinx.coroutines.launch

class SearchViewModel(private val repository: AddressRepository, private val context: Context) :
        ViewModel() {

    sealed class SearchState {
        class InvalidationFields(val fields: List<Pair<String, Int>>) : SearchState()
        class SearchZipCodeSuccess(val address: AddressResponse) : SearchState()
        class SearchStreetSuccess(val address: List<AddressResponse>) : SearchState()
        class SearchError(val error: Pair<String, Int>) : SearchState()
        class NotFound(val error: Pair<String, Int>) : SearchState()
    }

    private val _searchStateEvent = MutableLiveData<SearchState>()

    private var value = ""

    val searchStateEvent: LiveData<SearchState>
        get() = _searchStateEvent

    private val _isLoading = MutableLiveData(false)

    val isLoading: LiveData<Boolean>
        get() = _isLoading


    /* return address
     -> with input value type int return address by zip code
     -> with input value type string return address by street name
     */
    fun getAddress() {
        viewModelScope.launch {
            Log.i("value: ", value)
            if (isValidForm(value)) {
                if (value.isDigitsOnly()) {
                    getAddressByZipCode()

                } else {
                    getAddressByStreet()

                }

            }

        }

    }

    //get address by zip code from repository
    private fun getAddressByZipCode() {
        _isLoading.value = true

        viewModelScope.launch {

            when (val result = repository.getAddressByZipCode(value)) {
                is AppResult.Success -> {
                    if (result.address.toString().contains("null")) {
                        Log.i("r:", "nao encontrado")
                        _searchStateEvent.value = SearchState.SearchError(CEP_NOT_FOUND)

                    } else {
                        Log.i("r:", " encontrado")
                        _searchStateEvent.value =
                                SearchState.SearchZipCodeSuccess(result.address)

                    }
                }
                is AppResult.ApiError -> {
                    if (result.statusCode == 404) {
                        _searchStateEvent.value = SearchState.SearchError(BAD_REQUEST)
                    }
                }
                else -> {
                    _searchStateEvent.value = SearchState.SearchError(INTERNAL_ERROR)
                }
            }
            _isLoading.value = false
        }

    }

    //get address by street name from repository
    private fun getAddressByStreet() {
        _isLoading.value = true

        viewModelScope.launch {
            Log.i("street: ", value)

            when (val result = repository.getAddressByStreet(value)) {
                is AppResult.Success -> {
                    if (result.address.size > 1) {
                        _searchStateEvent.value =
                                SearchState.SearchStreetSuccess(result.address)
                    } else {
                        _searchStateEvent.value = SearchState.NotFound(CEP_NOT_FOUND)

                    }
                }

                is AppResult.ApiError -> {
                    Log.i("error:", result.statusCode.toString())
                    if (result.statusCode == 400) {
                        _searchStateEvent.value = SearchState.SearchError(BAD_REQUEST)
                    }
                }
                is AppResult.ServerError -> {
                    Log.i("error:", result.toString())

                    _searchStateEvent.value = SearchState.SearchError(INTERNAL_ERROR)
                }
                is AppResult.NotFound -> {
                    _searchStateEvent.value = SearchState.NotFound(CEP_NOT_FOUND)
                }
            }

            _isLoading.value = false
        }


    }

    //get input zip code from view
    fun onUserChangedInputValue(value: String) {
        this.value = value.trim()
    }

    private fun isValidForm(value: String): Boolean {
        val invalidFields = arrayListOf<Pair<String, Int>>()
        Log.i("string", value)
        if (value.isEmpty()) {
            invalidFields.add(INPUT_EMPTY)
        } else {
            if (value.isDigitsOnly()) {
                if (value.length > 8 || value.length < 8) {
                    invalidFields.add(INCORRET_SIZE_ZIP_CODE)
                }
            } else {
                if (value.length < 4) {
                    invalidFields.add(INCORRET_SIZE_STREET)
                }

            }
        }


        if (invalidFields.isNotEmpty()) {
            _searchStateEvent.value = SearchState.InvalidationFields(invalidFields)
            return false
        }
        return true
    }

    companion object {

        val INPUT_EMPTY = "INPUT_EMPTY" to R.string.input_empty
        val INTERNAL_ERROR = "INTERNAL_ERROR" to R.string.internal_error
        val BAD_REQUEST = "BAD_REQUEST" to R.string.bad_request
        val CEP_NOT_FOUND = "ZIP_CODE_NOT_FOUND" to R.string.cep_not_found
        val INCORRET_SIZE_ZIP_CODE = "INCORRET_SIZE_ZIP_CODE" to R.string.incorrect_size_zip_code
        val INCORRET_SIZE_STREET = "INCORRET_SIZE_STREET" to R.string.incorrect_size_street

    }
}
package br.com.emdev.addressfinder.di

import br.com.emdev.addressfinder.presentation.search.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val searchModelModule = module {

    // Specific viewModel pattern to tell Koin how to build CountriesViewModel
    viewModel {
        SearchViewModel(repository = get(), context = get())
    }

}
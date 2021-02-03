package br.com.emdev.addressfinder.di

import android.content.Context
import br.com.emdev.addressfinder.data.ApiService
import br.com.emdev.addressfinder.repository.AddressRepository
import br.com.emdev.addressfinder.repository.AddressRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {

    fun provideCountryRepository(api: ApiService, context: Context): AddressRepository {
        return AddressRepositoryImpl(api, context)
    }
    single { provideCountryRepository(get(), androidContext()) }
    }
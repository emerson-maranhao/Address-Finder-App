package br.com.emdev.addressfinder.utils

import android.app.Application
import br.com.emdev.addressfinder.di.appModule
import br.com.emdev.addressfinder.di.repositoryModule
import br.com.emdev.addressfinder.di.searchModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App:Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            androidLogger()
            modules(
                listOf(
                    appModule,
                    repositoryModule,
                    searchModelModule,
                )
            )
        }
    }
}
package br.com.emdev.addressfinder.di

import br.com.emdev.addressfinder.data.ApiHelper
import br.com.emdev.addressfinder.data.ApiHelperImpl
import br.com.emdev.addressfinder.data.ApiService
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    single { provideRetrofit("https://viacep.com.br/ws/") }
    single { provideApiService(get()) }
    single<ApiHelper> {
        return@single ApiHelperImpl(get())
    }
}


private fun provideRetrofit(
    BASE_URL: String
): Retrofit =
    Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()


private fun provideApiService(retrofit: Retrofit): ApiService {
    return retrofit.create(ApiService::class.java)

}



package br.com.emdev.addressfinder.repository

import android.content.Context
import android.util.Log
import br.com.emdev.addressfinder.data.ApiService
import br.com.emdev.addressfinder.data.response.AddressResponse
import br.com.emdev.addressfinder.utils.AppResult

class AddressRepositoryImpl(
    private val api: ApiService,
    private val context: Context
) : AddressRepository {

    override suspend fun getAddressByZipCode(zipCode: String): AppResult<AddressResponse> {
        return try {
            val response = api.getAddressByZipCode(zipCode)
            if (response.isSuccessful) {

                return AppResult.Success(response.body()!!)

            } else {
                Log.i("ApiError:", response.message())
                AppResult.ApiError(response.code())

            }
        } catch (err: Exception) {
            Log.i("SeverError:",err.message.toString())

            AppResult.ServerError
        }

    }

    override suspend fun getAddressByStreet(street: String): AppResult<List<AddressResponse>> {
        return try {
            val response = api.getAddressByStreet(street)
            if (response.isSuccessful) {

                return AppResult.Success(response.body()!!)

            } else {
                AppResult.ApiError(response.code())

            }
        } catch (err: Exception) {
            AppResult.ServerError
        }
    }

}
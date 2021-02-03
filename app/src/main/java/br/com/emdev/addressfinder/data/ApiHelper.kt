package br.com.emdev.addressfinder.data

import br.com.emdev.addressfinder.data.response.AddressResponse
import retrofit2.Response

interface ApiHelper {
    suspend fun getAddressByZipCode(zipCode: String): Response<AddressResponse>
    suspend fun getAddressByStreet(street: String): Response<List<AddressResponse>>
}
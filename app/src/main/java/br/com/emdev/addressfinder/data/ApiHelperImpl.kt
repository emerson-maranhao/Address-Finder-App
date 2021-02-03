package br.com.emdev.addressfinder.data

import br.com.emdev.addressfinder.data.response.AddressResponse
import retrofit2.Response

class ApiHelperImpl(private val apiService: ApiService) : ApiHelper {
    override suspend fun getAddressByZipCode(zipCode: String): Response<AddressResponse> {
        return apiService.getAddressByZipCode(zipCode)

    }

    override suspend fun getAddressByStreet(street: String): Response<List<AddressResponse>> {
       return apiService.getAddressByStreet(street)
    }
}
package br.com.emdev.addressfinder.data

import br.com.emdev.addressfinder.data.response.AddressResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("{zipCode}/json")
    suspend fun getAddressByZipCode(
        @Path("zipCode") zipCode: String
    ): Response<AddressResponse>

    @GET("{state}/{city}/{street}/json")
    suspend fun getAddressByStreet(
        @Path("street") street: String,
        @Path("state") state: String = "AM",
        @Path("city") city: String = "Manaus"
    ): Response<List<AddressResponse>>
}

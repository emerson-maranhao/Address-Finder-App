package br.com.emdev.addressfinder.repository

import br.com.emdev.addressfinder.data.response.AddressResponse
import br.com.emdev.addressfinder.utils.AppResult

interface AddressRepository {
    suspend fun getAddressByZipCode(zipCode: String): AppResult<AddressResponse>
    suspend fun getAddressByStreet(street: String):AppResult<List<AddressResponse>>
}
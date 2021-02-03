package br.com.emdev.addressfinder.utils

sealed class AppResult<out T> {
    class Success<out T>(val address: T) : AppResult<T>()
    class ApiError(val statusCode: Int) : AppResult<Nothing>()
    class NotFound(val message: String) : AppResult<Nothing>()
    object ServerError : AppResult<Nothing>()
}
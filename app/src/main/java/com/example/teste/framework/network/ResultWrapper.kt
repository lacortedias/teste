package com.example.teste.framework.network

sealed class ResultWrapper<out T> {
    data class Success<out T>(val value: T): ResultWrapper<T>()
    data class Error(val message: String = ""): ResultWrapper<Nothing>()
}

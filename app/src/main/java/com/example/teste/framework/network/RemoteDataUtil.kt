package com.example.teste.framework.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.UnknownHostException

suspend inline fun <reified T> call(crossinline apiCall: suspend () -> T): ResultWrapper<T> {

    val errorTag = "ERROR_API"

    return withContext(Dispatchers.IO) {
        try {
            val result = apiCall.invoke()
            ResultWrapper.Success(result)
        } catch (throwable: Throwable) {
            ResultWrapper.Error(message = errorTag)
        } catch (unknownHostException: UnknownHostException) {
            ResultWrapper.Error(message = errorTag)
        } catch (exception: Exception) {
            ResultWrapper.Error(message = errorTag)
        }
    }
}
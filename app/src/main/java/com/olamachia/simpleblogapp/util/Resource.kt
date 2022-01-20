package com.olamachia.simpleblogapp.util

// Generic Resource Class to handle network errors
 sealed class Resource<out T>(val data: T? = null) {

    class Success<out T>(data: T): Resource<T>(data)

    data class Error(val exception: Exception): Resource<Nothing>()

    object Loading: Resource<Nothing>()


}
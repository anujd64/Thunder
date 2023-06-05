package com.theflexproject.thunder.fragments.homeNew

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null,
    val status: Status
) {

    class Success<T>(data: T) : Resource<T>(data = data, status = Status.SUCCESS)

    class Error<T>(
        errorMessage: String,
        data:T?=null
    ) : Resource<T>(message = errorMessage, status = Status.ERROR, data = data)

    class Loading<T> : Resource<T>(status = Status.LOADING)
}

enum class Status {
    SUCCESS,
    ERROR,
    LOADING
}

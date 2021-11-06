package com.abrahammontes.prueba.data

sealed class Result<out R> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Throwable) : Result<Nothing>()

    private var hasBeenHandled = false

    /**
     * Returns the content and prevents its use again.
     */
    fun getResultIfNotHandled(): Result<R>? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            this
        }
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    fun peekResult(): Result<R> = this
}
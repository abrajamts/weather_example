package com.abrahammontes.prueba.common

open class Event<out T> (private val content : T? = null, val status: Status = Status.SUCCESS, val message: String? = null) {
    private var hasBeenHandled = false
        private set // Allow external read but not write

    /**
     * Returns the content and prevents its use again.
     */
    fun getEventIfNotHandled(): Event<T>? {
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
    fun peekContent(): T? = content

    fun peekEvent(): Event<T> = this

    enum class Status {
        LOADING, SUCCESS, ERROR
    }

    companion object {

        fun <T> success(data: T) = Event(status = Status.SUCCESS, content = data)

        fun <T> error(message: String?) = Event<T>(status = Status.ERROR, message = message)

        fun <T> loading() = Event<T>(status = Status.LOADING)

    }
}
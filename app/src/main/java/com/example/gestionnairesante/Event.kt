package com.example.gestionnairesante

open class Event<out T>(private val content: T) {
    var hasBeenHandle = false
        private set

    fun getContentIfNotHandle(): T? {
        return if (hasBeenHandle) {
            null
        } else {
            hasBeenHandle = true
            content
        }
    }

    fun peekContent(): T = content
}
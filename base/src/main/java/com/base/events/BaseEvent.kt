package com.base.events

class BaseEvent<out T>(private val data: T?) {

    private var hasBeenHandled = false

    fun getEventIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            data
        }
    }
}
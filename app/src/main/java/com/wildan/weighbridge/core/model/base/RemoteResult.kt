package com.wildan.weighbridge.core.model.base

sealed class RemoteResult<out R> {
    data class OnSuccess<out R>(val data: R) : RemoteResult<R>()
    data class OnError<out R>(val message: String? = "", val data: R? = null) : RemoteResult<R>()
}
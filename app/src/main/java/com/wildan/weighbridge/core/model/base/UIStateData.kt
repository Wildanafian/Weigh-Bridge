package com.wildan.weighbridge.core.model.base

data class UIStateData<out T>(
    val data: T? = null,
    val loading: Boolean? = null,
    val message: String? = null,
)
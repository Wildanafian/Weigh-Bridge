package com.wildan.weighbridge.core.ui.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wildan.weighbridge.core.model.base.UIStateData

/**
 * Created by Wildan Nafian on 05/08/2022.
 * Github https://github.com/Wildanafian
 * wildanafian8@gmail.com
 */
open class BaseViewModel : ViewModel() {

    @JvmName("update")
    fun <T> MutableLiveData<UIStateData<T>>.update(
        data: T? = null,
        loading: Boolean? = false,
        message: String? = null,
    ) {
        value = UIStateData(data, loading, message)
    }

}

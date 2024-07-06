package com.wildan.weighbridge.core.ui.helper

import android.content.Context
import android.text.TextWatcher
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wildan.weighbridge.R
import com.wildan.weighbridge.core.common.ProjectConstant

/**
 * Created by Wildan Nafian on 12/01/2022.
 * Github https://github.com/Wildanafian
 * wildanafian8@gmail.com
 */

/**
 * Initialize RecycleView
 * yourRecycleView.initRecycleView(your adapter)
 */
fun <RV : RecyclerView.ViewHolder?> RecyclerView.initRecycleView(
    adapterRV: RecyclerView.Adapter<RV>,
    isVertical: Boolean = true,
    isReverse: Boolean = false,
    fixedSize: Boolean = false,
    cacheRv: Boolean = false,
    customLinearLayoutManager: LinearLayoutManager? = null,
) =
    this.apply {
        layoutManager = customLinearLayoutManager ?: LinearLayoutManager(
            this.context,
            if (isVertical) LinearLayoutManager.VERTICAL else LinearLayoutManager.HORIZONTAL,
            isReverse
        )
        adapter = adapterRV
        setHasFixedSize(fixedSize)
        if (cacheRv) setItemViewCacheSize(25)

    }

typealias Inflate<T> = (LayoutInflater) -> T

fun EditText.getTexts() = this.text.toString()

fun EditText.getWeight() = this.getTexts().ifEmpty { "0" }.toDouble()

fun EditText.isNotEmpty() = this.text.toString().isNotEmpty()

fun EditText.listener(callback: ((Int) -> Unit)? = null) : TextWatcher {
    return addTextChangedListener(onTextChanged = { count, _, _, _ ->
        callback?.invoke(count?.length ?: 0)
    })
}

fun EditText.isLicenseNumberValid(): Boolean {
    return ProjectConstant.LICENSE_NUMBER.toRegex().matches(this.getTexts())
}

fun Double.isWeightInRange(inbound: Double): Boolean {
    val range = 0.001..(inbound - 0.001)
    return range.contains(this)
}

fun Button.setIsActive(status: Boolean) {
    if (status) this.setBackgroundAndText(this.context, R.drawable.bg_primary_r30, R.color.white)
    else this.setBackgroundAndText(this.context, R.drawable.bg_gray_r30, R.color.white)
    this.isEnabled = status
}

fun Button.setBackgroundAndText(
    context: Context,
    @DrawableRes drawable: Int,
    @ColorRes color: Int,
) {
    this.setBackgroundResource(drawable)
    this.setTextColor(ContextCompat.getColor(context, color))
}

fun TextView.textColorAndBackground(textColor: Int, background: Int) {
    setTextColor(ContextCompat.getColor(context, textColor))
    setBackgroundResource(background)
}
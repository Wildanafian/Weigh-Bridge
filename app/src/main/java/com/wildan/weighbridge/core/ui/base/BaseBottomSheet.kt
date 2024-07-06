package com.wildan.weighbridge.core.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.wildan.weighbridge.R
import com.wildan.weighbridge.core.ui.helper.Inflate

/**
 * Created by Wildan Nafian on 20/05/2024.
 * Github https://github.com/Wildanafian
 * wildanafian8@gmail.com
 */
open class BaseBottomSheet<out VB : ViewBinding>(private val inflate: Inflate<VB>) : BottomSheetDialogFragment() {

    private var _binding: VB? = null
    protected val bind get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogTheme)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = inflate(inflater)
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListener()
        initObserver()
    }

    open fun initView() = Unit

    open fun initListener() = Unit

    open fun initObserver() = Unit

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    protected fun String.showToast() = Toast.makeText(requireActivity(), this, Toast.LENGTH_SHORT).show()

}

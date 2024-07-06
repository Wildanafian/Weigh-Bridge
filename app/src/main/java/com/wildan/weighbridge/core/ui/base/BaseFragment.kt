package com.wildan.weighbridge.core.ui.base

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.wildan.weighbridge.R
import com.wildan.weighbridge.core.ui.base.common.CommonFunction
import com.wildan.weighbridge.core.ui.helper.Inflate

/**
 * Created by Wildan Nafian on 12/01/2022.
 * Github https://github.com/Wildanafian
 * wildanafian8@gmail.com
 */

@SuppressLint("InflateParams")
abstract class BaseFragment<out VB : ViewBinding>(private val inflate: Inflate<VB>) : Fragment(), CommonFunction {

    private var _binding: VB? = null
    protected val bind get() = _binding!!

    private val loadingDialog: Dialog by lazy {
        Dialog(requireContext()).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            val view = LayoutInflater.from(requireContext()).inflate(R.layout.custom_loading, null)
            setContentView(view)
            window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window!!.attributes = WindowManager.LayoutParams().apply {
                copyFrom(window!!.attributes)
                width = WindowManager.LayoutParams.WRAP_CONTENT
                height = WindowManager.LayoutParams.WRAP_CONTENT
                gravity = Gravity.CENTER
            }
            setCancelable(false)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = inflate(inflater)
        initView()
        initListener()
        initObserver()
        return bind.root
    }

    override fun onDestroyView() {
        false.showLoadingDialog()
        loadingDialog.dismiss()
        _binding = null
        super.onDestroyView()
    }

    override fun initView() = Unit

    override fun initListener() = Unit

    override fun initObserver() = Unit

    protected fun Boolean?.showLoadingDialog() {
        if (this == true) loadingDialog.show() else loadingDialog.dismiss()
    }

    protected fun String?.showToast() = Toast.makeText(requireActivity(), this, Toast.LENGTH_SHORT).show()

}

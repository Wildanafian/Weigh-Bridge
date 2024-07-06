package com.wildan.weighbridge.core.ui.base

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.wildan.weighbridge.core.ui.base.common.CommonFunction
import com.wildan.weighbridge.core.ui.helper.Inflate

/**
 * Created by Wildan Nafian on 12/01/2022.
 * Github https://github.com/Wildanafian
 * wildanafian8@gmail.com
 */

@SuppressLint("InflateParams")
abstract class BaseActivity<out VB : ViewBinding>(private val inflate: Inflate<VB>) : AppCompatActivity(),
                                                                                      CommonFunction {

    private var _binding: VB? = null
    protected val bind get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = inflate(layoutInflater)
        setContentView(requireNotNull(_binding).root)
        initView()
        initListener()
        initObserver()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun initView() = Unit

    override fun initListener() = Unit

    override fun initObserver() = Unit

}

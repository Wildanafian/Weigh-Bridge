package com.wildan.weighbridge.ui.feature.add

import com.wildan.weighbridge.R
import com.wildan.weighbridge.core.ui.helper.goBack
import com.wildan.weighbridge.core.ui.helper.observe
import com.wildan.weighbridge.core.ui.helper.onTextChanged
import dagger.hilt.android.AndroidEntryPoint

/*
 * Created by Wildan Nafian on 7/4/24.
 * Github https://github.com/Wildanafian
 * wildanafian8@gmail.com
 */

@AndroidEntryPoint
class AddTicketFragment : BaseTicketManagementFragment() {

    override fun initView() {
        checkMandatoryFieldIsNotEmpty()
        bind.etNetWeight.isEnabled = false
    }

    override fun initListener() {
        super.initListener()
        bind.etInboundWeight.onTextChanged {
            checkMandatoryFieldIsNotEmpty()
            calculateNetWeight()
        }

        bind.etOutboundWeight.onTextChanged {
            checkMandatoryFieldIsNotEmpty()
            calculateNetWeight()
        }

        bind.btnSave.setOnClickListener {
            vm.submitNewTicket(createTicketData())
        }
    }

    override fun initObserver() {
        observe(vm.addNewTicket) { data ->
            data?.data?.let {
                getString(R.string.ticket_saved_successfully).showToast()
                goBack()
            }
            data?.message?.let { it.showToast() }
            data?.loading?.let { it.showLoadingDialog() }
        }
    }

}

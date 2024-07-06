package com.wildan.weighbridge.ui.feature.edit

import androidx.navigation.fragment.navArgs
import com.wildan.weighbridge.R
import com.wildan.weighbridge.core.ui.helper.getTexts
import com.wildan.weighbridge.core.ui.helper.getWeight
import com.wildan.weighbridge.core.ui.helper.goBack
import com.wildan.weighbridge.core.ui.helper.listener
import com.wildan.weighbridge.core.ui.helper.observe
import com.wildan.weighbridge.ui.feature.add.BaseTicketManagementFragment
import dagger.hilt.android.AndroidEntryPoint

/*
 * Created by Wildan Nafian on 7/4/24.
 * Github https://github.com/Wildanafian
 * wildanafian8@gmail.com
 */

@AndroidEntryPoint
class EditTicketFragment : BaseTicketManagementFragment() {

    private val args: EditTicketFragmentArgs by navArgs()
    override fun initView() {
        bind.tvToolbarTitle.text = requireContext().getString(R.string.edit_ticket)

        with(args.ticketData) {
            bind.etDriverName.setText(driverName)
            bind.etLicense.setText(licenseNumber)
            bind.etDate.text = date
            bind.etTime.text = time
            bind.etInboundWeight.setText(inboundWeight.toString())
            bind.etOutboundWeight.setText(outboundWeight.toString())
            bind.etNetWeight.setText(netWeight.toString())
        }

        checkMandatoryFieldIsNotEmpty()
    }

    override fun initListener() {
        super.initListener()
        bind.etInboundWeight.listener {
            checkMandatoryFieldIsNotEmpty()
            calculateNetWeight()

        }

        bind.etOutboundWeight.listener {
            checkMandatoryFieldIsNotEmpty()
            calculateNetWeight()
        }

        bind.btnSave.setOnClickListener {
            vm.submitEditedTicket(createEditedTicketData())
        }
    }

    override fun initObserver() {
        observe(vm.editTicket) { data ->
            data?.data?.let {
                getString(R.string.ticket_changed_successfully).showToast()
                goBack()
            }
            data?.message?.let { it.showToast() }
            data?.loading?.let { it.showLoadingDialog() }
        }
    }

    private fun createEditedTicketData() = args.ticketData.copy(
        date = bind.etDate.text.toString(),
        time = bind.etTime.text.toString(),
        licenseNumber = bind.etLicense.getTexts(),
        driverName = bind.etDriverName.getTexts(),
        inboundWeight = bind.etInboundWeight.getWeight(),
        outboundWeight = bind.etOutboundWeight.getWeight(),
        netWeight = bind.etNetWeight.getWeight()
    )

}

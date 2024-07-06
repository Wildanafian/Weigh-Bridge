package com.wildan.weighbridge.ui

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import androidx.fragment.app.viewModels
import com.google.android.material.datepicker.MaterialDatePicker
import com.wildan.weighbridge.R
import com.wildan.weighbridge.core.common.ProjectConstant.DD_MM_YYYY
import com.wildan.weighbridge.core.common.ProjectConstant.HH_MM
import com.wildan.weighbridge.core.model.TicketItem
import com.wildan.weighbridge.core.ui.base.BaseFragment
import com.wildan.weighbridge.core.ui.helper.getTexts
import com.wildan.weighbridge.core.ui.helper.getWeight
import com.wildan.weighbridge.core.ui.helper.goBack
import com.wildan.weighbridge.core.ui.helper.isLicenseNumberValid
import com.wildan.weighbridge.core.ui.helper.isNotEmpty
import com.wildan.weighbridge.core.ui.helper.listener
import com.wildan.weighbridge.core.ui.helper.observe
import com.wildan.weighbridge.core.ui.helper.setIsActive
import com.wildan.weighbridge.core.ui.helper.setToolbarBackNavigationAction
import com.wildan.weighbridge.databinding.FragmentAddTicketBinding
import com.wildan.weighbridge.ui.viewmodel.TicketManagementViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/*
 * Created by Wildan Nafian on 7/4/24.
 * Github https://github.com/Wildanafian
 * wildanafian8@gmail.com
 */

@AndroidEntryPoint
class BackupAddTicketFragment : BaseFragment<FragmentAddTicketBinding>(FragmentAddTicketBinding::inflate) {

    private val vm by viewModels<TicketManagementViewModel>()
    private val datePicker by lazy {
        MaterialDatePicker.Builder.datePicker()
            .setTheme(R.style.ThemeOverlay_App_DatePicker)
            .setTitleText(getString(R.string.select_inbound_date))
            .build()
    }

    override fun initView() {
        checkMandatoryFieldIsNotEmpty()
    }

    @SuppressLint("SimpleDateFormat")
    override fun initListener() {
        bind.toolbar.setToolbarBackNavigationAction(this)

        bind.etDate.setOnClickListener {
            datePicker.show(childFragmentManager, "datePicker")
        }

        bind.etTime.setOnClickListener {
            showTimePicker()
        }

        bind.etLicense.listener {
            checkMandatoryFieldIsNotEmpty()
            if (!bind.etLicense.isLicenseNumberValid() && it>5) getString(R.string.invalid_license_number).showToast()
        }

        bind.etDriverName.listener {
            checkMandatoryFieldIsNotEmpty()
        }

        bind.etInboundWeight.listener {
            checkMandatoryFieldIsNotEmpty()
        }

        bind.etOutboundWeight.listener {
            checkMandatoryFieldIsNotEmpty()
//            if (bind.etOutboundWeight.isOutboundWeightInRange(bind.etInboundWeight)) {
//                val netWeight = bind.etInboundWeight.getWeight() - bind.etOutboundWeight.getWeight()
//                bind.etNetWeight.setText(netWeight.toString())
//            } else {
//                bind.etNetWeight.setText(getString(R.string.zero))
//            }
        }

        bind.btnSave.setOnClickListener {
            vm.submitNewTicket(createTicketData())
        }

        datePicker.addOnPositiveButtonClickListener {
            val format = SimpleDateFormat(DD_MM_YYYY)
            bind.etDate.text = format.format(Date(it))
            checkMandatoryFieldIsNotEmpty()
        }

        datePicker.addOnCancelListener {
            checkMandatoryFieldIsNotEmpty()
        }
    }

    override fun initObserver() {
        observe(vm.addNewTicket) {
            getString(R.string.ticket_saved_successfully).showToast()
            goBack()
        }
    }

    private fun createTicketData() = TicketItem(
        date = bind.etDate.text.toString(),
        time = bind.etTime.text.toString(),
        licenseNumber = bind.etLicense.getTexts(),
        driverName = bind.etDriverName.getTexts(),
        inboundWeight = bind.etInboundWeight.getWeight(),
        outboundWeight = bind.etOutboundWeight.getWeight(),
        netWeight = bind.etNetWeight.getWeight()
    )

    private fun checkMandatoryFieldIsNotEmpty() {
        val isInBoundBiggerThanOutBoundWeight = bind.etInboundWeight.getWeight() > bind.etOutboundWeight.getWeight()
        val isLicenseNumberValid = bind.etLicense.isLicenseNumberValid()
        val state = bind.etDate.text.isNotEmpty() && bind.etLicense.isNotEmpty() && bind.etTime.text.isNotEmpty()
                && bind.etDriverName.isNotEmpty() && bind.etInboundWeight.isNotEmpty() && isLicenseNumberValid
                && isInBoundBiggerThanOutBoundWeight
        bind.btnSave.setIsActive(state)
    }

    private fun showTimePicker() {
        val calendar = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minute)
            bind.etTime.text = SimpleDateFormat(HH_MM, Locale.getDefault()).format(calendar.time)
        }
        TimePickerDialog(
            requireContext(),
            timeSetListener,
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        ).show()
    }

}

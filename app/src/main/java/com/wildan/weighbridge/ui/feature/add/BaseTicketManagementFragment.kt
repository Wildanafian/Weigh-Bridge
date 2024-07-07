package com.wildan.weighbridge.ui.feature.add

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
import com.wildan.weighbridge.core.ui.helper.isLicenseNumberValid
import com.wildan.weighbridge.core.ui.helper.isNotEmpty
import com.wildan.weighbridge.core.ui.helper.isWeightInRange
import com.wildan.weighbridge.core.ui.helper.onTextChanged
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
open class BaseTicketManagementFragment : BaseFragment<FragmentAddTicketBinding>(FragmentAddTicketBinding::inflate) {

    companion object {

        const val MIN_5_CHAR = 5
    }

    protected val vm by viewModels<TicketManagementViewModel>()
    private val datePicker by lazy {
        MaterialDatePicker.Builder.datePicker()
            .setTheme(R.style.ThemeOverlay_App_DatePicker)
            .setTitleText(getString(R.string.select_inbound_date))
            .build()
    }
    private lateinit var timePicker: TimePickerDialog

    @SuppressLint("SimpleDateFormat")
    override fun initListener() {
        bind.toolbar.setToolbarBackNavigationAction(this)

        bind.etDate.setOnClickListener {
            if(datePicker.isAdded) return@setOnClickListener
            datePicker.show(childFragmentManager, getString(R.string.datepicker))
        }

        bind.etTime.setOnClickListener {
            if(!::timePicker.isInitialized) initTimePicker()
            else timePicker.show()
        }

        bind.etLicense.onTextChanged {
            checkMandatoryFieldIsNotEmpty()
            bind.tilLicense.error = if (!bind.etLicense.isLicenseNumberValid() && it > MIN_5_CHAR) {
                getString(R.string.invalid_license_number)
            } else {
                ""
            }
        }

        bind.etDriverName.onTextChanged {
            checkMandatoryFieldIsNotEmpty()
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

    protected fun createTicketData() = TicketItem(
        date = bind.etDate.text.toString(),
        time = bind.etTime.text.toString(),
        licenseNumber = bind.etLicense.getTexts(),
        driverName = bind.etDriverName.getTexts(),
        inboundWeight = bind.etInboundWeight.getWeight(),
        outboundWeight = bind.etOutboundWeight.getWeight(),
        netWeight = bind.etNetWeight.getWeight()
    )

    protected fun checkMandatoryFieldIsNotEmpty() {
        val isInBoundBiggerThanOutBoundWeight = bind.etInboundWeight.getWeight() > bind.etOutboundWeight.getWeight()
        val isLicenseNumberValid = bind.etLicense.isLicenseNumberValid()
        val state = bind.etDate.text.isNotEmpty() && bind.etLicense.isNotEmpty() && bind.etTime.text.isNotEmpty()
                && bind.etDriverName.isNotEmpty() && bind.etInboundWeight.isNotEmpty() && isLicenseNumberValid
                && isInBoundBiggerThanOutBoundWeight
        bind.btnSave.setIsActive(state)
    }

    private fun initTimePicker() {
        val calendar = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
            calendar[Calendar.HOUR_OF_DAY] = hour
            calendar[Calendar.MINUTE] = minute
            bind.etTime.text = SimpleDateFormat(HH_MM, Locale.getDefault()).format(calendar.time)
        }
        timePicker = TimePickerDialog(
            requireContext(),
            timeSetListener,
            calendar[Calendar.HOUR_OF_DAY],
            calendar[Calendar.MINUTE],
            true
        )
    }

    protected fun calculateNetWeight() {
        if (bind.etOutboundWeight.getWeight().isWeightInRange(bind.etInboundWeight.getWeight())) {
            val netWeight = bind.etInboundWeight.getWeight() - bind.etOutboundWeight.getWeight()
            bind.etNetWeight.setText(netWeight.toString())
            bind.tilOutbound.error = ""
        } else {
            bind.etNetWeight.setText(getString(R.string.zero))
            bind.tilOutbound.error = if (bind.etOutboundWeight.getWeight() > bind.etInboundWeight.getWeight()) {
                getString(R.string.outbound_weight_is_bigger_than_inbound_weight)
            } else {
                getString(R.string.outbound_weight_is_below_inbound_weight)
            }
        }
    }
}

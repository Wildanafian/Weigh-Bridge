package com.wildan.weighbridge.ui.sheet

import android.os.Build
import com.wildan.weighbridge.R
import com.wildan.weighbridge.core.common.ProjectConstant.DATA
import com.wildan.weighbridge.core.model.TicketItem
import com.wildan.weighbridge.core.ui.base.BaseBottomSheet
import com.wildan.weighbridge.databinding.SheetDetailTicketBinding

/*
 * Created by Wildan Nafian on 7/4/24.
 * Github https://github.com/Wildanafian
 * wildanafian8@gmail.com
 */

class TicketDetailBottomSheet : BaseBottomSheet<SheetDetailTicketBinding>(SheetDetailTicketBinding::inflate) {

    private var data: TicketItem? = TicketItem()
    var onTapEdit: ((TicketItem) -> Unit)? = null
    var onTapDelete: ((String) -> Unit)? = null

    @Suppress("DEPRECATION")
    override fun initView() {
        data = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(DATA, TicketItem::class.java) ?: TicketItem()
        } else {
            arguments?.getParcelable(DATA) ?: TicketItem()
        }
        with(data) {
            bind.tvDriverName.text = this?.driverName
            bind.tvLicenseNumber.text = this?.licenseNumber
            bind.tvDateTime.text = getString(R.string.set_date_and_time, this?.date, this?.time)
            bind.tvInboundWeight.text = getString(R.string.set_ton, this?.inboundWeight.toString())
            bind.tvOutboundWeight.text = getString(R.string.set_ton, this?.outboundWeight.toString())
            bind.tvNetWeight.text = getString(R.string.set_ton, this?.netWeight.toString())
        }
    }

    override fun initListener() {
        bind.btnEdit.setOnClickListener {
            data?.let { item -> onTapEdit?.invoke(item) }
            dismiss()
        }

        bind.btnDelete.setOnClickListener {
            data?.let { item -> onTapDelete?.invoke(item.id) }
            dismiss()
        }
    }

}

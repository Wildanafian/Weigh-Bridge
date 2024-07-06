package com.wildan.weighbridge.ui.sheet

import androidx.fragment.app.activityViewModels
import com.wildan.weighbridge.R
import com.wildan.weighbridge.core.model.SortFilterOption
import com.wildan.weighbridge.core.ui.base.BaseBottomSheet
import com.wildan.weighbridge.core.ui.helper.getTexts
import com.wildan.weighbridge.core.ui.helper.isNotEmpty
import com.wildan.weighbridge.databinding.SheetFilterBinding
import com.wildan.weighbridge.ui.viewmodel.TicketViewModel

/*
 * Created by Wildan Nafian on 7/4/24.
 * Github https://github.com/Wildanafian
 * wildanafian8@gmail.com
 */

class FilterBottomSheet : BaseBottomSheet<SheetFilterBinding>(SheetFilterBinding::inflate) {

    private val vm by activityViewModels<TicketViewModel>()
    private var filter: SortFilterOption = SortFilterOption.NO_SORT_FILTER

    override fun initListener() {
        bind.chipGroupFilter.setOnCheckedStateChangeListener { group, _ ->
            filter = when (group.checkedChipId) {
                R.id.chip_weight_date    -> SortFilterOption.DATE
                R.id.chip_driver_name    -> SortFilterOption.DRIVER_NAME
                R.id.chip_license_number -> SortFilterOption.LICENSE_NUMBER
                else -> SortFilterOption.NO_SORT_FILTER
            }
        }

        bind.btnClose.setOnClickListener {
            if (bind.etInput.isNotEmpty()) {
                vm.getFilteredList(filter, bind.etInput.getTexts())
                dismiss()
            } else {
                getString(R.string.keyword_cannot_be_empty).showToast()
            }
        }
    }

}

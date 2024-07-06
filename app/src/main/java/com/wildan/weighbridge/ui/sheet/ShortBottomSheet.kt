package com.wildan.weighbridge.ui.sheet

import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.activityViewModels
import com.wildan.weighbridge.core.model.SortFilterOption
import com.wildan.weighbridge.core.ui.base.BaseBottomSheet
import com.wildan.weighbridge.databinding.SheetShortBinding
import com.wildan.weighbridge.ui.viewmodel.TicketViewModel

/*
 * Created by Wildan Nafian on 7/4/24.
 * Github https://github.com/Wildanafian
 * wildanafian8@gmail.com
 */

class ShortBottomSheet : BaseBottomSheet<SheetShortBinding>(SheetShortBinding::inflate) {

    private val vm by activityViewModels<TicketViewModel>()
    private var selectedOption = SortFilterOption.NO_SORT_FILTER

    override fun initListener() {
        bind.radiogroupShort.setOnCheckedChangeListener { group, _ ->
            if (selectedOption != group.getSelectedRadio()) {
                selectedOption = group.getSelectedRadio()
                vm.getSortedList(selectedOption)
                dismiss()
            }
        }

        bind.btnClose.setOnClickListener {
            dismiss()
        }
    }

    private fun RadioGroup.getSelectedRadio(): SortFilterOption {
        val radioButton = bind.root.findViewById<RadioButton>(checkedRadioButtonId)
        val selected = radioButton.text.toString().lowercase()
        return when {
            selected.contains("date", true) -> SortFilterOption.DATE
            selected.contains("name", true) -> SortFilterOption.DRIVER_NAME
            else                            -> SortFilterOption.LICENSE_NUMBER
        }
    }

}

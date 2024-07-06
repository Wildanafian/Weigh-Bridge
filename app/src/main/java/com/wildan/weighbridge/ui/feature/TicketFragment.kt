package com.wildan.weighbridge.ui.feature

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.wildan.weighbridge.R
import com.wildan.weighbridge.core.common.ProjectConstant.DATA
import com.wildan.weighbridge.core.ui.base.BaseFragment
import com.wildan.weighbridge.core.ui.helper.gooTo
import com.wildan.weighbridge.core.ui.helper.initRecycleView
import com.wildan.weighbridge.core.ui.helper.observe
import com.wildan.weighbridge.databinding.FragmentTicketBinding
import com.wildan.weighbridge.ui.adapter.TicketListAdapter
import com.wildan.weighbridge.ui.sheet.FilterBottomSheet
import com.wildan.weighbridge.ui.sheet.ShortBottomSheet
import com.wildan.weighbridge.ui.sheet.TicketDetailBottomSheet
import com.wildan.weighbridge.ui.viewmodel.TicketManagementViewModel
import com.wildan.weighbridge.ui.viewmodel.TicketViewModel
import dagger.hilt.android.AndroidEntryPoint

/*
 * Created by Wildan Nafian on 7/4/24.
 * Github https://github.com/Wildanafian
 * wildanafian8@gmail.com
 */

@AndroidEntryPoint
class TicketFragment : BaseFragment<FragmentTicketBinding>(FragmentTicketBinding::inflate) {

    private val vm by activityViewModels<TicketViewModel>()
    private val vmManagementTicket by viewModels<TicketManagementViewModel>()
    private val adapterTicket by lazy { TicketListAdapter(this) }
    private val ticketDetailSheet by lazy { TicketDetailBottomSheet() }
    private val filterSheet by lazy { FilterBottomSheet() }
    private val shortSheet by lazy { ShortBottomSheet() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        vm.getTicketList()
        bind.rvTicket.initRecycleView(adapterRV = adapterTicket)
    }

    override fun initListener() {
        bind.btnCreateNewTicket.setOnClickListener {
            gooTo(TicketFragmentDirections.toAddTicket())
        }

        bind.btnFilter.setOnClickListener {
            filterSheet.show(childFragmentManager, getString(R.string.filtersheet))
        }

        bind.btnShorting.setOnClickListener {
            shortSheet.show(childFragmentManager, getString(R.string.shortsheet))
        }

        bind.swipe.setOnRefreshListener {
            vm.getTicketList()
            bind.swipe.isRefreshing = false
        }

        adapterTicket.onTapTicket = {
            ticketDetailSheet.arguments = Bundle().apply { putParcelable(DATA, it) }
            ticketDetailSheet.show(childFragmentManager, getString(R.string.ticketdetailsheet))
        }

        ticketDetailSheet.onTapEdit = {
            gooTo(TicketFragmentDirections.toEditTicket(it))
        }

        ticketDetailSheet.onTapDelete = {
            vmManagementTicket.deleteTicket(it)
        }
    }

    override fun initObserver() {
        observe(vm.ticketList) { data ->
            data?.data?.let {
                adapterTicket.clear()
                adapterTicket.addAll(it)
            }
            data?.message?.let { it.showToast() }
            data?.loading?.let { it.showLoadingDialog() }
            bind.emptyState.root.isVisible = adapterTicket.items.isEmpty()
        }

        observe(vmManagementTicket.deleteTicket) { data ->
            data?.data?.let {
                vm.getTicketList()
                getString(R.string.ticket_deleted_successfully).showToast()
            }
            data?.message?.let { it.showToast() }
            data?.loading?.let { it.showLoadingDialog() }
        }
    }

}

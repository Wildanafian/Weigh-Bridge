package com.wildan.weighbridge.ui.feature

import android.os.Bundle
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.wildan.weighbridge.R
import com.wildan.weighbridge.core.common.ProjectConstant.DATA
import com.wildan.weighbridge.core.ui.base.BaseFragment
import com.wildan.weighbridge.core.ui.helper.gooTo
import com.wildan.weighbridge.core.ui.helper.observe
import com.wildan.weighbridge.databinding.FragmentTicketBinding
import com.wildan.weighbridge.ui.compose.TicketItem
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

    private val ticketDetailSheet by lazy { TicketDetailBottomSheet() }
    private val filterSheet by lazy { FilterBottomSheet() }
    private val shortSheet by lazy { ShortBottomSheet() }

    @OptIn(ExperimentalMaterialApi::class)
    override fun initView() {
        vm.getTicketList()
        bind.composeView.setContent {
            val isRefresh = vm.ticketList.observeAsState().value?.loading ?: false
            val list = vm.ticketList.observeAsState().value?.data ?: emptyList()
            val pullRefreshState = rememberPullRefreshState(
                refreshing = isRefresh,
                onRefresh = { vm.getTicketList() }
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pullRefresh(pullRefreshState)
                    .padding(12.dp)
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(list) {
                        TicketItem(data = it) {
                            ticketDetailSheet.arguments = Bundle().apply { putParcelable(DATA, it) }
                            ticketDetailSheet.show(
                                childFragmentManager,
                                getString(R.string.ticketdetailsheet)
                            )
                        }
                    }
                }

                PullRefreshIndicator(
                    refreshing = isRefresh,
                    state = pullRefreshState,
                    modifier = Modifier.align(Alignment.TopCenter),
                )
            }
        }
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

//        adapterTicket.onTapTicket = {
//            ticketDetailSheet.arguments = Bundle().apply { putParcelable(DATA, it) }
//            ticketDetailSheet.show(childFragmentManager, getString(R.string.ticketdetailsheet))
//        }

        ticketDetailSheet.onTapEdit = {
            gooTo(TicketFragmentDirections.toEditTicket(it))
        }

        ticketDetailSheet.onTapDelete = {
            vmManagementTicket.deleteTicket(it)
        }
    }

    override fun initObserver() {
//        observe(vm.ticketList) { data ->
//            data?.data?.let {
//                adapterTicket.clear()
//                adapterTicket.addAll(it)
//            }
//            data?.message?.let { it.showToast() }
//            data?.loading?.let { it.showLoadingDialog() }
//            bind.emptyState.root.isVisible = adapterTicket.items.isEmpty()
//        }

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

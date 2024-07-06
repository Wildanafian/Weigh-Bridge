package com.wildan.weighbridge.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.wildan.weighbridge.core.model.SortFilterOption
import com.wildan.weighbridge.core.model.TicketItem
import com.wildan.weighbridge.core.model.base.RemoteResult
import com.wildan.weighbridge.core.model.base.UIStateData
import com.wildan.weighbridge.core.ui.base.BaseViewModel
import com.wildan.weighbridge.domain.FilterAndSortTicketsUseCase
import com.wildan.weighbridge.domain.TicketDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

/*
 * Created by Wildan Nafian on 7/4/24.
 * Github https://github.com/Wildanafian
 * wildanafian8@gmail.com
 */

@HiltViewModel
class TicketViewModel @Inject constructor(
    private val ticketUseCase: TicketDataUseCase,
    private val filterAnSortUseCase: FilterAndSortTicketsUseCase,
) : BaseViewModel() {

    private var _ticketList = MutableLiveData<UIStateData<List<TicketItem>>>()
    val ticketList: LiveData<UIStateData<List<TicketItem>>> = _ticketList

    fun getTicketList() {
        viewModelScope.launch {
            ticketUseCase.getTicketList()
                .onStart { _ticketList.value = UIStateData(loading = true) }
                .collect {
                    when (it) {
                        is RemoteResult.OnSuccess -> _ticketList.update(data = it.data)
                        is RemoteResult.OnError   -> _ticketList.update(message = it.message, data = it.data)
                    }
                }
        }
    }

    fun getSortedList(sortOption: SortFilterOption?) {
        viewModelScope.launch {
            val tickets = _ticketList.value?.data ?: emptyList()
            when (sortOption) {
                SortFilterOption.DATE           -> _ticketList.update(data = filterAnSortUseCase.sortByDate(tickets))
                SortFilterOption.DRIVER_NAME    -> _ticketList.update(data = filterAnSortUseCase.sortByDriverName(tickets))
                SortFilterOption.LICENSE_NUMBER -> _ticketList.update(data = filterAnSortUseCase.sortByLicenseNumber(tickets))
                else                            -> getTicketList()
            }
        }
    }

    fun getFilteredList(filter: SortFilterOption, keyword: String) {
        viewModelScope.launch {
            if (filter == SortFilterOption.NO_SORT_FILTER) {
                getTicketList()
                return@launch
            }

            val tickets = _ticketList.value?.data ?: emptyList()
            when (filter) {
                SortFilterOption.DATE        -> _ticketList.update(data = filterAnSortUseCase.filterByDate(tickets, keyword))
                SortFilterOption.DRIVER_NAME -> _ticketList.update(data = filterAnSortUseCase.filterByDriverName(tickets, keyword))
                else                         -> _ticketList.update(data = filterAnSortUseCase.filterByLicenseNumber(tickets, keyword))
            }
        }
    }

}

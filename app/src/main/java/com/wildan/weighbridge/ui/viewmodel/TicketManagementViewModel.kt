package com.wildan.weighbridge.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.wildan.weighbridge.core.model.TicketItem
import com.wildan.weighbridge.core.model.base.RemoteResult
import com.wildan.weighbridge.core.model.base.UIStateData
import com.wildan.weighbridge.core.ui.base.BaseViewModel
import com.wildan.weighbridge.domain.TicketManagementUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/*
 * Created by Wildan Nafian on 7/4/24.
 * Github https://github.com/Wildanafian
 * wildanafian8@gmail.com
 */

@HiltViewModel
class TicketManagementViewModel @Inject constructor(
    private val useCase: TicketManagementUseCase,
) : BaseViewModel() {

    private var _addNewTicket = MutableLiveData<UIStateData<String>>()
    val addNewTicket: LiveData<UIStateData<String>> = _addNewTicket

    fun submitNewTicket(data: TicketItem) {
        viewModelScope.launch {
            _addNewTicket.value = UIStateData(loading = true)
            when (val result = useCase.addNewTicket(data)) {
                is RemoteResult.OnSuccess -> _addNewTicket.update(data = result.data)
                is RemoteResult.OnError   -> _addNewTicket.update(message = result.message)
            }
        }
    }

    private var _editTicket = MutableLiveData<UIStateData<String>>()
    val editTicket: LiveData<UIStateData<String>> = _editTicket

    fun submitEditedTicket(data: TicketItem) {
        viewModelScope.launch {
            _editTicket.value = UIStateData(loading = true)
            when (val result = useCase.editTicket(data)) {
                is RemoteResult.OnSuccess -> _editTicket.update(data = result.data)
                is RemoteResult.OnError   -> _editTicket.update(message = result.message)
            }
        }
    }

    private var _deleteTicket = MutableLiveData<UIStateData<String>>()
    val deleteTicket: LiveData<UIStateData<String>> = _deleteTicket

    fun deleteTicket(ticketId: String) {
        viewModelScope.launch {
            _deleteTicket.value = UIStateData(loading = true)
            when (val result = useCase.deleteTicket(ticketId)) {
                is RemoteResult.OnSuccess -> _deleteTicket.update(data = result.data)
                is RemoteResult.OnError   -> _deleteTicket.update(message = result.message)
            }
        }
    }

}

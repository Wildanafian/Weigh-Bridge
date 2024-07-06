package com.wildan.weighbridge.domain

import com.wildan.weighbridge.core.model.TicketItem
import com.wildan.weighbridge.core.model.base.RemoteResult

/*
 * Created by Wildan Nafian on 7/4/24.
 * Github https://github.com/Wildanafian
 * wildanafian8@gmail.com
 */

interface TicketManagementUseCase {

    suspend fun addNewTicket(data: TicketItem): RemoteResult<String>
    suspend fun editTicket(data: TicketItem): RemoteResult<String>
    suspend fun deleteTicket(ticketId: String): RemoteResult<String>

}

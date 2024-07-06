package com.wildan.weighbridge.domain

import com.wildan.weighbridge.core.model.TicketItem
import com.wildan.weighbridge.core.model.base.RemoteResult
import com.wildan.weighbridge.data.repository.TicketManagementRepositoryImpl
import java.util.UUID
import javax.inject.Inject

/*
 * Created by Wildan Nafian on 7/4/24.
 * Github https://github.com/Wildanafian
 * wildanafian8@gmail.com
 */

class TicketManagementUseCaseImpl @Inject constructor(
    private val repository: TicketManagementRepositoryImpl,
) : TicketManagementUseCase {

    companion object {

        const val ID_LENGTH = 15
    }

    override suspend fun addNewTicket(data: TicketItem): RemoteResult<String> {
        val randomId = UUID.randomUUID().toString().take(ID_LENGTH)
        return repository.addNewTicket(data.copy(id = randomId))
    }

    override suspend fun editTicket(data: TicketItem): RemoteResult<String> {
        return repository.editTicket(data)
    }

    override suspend fun deleteTicket(ticketId: String): RemoteResult<String> {
        return repository.deleteTicket(ticketId)
    }

}

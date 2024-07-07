package com.wildan.weighbridge.core.firebase

import com.wildan.weighbridge.core.model.TicketItem
import com.wildan.weighbridge.core.model.base.RemoteResult
import kotlinx.coroutines.flow.Flow

interface FirebaseManager {
    suspend fun editTicket(data: TicketItem): RemoteResult<String>
    suspend fun deleteTicketById(ticketId: String): RemoteResult<String>
    fun getTicketData(): Flow<RemoteResult<List<TicketItem>>>
}
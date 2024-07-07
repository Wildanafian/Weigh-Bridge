package com.wildan.weighbridge.data.datasource.remote

import com.wildan.weighbridge.core.firebase.FirebaseManager
import com.wildan.weighbridge.core.model.TicketItem
import com.wildan.weighbridge.core.model.base.RemoteResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FirebaseRemoteImpl @Inject constructor(
    private val firebase: FirebaseManager,
) : FirebaseRemote {

    override suspend fun editTicket(data: TicketItem): RemoteResult<String> {
        return firebase.editTicket(data)
    }

    override suspend fun deleteTicketById(ticketId: String): RemoteResult<String> {
        return firebase.deleteTicketById(ticketId)
    }

    override fun getTicketData(): Flow<RemoteResult<List<TicketItem>>> {
        return firebase.getTicketData()
    }
}

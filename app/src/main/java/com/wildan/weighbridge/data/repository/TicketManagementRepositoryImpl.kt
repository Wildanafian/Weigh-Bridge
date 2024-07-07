package com.wildan.weighbridge.data.repository

import com.wildan.weighbridge.core.common.di.IOThread
import com.wildan.weighbridge.core.common.network.NetworkChecker
import com.wildan.weighbridge.core.model.PendingActionOption
import com.wildan.weighbridge.core.model.TicketItem
import com.wildan.weighbridge.core.model.base.RemoteResult
import com.wildan.weighbridge.data.datasource.local.PendingActionManager
import com.wildan.weighbridge.data.datasource.local.TicketManager
import com.wildan.weighbridge.data.datasource.remote.FirebaseRemote
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

/*
 * Created by Wildan Nafian on 7/4/24.
 * Github https://github.com/Wildanafian
 * wildanafian8@gmail.com
 */

class TicketManagementRepositoryImpl @Inject constructor(
    private val networkSource: FirebaseRemote,
    private val localSource: TicketManager,
    private val pendingAction: PendingActionManager,
    private val networkCheck: NetworkChecker,
    @IOThread private val ioDispatcher: CoroutineDispatcher,
) : TicketManagementRepository {

    override suspend fun addNewTicket(data: TicketItem): RemoteResult<String> {
        return withContext(ioDispatcher) {
            if (networkCheck.isConnected()) {
                when (val result = networkSource.editTicket(data)) {
                    is RemoteResult.OnSuccess -> {
                        localSource.editTicket(data)
                        result
                    }

                    else                      -> {
                        localSource.cacheTicket(data)
                        pendingAction.addNewPendingAction(PendingActionOption.ADD, data = data)
                        RemoteResult.OnSuccess(data = "success")
                    }
                }
            } else {
                localSource.cacheTicket(data)
                pendingAction.addNewPendingAction(PendingActionOption.ADD, data = data)
                RemoteResult.OnSuccess(data = "success")
            }
        }
    }

    override suspend fun editTicket(data: TicketItem): RemoteResult<String> {
        return withContext(ioDispatcher) {
            if (networkCheck.isConnected()) {
                when (val result = networkSource.editTicket(data)) {
                    is RemoteResult.OnSuccess -> {
                        localSource.editTicket(data)
                        result
                    }

                    else                      -> {
                        localSource.editTicket(data)
                        pendingAction.addNewPendingAction(action = PendingActionOption.EDIT, data = data)
                        RemoteResult.OnSuccess(data = "success")
                    }
                }
            } else {
                localSource.editTicket(data)
                pendingAction.addNewPendingAction(action = PendingActionOption.EDIT, data = data)
                RemoteResult.OnSuccess(data = "success")
            }
        }
    }

    override suspend fun deleteTicket(ticketId: String): RemoteResult<String> {
        return withContext(ioDispatcher) {
            if (networkCheck.isConnected()) {
                when (val result = networkSource.deleteTicketById(ticketId)) {
                    is RemoteResult.OnSuccess -> {
                        localSource.deleteByTicketId(ticketId)
                        result
                    }

                    else                      -> {
                        localSource.deleteByTicketId(ticketId)
                        pendingAction.addNewPendingAction(action = PendingActionOption.DELETE, data = TicketItem(id = ticketId))
                        RemoteResult.OnSuccess(data = "success")
                    }
                }
            } else {
                localSource.deleteByTicketId(ticketId)
                pendingAction.addNewPendingAction(action = PendingActionOption.DELETE, data = TicketItem(id = ticketId))
                RemoteResult.OnSuccess(data = "success")
            }
        }
    }

}

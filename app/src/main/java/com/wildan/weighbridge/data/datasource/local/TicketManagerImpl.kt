package com.wildan.weighbridge.data.datasource.local

import com.wildan.weighbridge.core.common.di.IOThread
import com.wildan.weighbridge.core.datastore.room.dao.TicketDao
import com.wildan.weighbridge.core.model.TicketItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

/*
 * Created by Wildan Nafian on 7/6/24.
 * Github https://github.com/Wildanafian
 * wildanafian8@gmail.com
 */

class TicketManagerImpl @Inject constructor(
    private val dao: TicketDao,
    @IOThread private val ioDispatcher: CoroutineDispatcher,
) : TicketManager {

    override suspend fun cacheTicket(data: TicketItem) {
        withContext(ioDispatcher) {
            dao.cacheTicket(data)
        }
    }

    override suspend fun cacheTicketList(data: List<TicketItem>) {
        withContext(ioDispatcher) {
            dao.clearTicketList()
            dao.cacheTicketList(data)
        }
    }

    override suspend fun getTicketList(): List<TicketItem> {
        return withContext(ioDispatcher) {
            dao.getTicketList()
        }
    }

    override suspend fun editTicket(data: TicketItem) {
        withContext(ioDispatcher) {
            dao.editTicket(data)
        }
    }

    override suspend fun deleteByTicketId(ticketId: String) {
        withContext(ioDispatcher) {
            dao.deleteByTicketId(ticketId)
        }
    }

}

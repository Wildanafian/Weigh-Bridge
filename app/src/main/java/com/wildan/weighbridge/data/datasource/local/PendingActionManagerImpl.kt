package com.wildan.weighbridge.data.datasource.local

import com.wildan.weighbridge.core.common.di.IOThread
import com.wildan.weighbridge.core.datastore.room.dao.TicketDao
import com.wildan.weighbridge.core.model.PendingActionItem
import com.wildan.weighbridge.core.model.PendingActionOption
import com.wildan.weighbridge.core.model.TicketItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

/*
 * Created by Wildan Nafian on 7/6/24.
 * Github https://github.com/Wildanafian
 * wildanafian8@gmail.com
 */

class PendingActionManagerImpl @Inject constructor(
    private val dao: TicketDao,
    @IOThread private val ioDispatcher: CoroutineDispatcher,
) : PendingActionManager {

    override suspend fun getPendingActionList(): List<PendingActionItem> {
        return withContext(ioDispatcher) {
            dao.getPendingActionList()
        }
    }

    override suspend fun addNewPendingAction(action: PendingActionOption, data: TicketItem) {
        withContext(ioDispatcher) {
            dao.addNewPendingAction(PendingActionItem(action = action, data = data))
        }
    }

    override suspend fun deletePendingActionById(id: Int) {
        withContext(ioDispatcher) {
            dao.deletePendingActionById(id)
        }
    }
}
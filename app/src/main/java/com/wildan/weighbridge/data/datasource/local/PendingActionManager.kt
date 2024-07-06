package com.wildan.weighbridge.data.datasource.local

import com.wildan.weighbridge.core.model.PendingActionItem
import com.wildan.weighbridge.core.model.PendingActionOption
import com.wildan.weighbridge.core.model.TicketItem

/*
 * Created by Wildan Nafian on 7/6/24.
 * Github https://github.com/Wildanafian
 * wildanafian8@gmail.com
 */

interface PendingActionManager {

    suspend fun getPendingActionList(): List<PendingActionItem>
    suspend fun addNewPendingAction(action: PendingActionOption, data: TicketItem)
    suspend fun deletePendingActionById(id: Int)

}
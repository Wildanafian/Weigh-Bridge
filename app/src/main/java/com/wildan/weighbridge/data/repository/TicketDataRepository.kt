package com.wildan.weighbridge.data.repository

import com.wildan.weighbridge.core.model.TicketItem
import com.wildan.weighbridge.core.model.base.RemoteResult
import kotlinx.coroutines.flow.Flow

/*
 * Created by Wildan Nafian on 7/4/24.
 * Github https://github.com/Wildanafian
 * wildanafian8@gmail.com
 */

interface TicketDataRepository {

    fun getTicketListData(): Flow<RemoteResult<List<TicketItem>>>
    suspend fun getCachedTicketList(): RemoteResult<List<TicketItem>>

}

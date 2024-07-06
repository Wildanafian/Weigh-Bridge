package com.wildan.weighbridge.domain

import com.wildan.weighbridge.core.model.TicketItem
import com.wildan.weighbridge.core.model.base.RemoteResult
import kotlinx.coroutines.flow.Flow

/*
 * Created by Wildan Nafian on 7/4/24.
 * Github https://github.com/Wildanafian
 * wildanafian8@gmail.com
 */

interface TicketDataUseCase {

    fun getTicketList(): Flow<RemoteResult<List<TicketItem>>>

}

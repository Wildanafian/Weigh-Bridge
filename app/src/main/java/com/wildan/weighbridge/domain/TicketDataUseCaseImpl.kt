package com.wildan.weighbridge.domain

import com.wildan.weighbridge.core.model.TicketItem
import com.wildan.weighbridge.core.model.base.RemoteResult
import com.wildan.weighbridge.domain.repository.TicketDataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

/*
 * Created by Wildan Nafian on 7/4/24.
 * Github https://github.com/Wildanafian
 * wildanafian8@gmail.com
 */

class TicketDataUseCaseImpl @Inject constructor(
    private val repository: TicketDataRepository,
) : TicketDataUseCase {

    override fun getTicketList(): Flow<RemoteResult<List<TicketItem>>> {
        return repository.getTicketListData()
            .onStart { emit(repository.getCachedTicketList()) }
    }
}

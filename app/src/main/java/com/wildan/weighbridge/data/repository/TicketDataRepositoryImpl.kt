package com.wildan.weighbridge.data.repository

import com.wildan.weighbridge.core.common.di.IOThread
import com.wildan.weighbridge.core.common.network.NetworkChecker
import com.wildan.weighbridge.core.model.TicketItem
import com.wildan.weighbridge.core.model.base.RemoteResult
import com.wildan.weighbridge.data.datasource.local.TicketManager
import com.wildan.weighbridge.data.datasource.remote.FirebaseRemote
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

/*
 * Created by Wildan Nafian on 7/4/24.
 * Github https://github.com/Wildanafian
 * wildanafian8@gmail.com
 */

class TicketDataRepositoryImpl @Inject constructor(
    private val networkSource: FirebaseRemote,
    private val localSource: TicketManager,
    private val networkCheck: NetworkChecker,
    @IOThread private val ioDispatcher: CoroutineDispatcher,
) : TicketDataRepository {

    override fun getTicketListData(): Flow<RemoteResult<List<TicketItem>>> {
        return flow {
            if (networkCheck.isConnected()) {
                networkSource.getTicketData().collect {
                    when (val result = it) {
                        is RemoteResult.OnSuccess -> {
                            localSource.cacheTicketList(result.data)
                            emit(getCachedTicketList())
                        }

                        is RemoteResult.OnError   -> emit(result.copy(data = localSource.getTicketList()))
                    }
                }
            } else {
                emit(getCachedTicketList())
            }
        }.catch {
            emit(
                RemoteResult.OnError(
                    data = localSource.getTicketList()
                )
            )
        }.flowOn(ioDispatcher)
    }

    override suspend fun getCachedTicketList(): RemoteResult<List<TicketItem>> {
        return withContext(ioDispatcher) {
            RemoteResult.OnSuccess(data = localSource.getTicketList())
        }
    }

}

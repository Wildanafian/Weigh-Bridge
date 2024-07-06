package com.wildan.weighbridge.core.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.wildan.weighbridge.core.model.PendingActionOption
import com.wildan.weighbridge.core.model.base.RemoteResult
import com.wildan.weighbridge.data.datasource.local.PendingActionManager
import com.wildan.weighbridge.data.datasource.remote.FirebaseManager
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

/*
 * Created by Wildan Nafian on 7/5/24.
 * Github https://github.com/Wildanafian
 * wildanafian8@gmail.com
 */
@HiltWorker
class SynchronizationWorker @AssistedInject constructor(
    private val networkSource: FirebaseManager,
    private val pendingAction: PendingActionManager,
    @Assisted appContext: Context,
    @Assisted private val params: WorkerParameters,
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val pendingChanges = pendingAction.getPendingActionList()
        for (pending in pendingChanges) {
            when (pending.action) {
                PendingActionOption.ADD, PendingActionOption.EDIT  -> {
                    when (networkSource.editTicket(pending.data)) {
                        is RemoteResult.OnSuccess -> {
                            pendingAction.deletePendingActionById(pending.id)
                        }

                        else                      -> {
                            return Result.retry()
                        }
                    }
                }

                else                     -> {
                    when (networkSource.deleteTicketById(pending.data.id)) {
                        is RemoteResult.OnSuccess -> {
                            pendingAction.deletePendingActionById(pending.id)
                        }

                        else                      -> return Result.retry()
                    }
                }
            }
        }
        return Result.success()
    }
}
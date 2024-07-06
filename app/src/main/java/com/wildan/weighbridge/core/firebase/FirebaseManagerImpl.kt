package com.wildan.weighbridge.core.firebase

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.wildan.weighbridge.core.common.di.IOThread
import com.wildan.weighbridge.core.model.TicketItem
import com.wildan.weighbridge.core.model.base.RemoteResult
import com.wildan.weighbridge.data.datasource.remote.FirebaseManager
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FirebaseManagerImpl @Inject constructor(
    private val firebase: DatabaseReference,
    @IOThread private val ioDispatcher: CoroutineDispatcher,
) : FirebaseManager {

    override suspend fun editTicket(data: TicketItem): RemoteResult<String> {
        val result: CompletableDeferred<RemoteResult<String>> = CompletableDeferred()
        withContext(ioDispatcher) {
            firebase.child(data.id).setValue(data)
                .addOnSuccessListener {
                    result.complete(RemoteResult.OnSuccess(data = ""))
                }
                .addOnFailureListener {
                    result.complete(
                        RemoteResult.OnError("Upps cannot write the data at the moment, relax for a while")
                    )
                }
        }
        return result.await()
    }

    override suspend fun deleteTicketById(ticketId: String): RemoteResult<String> {
        val result: CompletableDeferred<RemoteResult<String>> = CompletableDeferred()
        withContext(ioDispatcher) {
            firebase.child(ticketId).removeValue()
                .addOnSuccessListener {
                    result.complete(RemoteResult.OnSuccess(data = ""))
                }
                .addOnFailureListener {
                    result.complete(
                        RemoteResult.OnError("Upps cannot delete the data at the moment, relax for a while")
                    )
                }
        }
        return result.await()
    }

    override fun getTicketData(): Flow<RemoteResult<List<TicketItem>>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val dataList = snapshot.children.mapNotNull { it.getValue(TicketItem::class.java) }
                val finalData = RemoteResult.OnSuccess(data = dataList)
                trySend(finalData).isSuccess
            }

            override fun onCancelled(error: DatabaseError) {
                val data = RemoteResult.OnError(
                    message = "Upps cannot get the data at the moment, relax for a while",
                    data = null
                )
                trySend(data)
                close(error.toException())
            }
        }
        firebase.addValueEventListener(listener)
        awaitClose { firebase.removeEventListener(listener) }
    }

}
package com.wildan.weighbridge.core.datastore.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.wildan.weighbridge.core.model.PendingActionItem
import com.wildan.weighbridge.core.model.TicketItem

/*
 * Created by Wildan Nafian on 31/01/24.
 * Github https://github.com/Wildanafian
 * wildanafian8@gmail.com
 */

@Dao
interface TicketDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun cacheTicket(data: TicketItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun cacheTicketList(data: List<TicketItem>)

    @Query("Select * from tickets")
    suspend fun getTicketList(): List<TicketItem>

    @Update
    suspend fun editTicket(ticketId: TicketItem)

    @Query("DELETE FROM tickets WHERE id = :ticketId")
    suspend fun deleteByTicketId(ticketId: String)

    @Query("DELETE FROM tickets")
    fun clearTicketList()

    @Query("Select * from pending_action")
    suspend fun getPendingActionList(): List<PendingActionItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNewPendingAction(data: PendingActionItem)

    @Query("DELETE FROM pending_action WHERE id = :id")
    suspend fun deletePendingActionById(id: Int)

}
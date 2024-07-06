package com.wildan.weighbridge.core.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pending_action")
data class PendingActionItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val action: PendingActionOption,
    val data: TicketItem,
)
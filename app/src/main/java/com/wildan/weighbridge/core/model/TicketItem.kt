package com.wildan.weighbridge.core.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "tickets")
@Parcelize
data class TicketItem(
    @PrimaryKey val id: String = "",
    val date: String? = "",
    val time: String? = "",
    val licenseNumber: String? = "",
    val driverName: String? = "",
    val inboundWeight: Double? = 0.0,
    val outboundWeight: Double? = 0.0,
    val netWeight: Double? = 0.0,
) : Parcelable

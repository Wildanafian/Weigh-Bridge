package com.wildan.weighbridge.core.datastore.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.wildan.weighbridge.core.model.PendingActionOption
import com.wildan.weighbridge.core.model.TicketItem

class Converters {

    @TypeConverter
    fun fromPendingActionOption(value: PendingActionOption): String {
        return value.name
    }

    @TypeConverter
    fun toPendingActionOption(value: String): PendingActionOption {
        return PendingActionOption.valueOf(value)
    }

    @TypeConverter
    fun fromTicketItem(ticketItem: TicketItem): String {
        return Gson().toJson(ticketItem)
    }

    @TypeConverter
    fun toTicketItem(value: String): TicketItem {
        val type = object : TypeToken<TicketItem>() {}.type
        return Gson().fromJson(value, type)
    }
}

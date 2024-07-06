package com.wildan.weighbridge.data.datasource.local

import com.wildan.weighbridge.core.model.TicketItem

/*
 * Created by Wildan Nafian on 7/6/24.
 * Github https://github.com/Wildanafian
 * wildanafian8@gmail.com
 */

interface TicketManager {

    suspend fun cacheTicket(data: TicketItem)
    suspend fun cacheTicketList(data: List<TicketItem>)
    suspend fun getTicketList(): List<TicketItem>
    suspend fun editTicket(data: TicketItem)
    suspend fun deleteByTicketId(ticketId: String)

}
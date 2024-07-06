package com.wildan.weighbridge.domain

import com.wildan.weighbridge.core.model.TicketItem

/*
 * Created by Wildan Nafian on 7/4/24.
 * Github https://github.com/Wildanafian
 * wildanafian8@gmail.com
 */

interface FilterAndSortTicketsUseCase {

    suspend fun sortByDate(data: List<TicketItem>): List<TicketItem>

    suspend fun sortByDriverName(data: List<TicketItem>): List<TicketItem>

    suspend fun sortByLicenseNumber(data: List<TicketItem>): List<TicketItem>

    suspend fun filterByDate(data: List<TicketItem>, keyword: String): List<TicketItem>

    suspend fun filterByDriverName(data: List<TicketItem>, keyword: String): List<TicketItem>

    suspend fun filterByLicenseNumber(data: List<TicketItem>, keyword: String): List<TicketItem>

}

package com.wildan.weighbridge.domain

import com.wildan.weighbridge.core.model.TicketItem
import javax.inject.Inject

/*
 * Created by Wildan Nafian on 7/4/24.
 * Github https://github.com/Wildanafian
 * wildanafian8@gmail.com
 */

class FilterAndSortTicketsUseCaseImpl @Inject constructor() : FilterAndSortTicketsUseCase {

    override suspend fun sortByDate(data: List<TicketItem>): List<TicketItem> {
        return data.sortedBy { it.date }
    }

    override suspend fun sortByDriverName(data: List<TicketItem>): List<TicketItem> {
        return data.sortedBy { it.driverName }
    }

    override suspend fun sortByLicenseNumber(data: List<TicketItem>): List<TicketItem> {
        return data.sortedBy { it.licenseNumber }
    }

    override suspend fun filterByDate(data: List<TicketItem>, keyword: String): List<TicketItem> {
        return data.filter { it.date.orEmpty().contains(keyword, true) }
    }

    override suspend fun filterByDriverName(data: List<TicketItem>, keyword: String): List<TicketItem> {
        return data.filter { it.driverName.orEmpty().contains(keyword, true) }
    }

    override suspend fun filterByLicenseNumber(data: List<TicketItem>, keyword: String): List<TicketItem> {
        return data.filter { it.licenseNumber.orEmpty().contains(keyword, true) }
    }

}

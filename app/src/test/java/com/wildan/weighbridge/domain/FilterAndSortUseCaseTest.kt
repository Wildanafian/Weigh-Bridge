package com.wildan.weighbridge.domain

import com.wildan.weighbridge.core.model.TicketItem
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Test

/*
 * Created by Wildan Nafian on 7/6/24.
 * Github https://github.com/Wildanafian
 * wildanafian8@gmail.com
 */

class FilterAndSortUseCaseTest {

    private val sut = FilterAndSortTicketsUseCaseImpl()

    private val ticketList = listOf(
        TicketItem(
            id = "23ih8dd",
            date = "11/11/11",
            time = "11:11",
            licenseNumber = "AA 1234 YY",
            driverName = "Joko",
            inboundWeight = 50.0,
            outboundWeight = 10.0,
            netWeight = 40.0
        ),
        TicketItem(
            id = "dnjdic0",
            date = "12/11/11",
            time = "12:11",
            licenseNumber = "AB 1234 YY",
            driverName = "Karima",
            inboundWeight = 30.0,
            outboundWeight = 10.0,
            netWeight = 20.0
        ),
        TicketItem(
            id = "zcvope",
            date = "12/11/11",
            time = "12:11",
            licenseNumber = "AB 1234 YY",
            driverName = "Dedi",
            inboundWeight = 35.0,
            outboundWeight = 10.0,
            netWeight = 25.0
        ),
    )

    @Test
    fun sortByDate_givenListOfTicketItems_sortsByDate() = runTest {
        val expected = listOf("23ih8dd", "dnjdic0", "zcvope")
        val result = sut.sortByDate(data = ticketList).map { it.id }
        assertEquals(expected, result)
    }

    @Test
    fun sortByDriverName_givenListOfTicketItems_sortsByDriverName() = runTest {
        val expected = listOf("zcvope", "23ih8dd","dnjdic0")
        val result = sut.sortByDriverName(data = ticketList).map { it.id }
        assertEquals(expected, result)
    }

    @Test
    fun sortByLicenseNumber_givenListOfTicketItems_sortsByLicenseNumber() = runTest {
        val expected = listOf("23ih8dd", "dnjdic0", "zcvope")
        val result = sut.sortByLicenseNumber(data = ticketList).map { it.id }
        assertEquals(expected, result)
    }

    @Test
    fun filterByDate_givenListOfTicketItemsAndKeyword_filtersByDate() = runTest {
        val expected = listOf("23ih8dd")
        val result = sut.filterByDate(data = ticketList, keyword = "11/11/11").map { it.id }
        assertEquals(expected, result)
    }

    @Test
    fun filterByDriverName_givenListOfTicketItemsAndKeyword_filtersByDriverName() = runTest {
        val expected = listOf("23ih8dd")
        val result = sut.filterByDriverName(data = ticketList, keyword = "Joko").map { it.id }
        assertEquals(expected, result)
    }

    @Test
    fun filterByLicenseNumber_givenListOfTicketItemsAndKeyword_filtersByLicenseNumber() = runTest {
        val expected = listOf("dnjdic0", "zcvope")
        val result = sut.filterByLicenseNumber(data = ticketList, keyword = "AB 1234 YY").map { it.id }
        assertEquals(expected, result)
    }

}
package com.wildan.weighbridge.domain

import app.cash.turbine.test
import com.wildan.weighbridge.core.model.TicketItem
import com.wildan.weighbridge.core.model.base.RemoteResult
import com.wildan.weighbridge.data.repository.TicketDataRepository
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test

/*
 * Created by Wildan Nafian on 7/6/24.
 * Github https://github.com/Wildanafian
 * wildanafian8@gmail.com
 */

class TicketDataUseCaseTest {

    private val repository: TicketDataRepository = mockk()
    private val sut = TicketDataUseCaseImpl(repository)

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
        )
    )

    @Test
    fun getTicketList_returnsListOfTicketList() = runTest {
        val expected = RemoteResult.OnSuccess(data = ticketList)
        val expectedFromCache = RemoteResult.OnSuccess(listOf(expected.data[1]))
        coEvery { repository.getCachedTicketList() } returns expectedFromCache
        coEvery { repository.getTicketListData() } returns flowOf(expected)

        sut.getTicketList().test {
            assertEquals(expectedFromCache, awaitItem())
            assertEquals(expected, awaitItem())
            awaitComplete()
        }
    }

}
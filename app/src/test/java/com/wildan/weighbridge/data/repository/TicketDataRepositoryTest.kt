package com.wildan.weighbridge.data.repository

import app.cash.turbine.test
import com.wildan.weighbridge.core.common.network.NetworkChecker
import com.wildan.weighbridge.core.model.TicketItem
import com.wildan.weighbridge.core.model.base.RemoteResult
import com.wildan.weighbridge.data.datasource.local.TicketManager
import com.wildan.weighbridge.data.datasource.remote.FirebaseManager
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

/*
 * Created by Wildan Nafian on 7/6/24.
 * Github https://github.com/Wildanafian
 * wildanafian8@gmail.com
 */

class TicketDataRepositoryTest {

    private val networkSource: FirebaseManager = mockk()
    private val localSource: TicketManager = mockk()
    private val networkCheck: NetworkChecker = mockk()
    private val ioDispatcher = Dispatchers.IO

    private val sut = TicketDataRepositoryImpl(networkSource, localSource, networkCheck, ioDispatcher)

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

    @Before
    fun start() {
        coEvery { localSource.getTicketList() } returns ticketList
    }

    @Test
    fun getTicketListData_networkConnected_returnsRemoteResultOnSuccessWithCachedData() = runTest {
        val expected = RemoteResult.OnSuccess(ticketList)
        coEvery { networkCheck.isConnected() } returns true
        coEvery { networkSource.getTicketData() } returns flowOf(expected)
        coEvery { localSource.cacheTicketList(ticketList) } returns Unit

        sut.getTicketListData().test {
            assertEquals(expected, awaitItem())
            awaitComplete()
        }
        coVerify(exactly = 1) { localSource.cacheTicketList(ticketList) }
        coVerify(exactly = 1) { localSource.getTicketList() }
    }

    @Test
    fun getTicketListData_networkConnected_returnsRemoteResultOnErrorWithCachedData() = runTest {
        val expected = RemoteResult.OnError<List<TicketItem>>(message = "error", data = ticketList)
        val remoteError = RemoteResult.OnError<List<TicketItem>>("error")
        coEvery { networkCheck.isConnected() } returns true
        coEvery { networkSource.getTicketData() } returns flowOf(remoteError)

        sut.getTicketListData().test {
            assertEquals(expected, awaitItem())
            awaitComplete()
        }
        coVerify(exactly = 1) { localSource.getTicketList() }
    }

    @Test
    fun getTicketListData_networkNotConnected_returnsCachedTicketList() = runTest {
        val expected = RemoteResult.OnSuccess(data = ticketList)
        coEvery { networkCheck.isConnected() } returns false

        sut.getTicketListData().test {
            assertEquals(expected, awaitItem())
            awaitComplete()
        }
        coVerify(exactly = 1) { localSource.getTicketList() }
    }

    @Test
    fun getTicketListData_whenExceptionThrown_returnsRemoteResultOnErrorWithCachedData() = runTest {
        coEvery { networkCheck.isConnected() } returns true
        coEvery { networkSource.getTicketData() } throws IllegalStateException()
        val expected = RemoteResult.OnError(data = ticketList)
        sut.getTicketListData().test {
            assertEquals(expected, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun getCachedTicketList_returnsRemoteResultOnSuccessWithCachedData() = runTest {
        val expected = RemoteResult.OnSuccess(ticketList)
        assertEquals(expected, sut.getCachedTicketList())
    }

}
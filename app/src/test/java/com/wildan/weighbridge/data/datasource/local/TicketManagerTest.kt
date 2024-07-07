package com.wildan.weighbridge.data.datasource.local

import com.wildan.weighbridge.core.datastore.dao.TicketDao
import com.wildan.weighbridge.core.model.TicketItem
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import org.junit.Test

/*
 * Created by Wildan Nafian on 7/6/24.
 * Github https://github.com/Wildanafian
 * wildanafian8@gmail.com
 */

class TicketManagerTest {

    private val roomDao: TicketDao = mockk()
    private val ioDispatcher = Dispatchers.IO

    private val sut = TicketManagerImpl(roomDao, ioDispatcher)

    @Test
    fun cacheTicket_givenValidTicketItem_cachesTicket() = runTest {
        val data = TicketItem(id = "iuhind", driverName = "wqdsj")
        coEvery { roomDao.cacheTicket(data) } returns Unit

        sut.cacheTicket(TicketItem(id = "iuhind", driverName = "wqdsj"))
        coVerify(exactly = 1) { roomDao.cacheTicket(data) }
    }

    @Test
    fun cacheTicketList_givenValidTicketItemList_cachesTicketList() = runTest {
        val data = listOf(
            TicketItem(id = "iuhind", driverName = "wqdsj"),
            TicketItem(id = "zxcnji", driverName = "jisdu")
        )
        coEvery { roomDao.clearTicketList() } returns Unit
        coEvery { roomDao.cacheTicketList(data) } returns Unit

        sut.cacheTicketList(data)
        coVerify(exactly = 1) { roomDao.cacheTicketList(data) }
    }

    @Test
    fun getTicketList_returnsListOfCachedTickets() = runTest {
        val expectedResult = listOf(
            TicketItem(id = "iuhind", driverName = "wqdsj"),
            TicketItem(id = "zxcnji", driverName = "jisdu")
        )
        coEvery { roomDao.getTicketList() } returns expectedResult

        assertEquals(expectedResult, sut.getTicketList())
    }

    @Test
    fun editTicket_givenValidTicketItem_editsTicket() = runTest {
        val data = TicketItem(id = "iuhind", driverName = "wqdsj")
        coEvery { roomDao.editTicket(data) } returns Unit

        sut.editTicket(TicketItem(id = "iuhind", driverName = "wqdsj"))
        coVerify(exactly = 1) { roomDao.editTicket(data) }
    }

    @Test
    fun deleteByTicketId_givenValidTicketId_deletesTicket() = runTest {
        val ticketId = "iuhind"
        coEvery { roomDao.deleteByTicketId(ticketId) } returns Unit

        sut.deleteByTicketId(ticketId)
        coVerify(exactly = 1) { roomDao.deleteByTicketId(ticketId) }
    }

}
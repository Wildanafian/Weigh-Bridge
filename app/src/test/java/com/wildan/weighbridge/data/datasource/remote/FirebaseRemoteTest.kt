package com.wildan.weighbridge.data.datasource.remote

import com.wildan.weighbridge.core.firebase.FirebaseManager
import com.wildan.weighbridge.core.model.TicketItem
import com.wildan.weighbridge.core.model.base.RemoteResult
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test

/*
 * Created by Wildan Nafian on 7/6/24.
 * Github https://github.com/Wildanafian
 * wildanafian8@gmail.com
 */

class FirebaseRemoteTest {

    private val firebase: FirebaseManager = mockk()

    private val sut = FirebaseRemoteImpl(firebase)

    private val ticket = TicketItem(id = "iuhind", driverName = "wqdsj")

    @Test
    fun editTicket_givenValidTicketItem_returnsOnSuccess() = runTest {
        val expected = RemoteResult.OnSuccess("s")
        coEvery { firebase.editTicket(ticket) } returns expected
        assertEquals(expected, sut.editTicket(ticket))
    }

    @Test
    fun editTicket_givenValidTicketItem_returnsOnError() = runTest {
        val expected = RemoteResult.OnError<String>("s")
        coEvery { firebase.editTicket(ticket) } returns expected
        assertEquals(expected, sut.editTicket(ticket))
    }

    @Test
    fun deleteTicketById_givenValidTicketId_returnsOnSuccess() = runTest {
        val expected = RemoteResult.OnSuccess("s")
        coEvery { firebase.deleteTicketById(ticket.id) } returns expected
        assertEquals(expected, sut.deleteTicketById(ticket.id))
    }

    @Test
    fun deleteTicketById_givenValidTicketId_returnsOnError() = runTest {
        val expected = RemoteResult.OnError<String>("s")
        coEvery { firebase.deleteTicketById(ticket.id) } returns expected
        assertEquals(expected, sut.deleteTicketById(ticket.id))
    }

    @Test
    fun getTicketData_returnsFlowOfRemoteResultOnSuccess() = runTest {
        val expected = RemoteResult.OnSuccess(listOf(ticket))
        coEvery { firebase.getTicketData() } returns flowOf(expected)
        assertEquals(expected, sut.getTicketData().first())
    }

    @Test
    fun getTicketData_returnsFlowOfRemoteResultOnError() = runTest {
        val expected = RemoteResult.OnError<List<TicketItem>>("s")
        coEvery { firebase.getTicketData() } returns flowOf(expected)
        assertEquals(expected, sut.getTicketData().first())
    }

}
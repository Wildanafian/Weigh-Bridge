package com.wildan.weighbridge.domain

import com.wildan.weighbridge.core.model.TicketItem
import com.wildan.weighbridge.core.model.base.RemoteResult
import com.wildan.weighbridge.data.repository.TicketManagementRepositoryImpl
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.util.UUID

/*
 * Created by Wildan Nafian on 7/6/24.
 * Github https://github.com/Wildanafian
 * wildanafian8@gmail.com
 */

class TicketManagementUseCaseTest {

    private val repository: TicketManagementRepositoryImpl = mockk()
    private val sut = TicketManagementUseCaseImpl(repository)
    private val ticket = TicketItem(driverName = "Joko")

    @Test
    fun addNewTicket_givenValidTicketItem_addsNewTicketAndReturnsResult() = runTest {
        mockkStatic(UUID::class)
        val fakeUUIDString = "111e4567-e11b-12d3-a456-426614174000"
        val expected = RemoteResult.OnSuccess("s")

        every { UUID.randomUUID().toString() } returns fakeUUIDString
        coEvery { repository.addNewTicket(ticket.copy(id = "111e4567-e11b-1")) } returns expected

        assertEquals(expected, sut.addNewTicket(ticket))
        coVerify { repository.addNewTicket(ticket.copy(id = "111e4567-e11b-1")) }
    }

    @Test
    fun editTicket_givenValidTicketItem_editsTicketAndReturnsResult() = runTest {
        val expected = RemoteResult.OnSuccess("s")

        coEvery { repository.editTicket(ticket) } returns expected

        assertEquals(expected, sut.editTicket(ticket))
        coVerify { repository.editTicket(ticket) }
    }

    @Test
    fun deleteTicket_givenValidTicketId_deletesTicketAndReturnsResult() = runTest {
        val ticket = TicketItem(id = "asd", driverName = "Joko")
        val expected = RemoteResult.OnSuccess("s")

        coEvery { repository.deleteTicket("asd") } returns expected

        assertEquals(expected, sut.deleteTicket(ticket.id))
        coVerify { repository.deleteTicket(ticket.id) }
    }

}
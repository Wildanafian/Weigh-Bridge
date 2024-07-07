package com.wildan.weighbridge.data.repository

import com.wildan.weighbridge.core.common.network.NetworkChecker
import com.wildan.weighbridge.core.model.PendingActionOption
import com.wildan.weighbridge.core.model.TicketItem
import com.wildan.weighbridge.core.model.base.RemoteResult
import com.wildan.weighbridge.data.datasource.local.PendingActionManager
import com.wildan.weighbridge.data.datasource.local.TicketManager
import com.wildan.weighbridge.data.datasource.remote.FirebaseRemote
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

class TicketManagementRepositoryTest {

    private val networkSource: FirebaseRemote = mockk()
    private val localSource: TicketManager = mockk()
    private val pendingAction: PendingActionManager = mockk()
    private val networkCheck: NetworkChecker = mockk()
    private val ioDispatcher = Dispatchers.IO

    private val sut = TicketManagementRepositoryImpl(
        networkSource,
        localSource,
        pendingAction,
        networkCheck,
        ioDispatcher
    )

    private val ticket = TicketItem(
        id = "123",
        date = "11/11/11",
        time = "11:11",
        licenseNumber = "AA 1234 YY",
        driverName = "Joko",
        inboundWeight = 50.0,
        outboundWeight = 10.0,
        netWeight = 40.0
    )

    @Test
    fun addNewTicket_networkConnected_returnsRemoteResultOnSuccess() = runTest {
        val expected = RemoteResult.OnSuccess("S")
        coEvery { networkCheck.isConnected() } returns true
        coEvery { networkSource.editTicket(ticket) } returns expected
        coEvery { localSource.editTicket(ticket) } returns Unit

        assertEquals(expected, sut.addNewTicket(ticket))
        coVerify { localSource.editTicket(ticket) }
    }

    @Test
    fun addNewTicket_networkConnectedWithErrorResponse_cachesTicketAndAddsPendingAction() = runTest {
        val expected = RemoteResult.OnSuccess(data = "success")
        val remoteResponse = RemoteResult.OnError<String>("S")
        coEvery { networkCheck.isConnected() } returns true
        coEvery { networkSource.editTicket(ticket) } returns remoteResponse
        coEvery { localSource.cacheTicket(ticket) } returns Unit
        coEvery { pendingAction.addNewPendingAction(PendingActionOption.ADD, data = ticket) } returns Unit

        assertEquals(expected, sut.addNewTicket(ticket))
        coVerify { localSource.cacheTicket(ticket) }
        coVerify { pendingAction.addNewPendingAction(PendingActionOption.ADD, data = ticket) }
    }

    @Test
    fun addNewTicket_networkNotConnected_cachesTicketAndAddsPendingAction() = runTest {
        val expected = RemoteResult.OnSuccess(data = "success")
        coEvery { networkCheck.isConnected() } returns false
        coEvery { localSource.cacheTicket(ticket) } returns Unit
        coEvery { pendingAction.addNewPendingAction(PendingActionOption.ADD, data = ticket) } returns Unit

        assertEquals(expected, sut.addNewTicket(ticket))
        coVerify { localSource.cacheTicket(ticket) }
        coVerify { pendingAction.addNewPendingAction(PendingActionOption.ADD, data = ticket) }
    }

    @Test
    fun editTicket_networkConnected_returnsRemoteResultOnSuccess() = runTest {
        val expected = RemoteResult.OnSuccess("S")
        coEvery { networkCheck.isConnected() } returns true
        coEvery { networkSource.editTicket(ticket) } returns expected
        coEvery { localSource.editTicket(ticket) } returns Unit

        assertEquals(expected, sut.editTicket(ticket))
        coVerify { localSource.editTicket(ticket) }
    }

    @Test
    fun editTicket_networkConnectedWithErrorResponse_cachesTicketAndAddsPendingAction() = runTest {
        val expected = RemoteResult.OnSuccess(data = "success")
        val remoteResponse = RemoteResult.OnError<String>("S")
        coEvery { networkCheck.isConnected() } returns true
        coEvery { networkSource.editTicket(ticket) } returns remoteResponse
        coEvery { localSource.editTicket(ticket) } returns Unit
        coEvery { pendingAction.addNewPendingAction(PendingActionOption.EDIT, data = ticket) } returns Unit

        assertEquals(expected, sut.editTicket(ticket))
        coVerify { localSource.editTicket(ticket) }
        coVerify { pendingAction.addNewPendingAction(PendingActionOption.EDIT, data = ticket) }
    }

    @Test
    fun editTicket_networkNotConnected_editsTicketAndAddsPendingAction() = runTest {
        val expected = RemoteResult.OnSuccess(data = "success")
        coEvery { networkCheck.isConnected() } returns false
        coEvery { localSource.editTicket(ticket) } returns Unit
        coEvery { pendingAction.addNewPendingAction(PendingActionOption.EDIT, data = ticket) } returns Unit

        assertEquals(expected, sut.editTicket(ticket))
        coVerify { localSource.editTicket(ticket) }
        coVerify { pendingAction.addNewPendingAction(PendingActionOption.EDIT, data = ticket) }
    }

    @Test
    fun deleteTicket_networkConnected_returnsRemoteResultOnSuccess() = runTest {
        val expected = RemoteResult.OnSuccess("S")
        coEvery { networkCheck.isConnected() } returns true
        coEvery { networkSource.deleteTicketById(ticket.id) } returns expected
        coEvery { localSource.deleteByTicketId(ticket.id) } returns Unit

        assertEquals(expected, sut.deleteTicket(ticket.id))
        coVerify { localSource.deleteByTicketId(ticket.id) }
    }

    @Test
    fun deleteTicket_networkConnectedWithErrorResponse_cachesTicketAndAddsPendingAction() = runTest {
        val expected = RemoteResult.OnSuccess(data = "success")
        val remoteResponse = RemoteResult.OnError<String>("S")
        coEvery { networkCheck.isConnected() } returns true
        coEvery { networkSource.deleteTicketById(ticket.id) } returns remoteResponse
        coEvery { localSource.deleteByTicketId(ticket.id) } returns Unit
        coEvery { pendingAction.addNewPendingAction(PendingActionOption.DELETE, data = TicketItem(id = ticket.id)) } returns Unit

        assertEquals(expected, sut.deleteTicket(ticket.id))
        coVerify { localSource.deleteByTicketId(ticket.id) }
        coVerify { pendingAction.addNewPendingAction(PendingActionOption.DELETE, data = TicketItem(id = ticket.id)) }
    }

    @Test
    fun deleteTicket_networkNotConnected_deletesTicketAndAddsPendingAction() = runTest {
        val expected = RemoteResult.OnSuccess(data = "success")
        coEvery { networkCheck.isConnected() } returns false
        coEvery { localSource.deleteByTicketId(ticket.id) } returns Unit
        coEvery { pendingAction.addNewPendingAction(PendingActionOption.DELETE, data = TicketItem(id = ticket.id)) } returns Unit

        assertEquals(expected, sut.deleteTicket(ticket.id))
        coVerify { localSource.deleteByTicketId(ticket.id) }
        coVerify { pendingAction.addNewPendingAction(PendingActionOption.DELETE, data = TicketItem(id = ticket.id)) }
    }

}
package com.wildan.weighbridge.data.datasource.local

import com.wildan.weighbridge.core.datastore.dao.TicketDao
import com.wildan.weighbridge.core.model.PendingActionItem
import com.wildan.weighbridge.core.model.PendingActionOption
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

class PendingActionManagerTest {

    private val roomDao: TicketDao = mockk()
    private val ioDispatcher = Dispatchers.IO

    private val sut = PendingActionManagerImpl(roomDao, ioDispatcher)

    @Test
    fun getPendingActionList_returnsListOfPendingActions() = runTest {
        val expectedResult = listOf(
            PendingActionItem(id = 6942, action = PendingActionOption.ADD, data = TicketItem(id = "Concetta")),
            PendingActionItem(id = 12421, action = PendingActionOption.ADD, data = TicketItem(id = "Zzcas")),
            PendingActionItem(id = 897, action = PendingActionOption.DELETE, data = TicketItem(id = "erf"))
        )
        coEvery { roomDao.getPendingActionList() } returns expectedResult

        assertEquals(expectedResult, sut.getPendingActionList())
    }

    @Test
    fun addNewPendingAction_givenValidActionAndData_addsPendingAction() = runTest {
        val expectedResult = PendingActionItem(action = PendingActionOption.ADD, data = TicketItem(id = "Concetta"))
        coEvery { roomDao.addNewPendingAction(expectedResult) } returns Unit

        sut.addNewPendingAction(PendingActionOption.ADD, TicketItem(id = "Concetta"))
        coVerify(exactly = 1) { roomDao.addNewPendingAction(expectedResult) }
    }

    @Test
    fun deletePendingActionById_givenValidId_deletesPendingAction() = runTest {
     val pendingId = 1234
     coEvery { roomDao.deletePendingActionById(pendingId) } returns Unit

     sut.deletePendingActionById(1234)
     coVerify(exactly = 1) { roomDao.deletePendingActionById(pendingId) }
    }

}
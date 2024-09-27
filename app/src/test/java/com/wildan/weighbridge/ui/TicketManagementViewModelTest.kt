package com.wildan.weighbridge.ui

import androidx.lifecycle.Observer
import com.wildan.weighbridge.core.model.TicketItem
import com.wildan.weighbridge.core.model.base.RemoteResult
import com.wildan.weighbridge.core.model.base.UIStateData
import com.wildan.weighbridge.domain.TicketManagementUseCase
import com.wildan.weighbridge.ui.helper.BaseTestInstantTaskExecutorRule
import com.wildan.weighbridge.ui.viewmodel.TicketManagementViewModel
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifySequence
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

/*
 * Created by Wildan Nafian on 7/6/24.
 * Github https://github.com/Wildanafian
 * wildanafian8@gmail.com
 */

@ExperimentalCoroutinesApi
class TicketManagementViewModelTest : BaseTestInstantTaskExecutorRule() {

    private val useCase: TicketManagementUseCase = mockk()

    private val sut = TicketManagementViewModel(useCase)
    private val observerAddNewTicket: Observer<UIStateData<String>> = mockk(relaxUnitFun = true)
    private val observerEditTicket: Observer<UIStateData<String>> = mockk(relaxUnitFun = true)
    private val observerDeleteTicket: Observer<UIStateData<String>> = mockk(relaxUnitFun = true)

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

    @Before
    fun setup() {
        sut.addNewTicket.observeForever(observerAddNewTicket)
        sut.editTicket.observeForever(observerEditTicket)
        sut.deleteTicket.observeForever(observerDeleteTicket)
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        sut.addNewTicket.removeObserver(observerAddNewTicket)
        sut.editTicket.removeObserver(observerEditTicket)
        sut.deleteTicket.removeObserver(observerDeleteTicket)
        Dispatchers.resetMain()
    }

    @Test
    fun submitNewTicket_givenValidTicketItem_returnsOnSuccessUpdatesLiveData() = runTest(testDispatcher) {
        val useCaseResponse = RemoteResult.OnSuccess("S")
        coEvery { useCase.addNewTicket(ticket) } returns useCaseResponse
        sut.submitNewTicket(ticket)
        advanceUntilIdle()

        verifySequence {
            observerAddNewTicket.onChanged(UIStateData(loading = true))
            observerAddNewTicket.onChanged(UIStateData(loading = false, data = "S"))
        }

        verify(exactly = 2) { observerAddNewTicket.onChanged(any()) }

    }

    @Test
    fun submitNewTicket_givenValidTicketItem_returnsOnErrorUpdatesLiveData() = runTest(testDispatcher) {
        val useCaseResponse = RemoteResult.OnError<String>("S")
        coEvery { useCase.addNewTicket(ticket) } returns useCaseResponse
        sut.submitNewTicket(ticket)
        advanceUntilIdle()

        verifySequence {
            observerAddNewTicket.onChanged(UIStateData(loading = true))
            observerAddNewTicket.onChanged(UIStateData(loading = false, message = "S"))
        }

        verify(exactly = 2) { observerAddNewTicket.onChanged(any()) }
    }

    @Test
    fun submitEditedTicket_givenValidTicketItem_returnsOnSuccessUpdatesLiveData() = runTest(testDispatcher) {
        val useCaseResponse = RemoteResult.OnSuccess("S")
        coEvery { useCase.editTicket(ticket) } returns useCaseResponse
        sut.submitEditedTicket(ticket)
        advanceUntilIdle()

        verifySequence {
            observerEditTicket.onChanged(UIStateData(loading = true))
            observerEditTicket.onChanged(UIStateData(loading = false, data = "S"))
        }

        verify(exactly = 2) { observerEditTicket.onChanged(any()) }
    }

    @Test
    fun submitEditedTicket_givenValidTicketItem_returnsOnErrorUpdatesLiveData() = runTest(testDispatcher) {
        val useCaseResponse = RemoteResult.OnError<String>("S")
        coEvery { useCase.deleteTicket(ticket.id) } returns useCaseResponse
        sut.deleteTicket(ticket.id)
        advanceUntilIdle()

        verifySequence {
            observerDeleteTicket.onChanged(UIStateData(loading = true))
            observerDeleteTicket.onChanged(UIStateData(loading = false, message = "S"))
        }

        verify(exactly = 2) { observerDeleteTicket.onChanged(any()) }
    }

    @Test
    fun deleteTicket_givenValidTicketId_returnsOnSuccessUpdatesLiveData() = runTest(testDispatcher) {
        val useCaseResponse = RemoteResult.OnSuccess("S")
        coEvery { useCase.editTicket(ticket) } returns useCaseResponse
        sut.submitEditedTicket(ticket)
        advanceUntilIdle()

        verifySequence {
            observerEditTicket.onChanged(UIStateData(loading = true))
            observerEditTicket.onChanged(UIStateData(loading = false, data = "S"))
        }

        verify(exactly = 2) { observerEditTicket.onChanged(any()) }
    }

    @Test
    fun deleteTicket_givenValidTicketId_returnsOnErrorUpdatesLiveData() = runTest(testDispatcher) {
        val useCaseResponse = RemoteResult.OnError<String>("S")
        coEvery { useCase.deleteTicket(ticket.id) } returns useCaseResponse
        sut.deleteTicket(ticket.id)
        advanceUntilIdle()

        verifySequence {
            observerDeleteTicket.onChanged(UIStateData(loading = true))
            observerDeleteTicket.onChanged(UIStateData(loading = false, message = "S"))
        }

        verify(exactly = 2) { observerDeleteTicket.onChanged(any()) }
    }

}
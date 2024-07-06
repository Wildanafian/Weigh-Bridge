package com.wildan.weighbridge.ui

import androidx.lifecycle.Observer
import com.wildan.weighbridge.core.model.SortFilterOption
import com.wildan.weighbridge.core.model.TicketItem
import com.wildan.weighbridge.core.model.base.RemoteResult
import com.wildan.weighbridge.core.model.base.UIStateData
import com.wildan.weighbridge.domain.FilterAndSortTicketsUseCase
import com.wildan.weighbridge.domain.TicketDataUseCase
import com.wildan.weighbridge.ui.helper.BaseTestInstantTaskExecutorRule
import com.wildan.weighbridge.ui.viewmodel.TicketViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifySequence
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Test

/*
 * Created by Wildan Nafian on 7/6/24.
 * Github https://github.com/Wildanafian
 * wildanafian8@gmail.com
 */

@ExperimentalCoroutinesApi
class TicketViewModelTest : BaseTestInstantTaskExecutorRule() {

    private val ticketUseCase: TicketDataUseCase = mockk()
    private val filterAnSortUseCase: FilterAndSortTicketsUseCase = mockk()

    private val sut = TicketViewModel(ticketUseCase, filterAnSortUseCase)
    private val observerTicketList: Observer<UIStateData<List<TicketItem>>> = mockk(relaxUnitFun = true)

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
    fun getTicketList_onSuccess_updatesLiveDataWithTicketList() = runBlocking {
        val useCaseResponse = RemoteResult.OnSuccess(ticketList)
        coEvery { ticketUseCase.getTicketList() } returns flowOf(useCaseResponse)
        sut.ticketList.observeForever(observerTicketList)
        sut.getTicketList()

        verifySequence {
            observerTicketList.onChanged(UIStateData(loading = true))
            observerTicketList.onChanged(UIStateData(loading = false, data = ticketList))
        }

        verify(exactly = 2) { observerTicketList.onChanged(any()) }

        sut.ticketList.removeObserver(observerTicketList)
    }

    @Test
    fun getTicketList_onError_updatesLiveDataWithErrorMessage() = runBlocking {
        val useCaseResponse = RemoteResult.OnError(data = ticketList, message = "s")
        coEvery { ticketUseCase.getTicketList() } returns flowOf(useCaseResponse)
        sut.ticketList.observeForever(observerTicketList)
        sut.getTicketList()

        verifySequence {
            observerTicketList.onChanged(UIStateData(loading = true))
            observerTicketList.onChanged(UIStateData(loading = false, data = ticketList, message = "s"))
        }

        verify(exactly = 2) { observerTicketList.onChanged(any()) }

        sut.ticketList.removeObserver(observerTicketList)
    }

    @Test
    fun getSortedList_givenSortOptionDate_sortsListByDate() = runBlocking {
        val useCaseResponse = RemoteResult.OnSuccess(ticketList)
        coEvery { ticketUseCase.getTicketList() } returns flowOf(useCaseResponse)
        coEvery { filterAnSortUseCase.sortByDate(ticketList) } returns ticketList
        sut.getTicketList()
        delay(200)

        sut.getSortedList(SortFilterOption.DATE)
        delay(200)

        val expected = listOf("23ih8dd", "dnjdic0", "zcvope")
        val result = sut.ticketList.value?.data?.map { it.id }
        assertEquals(expected, result)
        coVerify { filterAnSortUseCase.sortByDate(ticketList) }
    }

    @Test
    fun getSortedList_givenSortOptionDriverName_sortsListByDriverName() = runBlocking {
        val expectedOrder = listOf(
            TicketItem(
                id = "zcvope",
                driverName = "Dedi",
            ),
            TicketItem(
                id = "23ih8dd",
                driverName = "Joko",
            ),
            TicketItem(
                id = "dnjdic0",
                driverName = "Karima",
            ),
        )

        val useCaseResponse = RemoteResult.OnSuccess(ticketList)
        coEvery { ticketUseCase.getTicketList() } returns flowOf(useCaseResponse)
        coEvery { filterAnSortUseCase.sortByDriverName(ticketList) } returns expectedOrder
        sut.getTicketList()
        delay(200)

        sut.getSortedList(SortFilterOption.DRIVER_NAME)
        delay(200)

        val expected = listOf("zcvope", "23ih8dd","dnjdic0")
        val result = sut.ticketList.value?.data?.map { it.id }
        assertEquals(expected, result)
        coVerify { filterAnSortUseCase.sortByDriverName(ticketList) }
    }

    @Test
    fun getSortedList_givenSortOptionLicenseNumber_sortsListByLicenseNumber() = runBlocking {
        val useCaseResponse = RemoteResult.OnSuccess(ticketList)
        coEvery { ticketUseCase.getTicketList() } returns flowOf(useCaseResponse)
        coEvery { filterAnSortUseCase.sortByLicenseNumber(ticketList) } returns ticketList
        sut.getTicketList()
        delay(200)

        sut.getSortedList(SortFilterOption.LICENSE_NUMBER)
        delay(200)

        val expected = listOf("23ih8dd", "dnjdic0", "zcvope")
        val result = sut.ticketList.value?.data?.map { it.id }
        assertEquals(expected, result)
        coVerify { filterAnSortUseCase.sortByLicenseNumber(ticketList) }
    }

    @Test
    fun getSortedList_givenNoSortOptionLicenseNumber_returnsFullTicketList() = runBlocking {
        val useCaseResponse = RemoteResult.OnSuccess(ticketList)
        coEvery { ticketUseCase.getTicketList() } returns flowOf(useCaseResponse)
        sut.getSortedList(SortFilterOption.NO_SORT_FILTER)
        delay(200)
        verify { ticketUseCase.getTicketList() }
    }

    @Test
    fun getFilteredList_givenFilterOptionDate_filtersListByDate() = runBlocking {
        val useCaseResponse = RemoteResult.OnSuccess(ticketList)
        val keyword = "11/11/11"
        coEvery { ticketUseCase.getTicketList() } returns flowOf(useCaseResponse)
        coEvery { filterAnSortUseCase.filterByDate(ticketList, keyword) } returns listOf(ticketList[0])
        sut.getTicketList()
        delay(200)

        sut.getFilteredList(SortFilterOption.DATE, keyword)
        delay(200)

        val expected = listOf(ticketList[0])
        val result = sut.ticketList.value?.data
        assertEquals(expected, result)
        coVerify { filterAnSortUseCase.filterByDate(ticketList, keyword) }
    }

    @Test
    fun getFilteredList_givenFilterOptionDriverName_filtersListByDriverName() = runBlocking {
        val useCaseResponse = RemoteResult.OnSuccess(ticketList)
        val keyword = "Joko"
        coEvery { ticketUseCase.getTicketList() } returns flowOf(useCaseResponse)
        coEvery { filterAnSortUseCase.filterByDriverName(ticketList, keyword) } returns listOf(ticketList[0])
        sut.getTicketList()
        delay(200)

        sut.getFilteredList(SortFilterOption.DRIVER_NAME, keyword)
        delay(200)

        val expected = listOf(ticketList[0])
        val result = sut.ticketList.value?.data
        assertEquals(expected, result)
        coVerify { filterAnSortUseCase.filterByDriverName(ticketList, keyword) }
    }

    @Test
    fun getFilteredList_givenFilterOptionLicenseNumber_filtersListByLicenseNumber() = runBlocking {
        val useCaseResponse = RemoteResult.OnSuccess(ticketList)
        val keyword = "AB 1234 YY"
        coEvery { ticketUseCase.getTicketList() } returns flowOf(useCaseResponse)
        coEvery { filterAnSortUseCase.filterByLicenseNumber(ticketList, keyword) } returns listOf(ticketList[1], ticketList[2])
        sut.getTicketList()
        delay(200)

        sut.getFilteredList(SortFilterOption.LICENSE_NUMBER, keyword)
        delay(200)

        val expected = listOf(ticketList[1], ticketList[2])
        val result = sut.ticketList.value?.data
        assertEquals(expected, result)
        coVerify { filterAnSortUseCase.filterByLicenseNumber(ticketList, keyword) }
    }

    @Test
    fun getFilteredList_givenNoSortFilterOption_returnsFullTicketList() = runBlocking {
        val useCaseResponse = RemoteResult.OnSuccess(ticketList)
        coEvery { ticketUseCase.getTicketList() } returns flowOf(useCaseResponse)
        sut.getFilteredList(SortFilterOption.NO_SORT_FILTER, "")
        delay(200)
        verify { ticketUseCase.getTicketList() }
    }

}
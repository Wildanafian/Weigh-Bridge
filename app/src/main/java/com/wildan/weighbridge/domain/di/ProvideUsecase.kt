package com.wildan.weighbridge.domain.di

import com.wildan.weighbridge.domain.FilterAndSortTicketsUseCase
import com.wildan.weighbridge.domain.FilterAndSortTicketsUseCaseImpl
import com.wildan.weighbridge.domain.TicketDataUseCase
import com.wildan.weighbridge.domain.TicketDataUseCaseImpl
import com.wildan.weighbridge.domain.TicketManagementUseCase
import com.wildan.weighbridge.domain.TicketManagementUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ProvideUsecase {

    @Binds
    abstract fun provideUsecase(repository: FilterAndSortTicketsUseCaseImpl): FilterAndSortTicketsUseCase

    @Binds
    abstract fun provideTicketUsecase(repository: TicketDataUseCaseImpl): TicketDataUseCase

    @Binds
    abstract fun provideTicketManagementUseCase(repository: TicketManagementUseCaseImpl): TicketManagementUseCase

}
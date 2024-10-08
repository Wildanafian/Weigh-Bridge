package com.wildan.weighbridge.data.di

import com.wildan.weighbridge.data.repository.TicketDataRepository
import com.wildan.weighbridge.data.repository.TicketDataRepositoryImpl
import com.wildan.weighbridge.data.repository.TicketManagementRepository
import com.wildan.weighbridge.data.repository.TicketManagementRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ProvideRepository {

    @Binds
    abstract fun provideTicketManagementRepository(repository: TicketManagementRepositoryImpl): TicketManagementRepository

    @Binds
    abstract fun provideTicketDataRepository(repository: TicketDataRepositoryImpl): TicketDataRepository

}
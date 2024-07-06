package com.wildan.weighbridge.data.di

import com.wildan.weighbridge.data.datasource.local.PendingActionManager
import com.wildan.weighbridge.data.datasource.local.PendingActionManagerImpl
import com.wildan.weighbridge.data.datasource.local.TicketManager
import com.wildan.weighbridge.data.datasource.local.TicketManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ProvideLocalSource {

    @Binds
    abstract fun provideTicketManager(local: TicketManagerImpl): TicketManager

    @Binds
    abstract fun providePendingActionManager(local: PendingActionManagerImpl): PendingActionManager

}
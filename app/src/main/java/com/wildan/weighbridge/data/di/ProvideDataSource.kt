package com.wildan.weighbridge.data.di

import com.wildan.weighbridge.data.datasource.local.PendingActionManager
import com.wildan.weighbridge.data.datasource.local.PendingActionManagerImpl
import com.wildan.weighbridge.data.datasource.local.TicketManager
import com.wildan.weighbridge.data.datasource.local.TicketManagerImpl
import com.wildan.weighbridge.data.datasource.remote.FirebaseRemote
import com.wildan.weighbridge.data.datasource.remote.FirebaseRemoteImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ProvideDataSource {

    @Binds
    abstract fun provideTicketManager(local: TicketManagerImpl): TicketManager

    @Binds
    abstract fun providePendingActionManager(local: PendingActionManagerImpl): PendingActionManager

    @Binds
    abstract fun provideFirebaseRemote(remote: FirebaseRemoteImpl): FirebaseRemote

}
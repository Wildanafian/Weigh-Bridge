package com.wildan.weighbridge.core.firebase.di

import com.wildan.weighbridge.core.firebase.FirebaseManagerImpl
import com.wildan.weighbridge.data.datasource.remote.FirebaseManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ProvideFirebaseManager {

    @Binds
    abstract fun provideFirebaseManager(firebaseManagerImpl: FirebaseManagerImpl): FirebaseManager

}
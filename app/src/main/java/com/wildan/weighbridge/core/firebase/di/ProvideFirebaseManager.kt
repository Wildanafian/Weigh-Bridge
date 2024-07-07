package com.wildan.weighbridge.core.firebase.di

import com.wildan.weighbridge.core.firebase.FirebaseManager
import com.wildan.weighbridge.core.firebase.FirebaseManagerImpl
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
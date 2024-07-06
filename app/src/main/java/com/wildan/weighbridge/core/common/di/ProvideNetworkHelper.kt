package com.wildan.weighbridge.core.common.di

import com.wildan.weighbridge.core.common.network.NetworkChecker
import com.wildan.weighbridge.core.common.network.NetworkCheckerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ProvideNetworkHelper {

    @Binds
    abstract fun provideNetworkChecker(network: NetworkCheckerImpl): NetworkChecker

}
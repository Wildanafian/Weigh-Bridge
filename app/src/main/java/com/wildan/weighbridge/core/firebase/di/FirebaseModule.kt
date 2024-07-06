package com.wildan.weighbridge.core.firebase.di

import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    private const val FIREBASE_URL = "https://weighbridge-167b1-default-rtdb.asia-southeast1.firebasedatabase.app/"
    private const val FIREBASE_CHILD = "ticket"

    @Provides
    @Singleton
    fun provideFirebaseDatabase(): FirebaseDatabase {
        return FirebaseDatabase.getInstance(FIREBASE_URL)
    }

    @Provides
    @Singleton
    fun provideChildReference(firebaseDatabase: FirebaseDatabase) =
        firebaseDatabase.reference.child(FIREBASE_CHILD)

}
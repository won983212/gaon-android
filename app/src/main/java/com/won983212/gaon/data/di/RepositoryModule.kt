package com.won983212.gaon.data.di

import com.won983212.gaon.data.repository.PairingRepository
import com.won983212.gaon.data.repository.impl.PairingRepositoryImpl
import com.won983212.gaon.data.source.socket.SocketPairingDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class RepositoryModule {

    @Singleton
    @Provides
    fun provideCodeRepository(
        codeDataSource: SocketPairingDataSource
    ): PairingRepository {
        return PairingRepositoryImpl(codeDataSource)
    }
}
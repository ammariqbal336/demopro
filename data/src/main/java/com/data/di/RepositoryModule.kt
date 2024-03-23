package com.data.di

import com.data.repository.home.remote.HomeRepositoryImpl
import com.domain.repository.HomeRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn (SingletonComponent::class)
abstract class RepositoryModule {



    @Binds
    @Singleton
    abstract fun provideHomeRepository(homeRepository: HomeRepositoryImpl):HomeRepository
}
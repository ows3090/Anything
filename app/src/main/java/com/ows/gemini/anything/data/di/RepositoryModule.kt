package com.ows.gemini.anything.data.di

import com.ows.gemini.anything.data.repository.ImageRepository
import com.ows.gemini.anything.data.repository.ImageRepositoryImpl
import com.ows.gemini.anything.data.repository.LocalRepository
import com.ows.gemini.anything.data.repository.LocalRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds
    @Singleton
    fun bindLocalRepository(localRepositoryImpl: LocalRepositoryImpl): LocalRepository

    @Binds
    @Singleton
    fun bindImageRepository(imageRepositoryImpl: ImageRepositoryImpl): ImageRepository
}

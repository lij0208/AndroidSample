package com.liz.data.di

import com.liz.data.repository.NaverBlogSearchRepositoryImpl
import com.liz.domain.repository.SearchRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindSearchRepository(repository: NaverBlogSearchRepositoryImpl): SearchRepository
}
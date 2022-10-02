package com.example.kudata.repository.datasource

import com.example.kudata.repository.datasource.papago.PapagoDatasource
import com.example.kudata.repository.datasource.papago.PapagoDatasourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun providePapagoDatasource() : PapagoDatasource
            = PapagoDatasourceImpl()
}
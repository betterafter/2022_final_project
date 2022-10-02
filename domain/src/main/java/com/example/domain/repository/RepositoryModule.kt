package com.example.domain.repository

import com.example.kudata.repository.PapagoRepository
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
    fun providePapagoRepository(
        papagoDatasourceImpl: PapagoDatasourceImpl
    ) : PapagoRepository
            = PapagoRepositoryImpl(papagoDatasourceImpl)
}
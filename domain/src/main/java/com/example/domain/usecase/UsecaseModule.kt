package com.example.domain.usecase

import com.example.domain.repository.PapagoRepositoryImpl
import com.example.domain.usecase.papago.PapagoUsecase
import com.example.domain.usecase.papago.PapagoUsecaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UsecaseModule {

    @Provides
    @Singleton
    fun providePapagoUsecase(
        papagoRepositoryImpl: PapagoRepositoryImpl
    ) : PapagoUsecase
            = PapagoUsecaseImpl(papagoRepositoryImpl)
}
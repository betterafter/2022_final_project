package com.example.domain.repository

import com.example.kudata.repository.LoginRepository
import com.example.kudata.repository.PapagoRepository
import com.example.kudata.repository.datasource.login.FacebookLoginDatasourceImpl
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

    @Provides
    @Singleton
    fun provideLgoinRepository(
        facebookLoginDatasourceImpl: FacebookLoginDatasourceImpl
    ) : LoginRepository
            = LoginRepositoryImpl(facebookLoginDatasourceImpl)
}
package com.example.domain.repository

import com.example.kudata.repository.LoginRepository
import com.example.kudata.repository.PapagoRepository
import com.example.kudata.repository.UserRepository
import com.example.kudata.repository.datasource.login.FacebookLoginDatasourceImpl
import com.example.kudata.repository.datasource.login.GoogleLoginDatasourceImpl
import com.example.kudata.repository.datasource.papago.PapagoDatasourceImpl
import com.example.kudata.repository.datasource.user.UserDatasourceImpl
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
        googleLoginDatasourceImpl: GoogleLoginDatasourceImpl,
        facebookLoginDatasourceImpl: FacebookLoginDatasourceImpl
    ) : LoginRepository
            = LoginRepositoryImpl(googleLoginDatasourceImpl, facebookLoginDatasourceImpl)

    @Provides
    @Singleton
    fun provideUserRepository(
        userDatasourceImpl: UserDatasourceImpl
    ) : UserRepository
            = UserRepositoryImpl(userDatasourceImpl)
}
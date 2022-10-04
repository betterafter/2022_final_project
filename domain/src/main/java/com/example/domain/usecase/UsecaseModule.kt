package com.example.domain.usecase

import com.example.domain.repository.LoginRepositoryImpl
import com.example.domain.repository.PapagoRepositoryImpl
import com.example.domain.repository.UserRepositoryImpl
import com.example.domain.usecase.login.LoginUsecase
import com.example.domain.usecase.login.LoginUsecaseImpl
import com.example.domain.usecase.papago.PapagoUsecase
import com.example.domain.usecase.papago.PapagoUsecaseImpl
import com.example.domain.usecase.user.UserUsecase
import com.example.domain.usecase.user.UserUsecaseImpl
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

    @Provides
    @Singleton
    fun provideLoginUsecase(
        loginRepositoryImpl: LoginRepositoryImpl,
        userRepositoryImpl: UserRepositoryImpl
    ) : LoginUsecase
            = LoginUsecaseImpl(loginRepositoryImpl, userRepositoryImpl)

    @Provides
    @Singleton
    fun provideUserUsecase(
        userRepositoryImpl: UserRepositoryImpl
    ) : UserUsecase
            = UserUsecaseImpl(userRepositoryImpl)
}
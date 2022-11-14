package com.example.domain.usecase

import com.example.domain.repository.ChatRepositoryUmpl
import com.example.domain.repository.DashboardRepositoryImpl
import com.example.domain.repository.LoginRepositoryImpl
import com.example.domain.repository.MlKitRepositoryImpl
import com.example.domain.repository.PapagoRepositoryImpl
import com.example.domain.repository.UserRepositoryImpl
import com.example.domain.usecase.chat.ChatUsecase
import com.example.domain.usecase.chat.ChatUsecaseImpl
import com.example.domain.usecase.dashboard.DashboardUsecase
import com.example.domain.usecase.dashboard.DashboardUsecaseImpl
import com.example.domain.usecase.login.LoginUsecase
import com.example.domain.usecase.login.LoginUsecaseImpl
import com.example.domain.usecase.papago.TranslateUsecase
import com.example.domain.usecase.papago.TranslateUsecaseImpl
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
        papagoRepositoryImpl: PapagoRepositoryImpl,
        mlKitRepositoryImpl: MlKitRepositoryImpl
    ) : TranslateUsecase
            = TranslateUsecaseImpl(papagoRepositoryImpl, mlKitRepositoryImpl)

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
        userRepositoryImpl: UserRepositoryImpl,
        dashboardRepositoryImpl: DashboardRepositoryImpl
    ) : UserUsecase
            = UserUsecaseImpl(userRepositoryImpl, dashboardRepositoryImpl)

    @Provides
    @Singleton
    fun provideChatUsecase(
        loginRepositoryImpl: LoginRepositoryImpl,
        chatRepositoryUmpl: ChatRepositoryUmpl
    ) : ChatUsecase
            = ChatUsecaseImpl(loginRepositoryImpl, chatRepositoryUmpl)

    @Provides
    @Singleton
    fun provideDashboardUsecase(
        dashboardRepositoryImpl: DashboardRepositoryImpl
    ) : DashboardUsecase
            = DashboardUsecaseImpl(dashboardRepositoryImpl)
}
package com.example.domain.repository

import com.example.kudata.repository.ChatRepository
import com.example.kudata.repository.DashboardRepository
import com.example.kudata.repository.LoginRepository
import com.example.kudata.repository.MlKitRepository
import com.example.kudata.repository.PapagoRepository
import com.example.kudata.repository.UserRepository
import com.example.kudata.repository.datasource.chat.ChatDataSourceImpl
import com.example.kudata.repository.datasource.dashboard.DashboardDatasourceImpl
import com.example.kudata.repository.datasource.fcm.FcmDatasourceImpl
import com.example.kudata.repository.datasource.login.FacebookLoginDatasourceImpl
import com.example.kudata.repository.datasource.login.GoogleLoginDatasourceImpl
import com.example.kudata.repository.datasource.mltranslator.MlKitDatasourceImpl
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
    ): PapagoRepository = PapagoRepositoryImpl(papagoDatasourceImpl)

    @Provides
    @Singleton
    fun provideLgoinRepository(
        googleLoginDatasourceImpl: GoogleLoginDatasourceImpl,
        facebookLoginDatasourceImpl: FacebookLoginDatasourceImpl
    ): LoginRepository = LoginRepositoryImpl(googleLoginDatasourceImpl, facebookLoginDatasourceImpl)

    @Provides
    @Singleton
    fun provideUserRepository(
        userDatasourceImpl: UserDatasourceImpl
    ): UserRepository = UserRepositoryImpl(userDatasourceImpl)

    @Provides
    @Singleton
    fun provideChatRepository(
        chatDataSourceImpl: ChatDataSourceImpl,
        fcmDatasourceImpl: FcmDatasourceImpl
    ): ChatRepository = ChatRepositoryUmpl(chatDataSourceImpl, fcmDatasourceImpl)

    @Provides
    @Singleton
    fun provideDashboardRepository(
        dashboardDatasourceImpl: DashboardDatasourceImpl
    ): DashboardRepository = DashboardRepositoryImpl(dashboardDatasourceImpl)

    @Provides
    @Singleton
    fun provideMlKitRepository(
        mlKitDatasourceImpl: MlKitDatasourceImpl
    ): MlKitRepository = MlKitRepositoryImpl(mlKitDatasourceImpl)
}
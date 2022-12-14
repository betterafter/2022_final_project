package com.example.kudata.repository.datasource

import android.content.Context
import com.example.kudata.repository.datasource.chat.ChatDataSource
import com.example.kudata.repository.datasource.chat.ChatDataSourceImpl
import com.example.kudata.repository.datasource.dashboard.DashboardDatasource
import com.example.kudata.repository.datasource.dashboard.DashboardDatasourceImpl
import com.example.kudata.repository.datasource.fcm.FcmDatasource
import com.example.kudata.repository.datasource.fcm.FcmDatasourceImpl
import com.example.kudata.repository.datasource.login.FacebookLoginDatasource
import com.example.kudata.repository.datasource.login.FacebookLoginDatasourceImpl
import com.example.kudata.repository.datasource.login.GoogleLoginDatasource
import com.example.kudata.repository.datasource.login.GoogleLoginDatasourceImpl
import com.example.kudata.repository.datasource.mltranslator.MlKitDatasource
import com.example.kudata.repository.datasource.mltranslator.MlKitDatasourceImpl
import com.example.kudata.repository.datasource.papago.PapagoDatasource
import com.example.kudata.repository.datasource.papago.PapagoDatasourceImpl
import com.example.kudata.repository.datasource.user.UserDatasource
import com.example.kudata.repository.datasource.user.UserDatasourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun providePapagoDatasource(): PapagoDatasource = PapagoDatasourceImpl()

    @Provides
    @Singleton
    fun provideGoogleLoginDatasource(@ApplicationContext context: Context): GoogleLoginDatasource =
        GoogleLoginDatasourceImpl(context = context)

    @Provides
    @Singleton
    fun provideFacebookLoginDatasource(): FacebookLoginDatasource = FacebookLoginDatasourceImpl()

    @Provides
    @Singleton
    fun provideChatDatasource(): ChatDataSource = ChatDataSourceImpl()

    @Provides
    @Singleton
    fun provideUserDatasource(): UserDatasource = UserDatasourceImpl()

    @Provides
    @Singleton
    fun provideDashboardDatasource(): DashboardDatasource = DashboardDatasourceImpl()

    @Provides
    @Singleton
    fun provideMlKitDatasource(): MlKitDatasource = MlKitDatasourceImpl()

    @Provides
    @Singleton
    fun provideFcmDatasource(): FcmDatasource = FcmDatasourceImpl()
}
package com.example.kudata.repository.datasource

import android.content.Context
import com.example.kudata.repository.datasource.login.FacebookLoginDatasource
import com.example.kudata.repository.datasource.login.FacebookLoginDatasourceImpl
import com.example.kudata.repository.datasource.login.GoogleLoginDatasource
import com.example.kudata.repository.datasource.login.GoogleLoginDatasourceImpl
import com.example.kudata.repository.datasource.papago.PapagoDatasource
import com.example.kudata.repository.datasource.papago.PapagoDatasourceImpl
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
    fun providePapagoDatasource() : PapagoDatasource
            = PapagoDatasourceImpl()

    @Provides
    @Singleton
    fun provideGoogleLoginDatasource(@ApplicationContext context: Context) : GoogleLoginDatasource
            = GoogleLoginDatasourceImpl(context = context)

    @Provides
    @Singleton
    fun provideFacebookLoginDatasource() : FacebookLoginDatasource
            = FacebookLoginDatasourceImpl()
}
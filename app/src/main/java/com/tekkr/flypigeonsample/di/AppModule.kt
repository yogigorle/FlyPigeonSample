package com.tekkr.flypigeonsample.di

import android.content.Context
import com.tekkr.flypigeonsample.FlyPigeonSampleApplication
import com.tekkr.flypigeonsample.data.network.ApiInterface
import com.tekkr.flypigeonsample.data.network.WebServices
import com.tekkr.flypigeonsample.utils.DataStoreManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Singleton
    @Provides
    fun provideApplicationContext(): FlyPigeonSampleApplication =
        FlyPigeonSampleApplication.appContext!!

    @Singleton
    @Provides
    fun provideApiInterfaceRef(): ApiInterface =
        WebServices.retrofit.create(ApiInterface::class.java)

    @Singleton
    @Provides
    fun provideDataStoreManagerRef(@ApplicationContext appContext: Context): DataStoreManager = DataStoreManager(appContext)


}
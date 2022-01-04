package com.tekkr.flypigeonsample.di

import com.tekkr.flypigeonsample.FlyPigeonSampleApplication
import com.tekkr.flypigeonsample.data.network.ApiInterface
import com.tekkr.flypigeonsample.data.network.WebServices
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object AppModule {


    @Singleton
    @Provides
    fun provideApplicationContext(): FlyPigeonSampleApplication =
        FlyPigeonSampleApplication.appContenxt!!

    @Singleton
    @Provides
    fun provideApiInterfaceRef(): ApiInterface =
        WebServices.retrofit.create(ApiInterface::class.java)


}
package com.tekkr.flypigeonsample.di

import com.tekkr.flypigeonsample.data.network.ApiInterface
import com.tekkr.flypigeonsample.data.repositories.FlightsSearchRepo
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object RepositoriesModule {

    @Singleton
    @Provides
    fun provideFlightsSearchResultsRepo(
        apiInterface: ApiInterface
    ) = FlightsSearchRepo(apiInterface)
}
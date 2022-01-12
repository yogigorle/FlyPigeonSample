package com.tekkr.flypigeonsample.di

import com.tekkr.flypigeonsample.data.network.ApiInterface
import com.tekkr.flypigeonsample.data.repositories.FlightBookingRepo
import com.tekkr.flypigeonsample.data.repositories.FlightsSearchRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoriesModule {

    @Singleton
    @Provides
    fun provideFlightsSearchResultsRepo(
        apiInterface: ApiInterface
    ): FlightsSearchRepo = FlightsSearchRepo(apiInterface)


    @Singleton
    @Provides
    fun provideFlightBookingRepo(
        apiInterface: ApiInterface
    ): FlightBookingRepo = FlightBookingRepo(apiInterface)
}

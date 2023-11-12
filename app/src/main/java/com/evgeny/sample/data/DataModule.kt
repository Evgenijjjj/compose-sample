package com.evgeny.sample.data

import com.evgeny.sample.data.api.BeerRetrofitApi
import com.evgeny.sample.data.repository.BeerRepository
import com.evgeny.sample.data.repository.BeerRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @[Binds Singleton]
    fun bindBeerRepository(impl: BeerRepositoryImpl): BeerRepository

    companion object {
        @[Provides Singleton]
        fun provideRetrofit(): Retrofit {
            return Retrofit.Builder()
                .baseUrl("https://api.punkapi.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(OkHttpClient.Builder().build())
                .build()
        }

        @[Provides Singleton]
        fun provideBeerApi(
            retrofit: Retrofit,
        ): BeerRetrofitApi {
            return retrofit.create(BeerRetrofitApi::class.java)
        }
    }
}

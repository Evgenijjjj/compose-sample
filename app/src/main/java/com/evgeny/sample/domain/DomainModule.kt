package com.example.beerapp.domain

import com.evgeny.sample.domain.usecase.GetBeerUseCase
import com.evgeny.sample.domain.usecase.GetBeerUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DomainModule {

    @Binds
    fun bindGetBeerUseCase(impl: GetBeerUseCaseImpl): GetBeerUseCase
}

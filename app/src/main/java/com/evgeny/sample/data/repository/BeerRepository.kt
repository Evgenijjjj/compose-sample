package com.evgeny.sample.data.repository

import com.evgeny.sample.data.api.BeerRetrofitApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.evgeny.sample.data.model.BeerApi as BeerNetworkModel
import javax.inject.Inject

interface BeerRepository {

    suspend fun getBeer(page: Int): Result<List<BeerNetworkModel>>
}

class BeerRepositoryImpl @Inject constructor(
    private val beerApi: BeerRetrofitApi,
) : BeerRepository {

    override suspend fun getBeer(page: Int): Result<List<BeerNetworkModel>> {
        return try {
            withContext(Dispatchers.IO) {
                Result.success(beerApi.getBeer(page))
            }
        } catch (e: Throwable) {
            Result.failure(e)
        }
    }
}

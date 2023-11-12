package com.evgeny.sample.data.api

import com.evgeny.sample.data.model.BeerApi
import retrofit2.http.GET
import retrofit2.http.Query

interface BeerRetrofitApi {

    @GET("v2/beers?per_page=30")
    suspend fun getBeer(
        @Query("page") page: Int
    ): List<BeerApi>
}

package com.evgeny.sample.data.model

import com.google.gson.annotations.SerializedName

data class BeerApi(
    @SerializedName("name")
    val name: String,
    @SerializedName("image_url")
    val imageUrl: String?,
    @SerializedName("description")
    val description: String,
)

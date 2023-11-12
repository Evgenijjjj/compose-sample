package com.evgeny.sample.ui.beerPage

import com.evgeny.sample.domain.model.Beer


data class BeerUiState(
    val items: List<Beer> = listOf(),
    val loading: Boolean = false,
)

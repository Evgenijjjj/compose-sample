package com.evgeny.sample.ui.beerPage

sealed interface BeerUiAction {
    object LoadNextPage : BeerUiAction
    object SortClicked : BeerUiAction
}

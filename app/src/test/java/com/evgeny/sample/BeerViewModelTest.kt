package com.evgeny.sample

import app.cash.turbine.test
import com.evgeny.sample.domain.model.Beer
import com.evgeny.sample.domain.usecase.GetBeerUseCase
import com.evgeny.sample.ui.beerPage.BeerUiAction
import com.evgeny.sample.ui.beerPage.BeerViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BeerViewModelTest {

    private lateinit var viewModel: BeerViewModel
    private lateinit var getBeerUseCase: GetBeerUseCase

    @Before
    fun setup() {
        getBeerUseCase = mockk(relaxed = true)
        viewModel = BeerViewModel(getBeerUseCase)
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @After
    fun clear() {
        Dispatchers.resetMain()
    }

    @Test
    fun `show loader when next page is loading and hide after it is loaded`() = runTest {
        coEvery { getBeerUseCase(any()) } returns Result.success(BEER_MOCK)
        viewModel.uiState.test {
            viewModel.onAction(BeerUiAction.LoadNextPage)
            skipItems(1)
            Assert.assertTrue(awaitItem().loading)
            val contentState = awaitItem()
            Assert.assertFalse(contentState.loading)
            Assert.assertArrayEquals(contentState.items.toTypedArray(), BEER_MOCK.toTypedArray())
        }
    }

    @Test
    fun `sort works fine`() = runTest {
        coEvery { getBeerUseCase(any()) } returns Result.success(BEER_MOCK)

        viewModel.uiState.test {
            viewModel.onAction(BeerUiAction.LoadNextPage)
            skipItems(3) // skip initial, loading, content states
            viewModel.onAction(BeerUiAction.SortClicked)
            val contentState = awaitItem()
            Assert.assertArrayEquals(contentState.items.toTypedArray(), BEER_MOCK.sortedBy { it.name }.toTypedArray())

        }
    }

    private companion object {
        val BEER_MOCK = listOf(
            Beer(
                name = "5Bette Donaldson",
                imageUrl = "https://www.google.com/#q=dolores",
                description = "idque",
            ),
            Beer(
                name = "2Bette Donaldson",
                imageUrl = "https://www.google.com/#q=dolores",
                description = "idque",
            ),
            Beer(
                name = "3Bette Donaldson",
                imageUrl = "https://www.google.com/#q=dolores",
                description = "idque",
            ),
            Beer(
                name = "4Bette Donaldson",
                imageUrl = "https://www.google.com/#q=dolores",
                description = "idque",
            ),
            Beer(
                name = "1Bette Donaldson",
                imageUrl = "https://www.google.com/#q=dolores",
                description = "idque",
            ),
        )
    }
}


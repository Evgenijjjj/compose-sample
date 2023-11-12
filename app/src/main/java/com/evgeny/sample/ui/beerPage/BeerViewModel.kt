package com.evgeny.sample.ui.beerPage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.evgeny.sample.domain.usecase.GetBeerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class BeerViewModel @Inject constructor(
    private val getBeerUseCase: GetBeerUseCase
) : ViewModel() {

    private var page = 1
    private val _uiState = MutableStateFlow(BeerUiState(loading = false))
    val uiState = _uiState.asStateFlow()

    private val _errorFlow = MutableSharedFlow<String>()
    val errorFlow = _errorFlow.asSharedFlow()

    fun onAction(action: BeerUiAction) {
        when (action) {
            BeerUiAction.LoadNextPage -> loadNextPage()
            BeerUiAction.SortClicked -> sort()
        }
    }

    private fun sort() {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                val state = uiState.value
                _uiState.emit(state.copy(items = state.items.sortedBy { it.name }))
            }
        }
    }

    private fun loadNextPage() {
        val state = uiState.value
        if (state.loading.not()) {
            _uiState.update { state.copy(loading = true) }
            viewModelScope.launch {
                getBeerUseCase(page = page)
                    .onSuccess { newItems ->
                        _uiState.emit(
                            BeerUiState(
                                items = state.items + newItems,
                                loading = false,
                            )
                        )
                        page++
                    }
                    .onFailure {
                        _uiState.update { state.copy(loading = false) }
                        it.printStackTrace()
                        it.message?.let {
                            _errorFlow.emit(it)
                        }
                    }
            }
        }
    }
}

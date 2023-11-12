package com.evgeny.sample.ui.beerPage

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.evgeny.sample.domain.model.Beer
import com.evgeny.sample.ui.base.theme.SampleAppTheme

@Composable
fun BeerPage(
    viewModel: BeerViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    BeerPage(state = state, onAction = viewModel::onAction)

    LaunchedEffect(Unit) {
        viewModel.onAction(BeerUiAction.LoadNextPage)
    }

    LaunchedEffect(Unit) {
        viewModel.errorFlow.collect {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BeerPage(
    state: BeerUiState,
    onAction: (BeerUiAction) -> Unit,
) {
    val listState = rememberLazyListState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Beer Sample App")
                },
                scrollBehavior = scrollBehavior,
            )
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding), state = listState) {
            items(items = state.items, key = { it.name }) {
                BeerItem(item = it)
            }

            item {
                if (state.loading && state.items.isNotEmpty()) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp))
                    }
                }
            }
        }

        if (state.loading && state.items.isEmpty()) {
            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp))
            }
        }
    }

    LaunchedEffect(listState, state) {
        snapshotFlow { listState.layoutInfo }.collect {
            val lastItemKey = it.visibleItemsInfo.lastOrNull()?.key
            val shouldPaginate = state.items.isNotEmpty() && state.loading.not() &&
                    lastItemKey == state.items.lastOrNull()?.name

            if (shouldPaginate) {
                onAction(BeerUiAction.LoadNextPage)
            }
        }
    }
}

@Composable
private fun BeerItem(
    item: Beer,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = item.name,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            fontSize = 20.sp,
            color = Color.Black,
            maxLines = 1,
        )
        Text(
            text = item.description,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .alpha(0.8f),
            fontSize = 18.sp,
            color = Color.Black,
            maxLines = 3
        )
    }
}

private class BeerPagePreview : PreviewParameterProvider<BeerUiState> {
    override val values: Sequence<BeerUiState>
        get() = sequenceOf(
            BeerUiState(items = listOf(), loading = true),
            BeerUiState(
                items = listOf(
                    Beer(
                        name = "Lea Underwood",
                        imageUrl = "http://www.bing.com/search?q=nihil",
                        description = "fuisset"
                    ),
                    Beer(
                        name = "Bridgette Hood",
                        imageUrl = "http://www.bing.com/search?q=legere",
                        description = "has"
                    ),
                    Beer(
                        name = "Maura Morales",
                        imageUrl = "https://search.yahoo.com/search?p=suas",
                        description = "alienum"
                    )
                ), loading = true
            )
        )
}

@Composable
@Preview
private fun BeerPagePreview(
    @PreviewParameter(BeerPagePreview::class) state: BeerUiState
) {
    SampleAppTheme {
        BeerPage(state = state, onAction = {})
    }
}

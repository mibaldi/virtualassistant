package com.mibaldi.virtualassistant.ui.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.mibaldi.virtualassistant.MyAppComposable
import com.mibaldi.virtualassistant.R
import com.mibaldi.virtualassistant.domain.Event
import com.mibaldi.virtualassistant.ui.common.MainAppBar
import com.mibaldi.virtualassistant.ui.common.errorToString


@OptIn(ExperimentalMaterial3Api::class)
@ExperimentalFoundationApi
@Composable
fun MainScreen(onNavigate: (Int) -> Unit,logout:()->Unit) {
    MyAppComposable {
        Scaffold(
            topBar = { MainAppBar(stringResource(id = R.string.app_name), logout = { logout() }) }
        ) { padding ->
            MainContent(onNavigate = onNavigate, modifier = Modifier.padding(padding))
        }
    }
}

@ExperimentalFoundationApi
@Composable
fun MainContent(modifier: Modifier = Modifier,vm: MainViewModel = hiltViewModel(),onNavigate: (Int) -> Unit, ) {
    EventList(
        onClick = { onNavigate(it.id) },
        modifier = modifier
    )
    val state by vm.state.collectAsState()
    state.error?.let {
        Text(text = LocalContext.current.errorToString(it))
    }
}
@ExperimentalFoundationApi
@Composable
fun EventList(
    modifier: Modifier = Modifier,
    vm: MainViewModel = hiltViewModel(),
    onClick: (Event) -> Unit,
) {
    val state by vm.state.collectAsState()
    val list = state.events ?: emptyList()
    LazyVerticalGrid(
        columns = GridCells.Adaptive(dimensionResource(R.dimen.cell_min_width)),
        contentPadding = PaddingValues(dimensionResource(R.dimen.padding_xsmall)),
        modifier = modifier
    ) {
        items(list) {
            EventListItem(
                eventItem = it,
                onClick = { onClick(it) },
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_xsmall))
            )
        }
    }
}

@Composable
fun EventListItem(
    eventItem: Event,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.clickable { onClick() }
    ) {
        Column {
            Thumb(
                eventItem = eventItem,
                modifier = modifier.height(dimensionResource(R.dimen.cell_thumb_height))
            )
            Title(eventItem)
        }
    }
}

@Composable
private fun Title(eventItem: Event) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Blue)
            .padding(dimensionResource(R.dimen.padding_medium))
    ) {
        Text(
            text = eventItem.name,
           // style = MaterialTheme.typography.bodyLarge
        )
    }
}
@Composable
fun Thumb(eventItem: Event, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
    ) {
        AsyncImage(
            model = eventItem.thumb,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}
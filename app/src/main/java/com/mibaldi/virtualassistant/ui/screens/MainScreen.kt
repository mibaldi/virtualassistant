package com.mibaldi.virtualassistant.ui.screens

import android.app.Activity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mibaldi.virtualassistant.R
import com.mibaldi.virtualassistant.domain.Event
import com.mibaldi.virtualassistant.ui.common.GifImage
import com.mibaldi.virtualassistant.ui.common.Thumb
import com.mibaldi.virtualassistant.ui.common.Title
import com.mibaldi.virtualassistant.ui.common.UserViewModel
import com.mibaldi.virtualassistant.ui.common.errorToString
import com.mibaldi.virtualassistant.ui.common.goToHome

import com.mibaldi.virtualassistant.ui.main.MainViewModel






@ExperimentalFoundationApi
@Composable
fun MainScreen(userViewModel: UserViewModel = hiltViewModel(),onNavigate: (Event) -> Unit) {
    val context = LocalContext.current

    userViewModel.isLoggedIn.observe(LocalLifecycleOwner.current){ isLoggedIn ->
        if (!isLoggedIn) {
            // User is logged out, perform necessary actions
            context.goToHome()
            (context as Activity).finish()
        }
    }

    Column {
        GifImage(url="https://firebasestorage.googleapis.com/v0/b/virtualassistant-b1514.appspot.com/o/Conoce_a_SAM_nuestra_asistente_virtual.gif?alt=media&token=4135a9f3-a26f-42c1-8722-febb65249942")
        MainContent(onNavigate = onNavigate)
    }
}

@ExperimentalFoundationApi
@Composable
fun MainContent(modifier: Modifier = Modifier,vm: MainViewModel = hiltViewModel(),onNavigate: (Event) -> Unit ) {
    val context = LocalContext.current as Activity
    LaunchedEffect(Unit){
        vm.getEvents()
    }
    EventList(
        onClick = { onNavigate(it)},
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
    LazyRow(contentPadding = PaddingValues(dimensionResource(R.dimen.padding_xsmall)),
        modifier = modifier) {
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
                itemThumb = eventItem.thumb,
                modifier = modifier.height(dimensionResource(R.dimen.cell_thumb_height))
            )
            Title(eventItem.name,modifier = modifier.width(dimensionResource(R.dimen.cell_thumb_height)))
        }
    }
}


@Preview(showSystemUi = true)
@Composable
fun MainPreview(){
    Card(
        modifier = Modifier
            .clickable { }
            .width(200.dp)
    ) {
        Column {
            Thumb(
                itemThumb = "eventItem.thumb",
                modifier = Modifier.height(dimensionResource(R.dimen.cell_thumb_height)
                )
            )
            Title("eventItem.name")
        }
    }
}

package com.mibaldi.virtualassistant.ui.bookings

import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.mibaldi.virtualassistant.MyAppComposable
import com.mibaldi.virtualassistant.R
import com.mibaldi.virtualassistant.domain.Event
import com.mibaldi.virtualassistant.ui.bookings.ui.theme.Typography
import com.mibaldi.virtualassistant.ui.common.CustomDialog
import com.mibaldi.virtualassistant.ui.common.MainAppBar
import com.mibaldi.virtualassistant.ui.common.Thumb
import com.mibaldi.virtualassistant.ui.common.Title
import com.mibaldi.virtualassistant.ui.common.errorToString


@OptIn(ExperimentalMaterial3Api::class)
@ExperimentalFoundationApi
@Composable
fun BookingScreen(onNavigate: (Int) -> Unit,logout:()->Unit) {
    MyAppComposable {
        Scaffold(
            topBar = { MainAppBar(stringResource(id = R.string.app_name), logout = { logout() }) }
        ) { padding ->
            Column (modifier = Modifier.padding(padding)){
                GifImage(url="https://firebasestorage.googleapis.com/v0/b/virtualassistant-b1514.appspot.com/o/sam-sam-from-samsung.gif?alt=media&token=47bd221f-89ca-4673-b170-70a136af7d16")
                Text(text = "Realizar reservas",
                    modifier = Modifier.fillMaxWidth().padding(10.dp),
                    style = Typography.titleLarge,
                    textAlign = TextAlign.Center)
                BookingContent(onNavigate = onNavigate, modifier = Modifier.padding(10.dp))
            }

        }
    }
}

@Composable
fun GifImage(
    modifier: Modifier = Modifier,
    url:String
) {
    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context)
        .components { add(ImageDecoderDecoder.Factory()) }
        .build()
    Image(
        painter = rememberAsyncImagePainter(
            ImageRequest.Builder(context).data(url).apply(block = {
                size(Size.ORIGINAL)
            }).build(), imageLoader = imageLoader
        ),
        contentDescription = null,
        modifier = modifier.fillMaxWidth(),
    )
}
@ExperimentalFoundationApi
@Composable
fun BookingContent(modifier: Modifier = Modifier, vm: BookingViewModel = hiltViewModel(), onNavigate: (Int) -> Unit, ) {
    BookingList(
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
fun BookingList(
    modifier: Modifier = Modifier,
    vm: BookingViewModel = hiltViewModel(),
    onClick: (Event) -> Unit,
) {
    val openDialogCustom = remember { mutableStateOf(false) }
    val bookingClicked = remember { mutableStateOf<Event>(Event(-1,"")) }

    val state by vm.state.collectAsState()
    val list = state.events ?: emptyList()
    LazyVerticalGrid(
        columns = GridCells.Adaptive(dimensionResource(R.dimen.cell_min_width)),
        contentPadding = PaddingValues(dimensionResource(R.dimen.padding_xsmall)),
        modifier = modifier
    ) {
        items(list) {
            BookingListItem(
                eventItem = it,
                onClick = {
                    openDialogCustom.value = true
                    bookingClicked.value = it
                    onClick(it) },
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_xsmall))
            )
        }
    }
    if (openDialogCustom.value && bookingClicked.value.id != -1){
        CustomDialog(openDialogCustom = openDialogCustom, bookingClicked = bookingClicked)
    }

}


@Composable
fun BookingListItem(
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
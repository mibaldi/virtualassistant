package com.mibaldi.virtualassistant.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.mibaldi.virtualassistant.R
import com.mibaldi.virtualassistant.ui.main.MainViewModel


@Composable
fun Greeting(vm: MainViewModel = hiltViewModel(), onNameClick: (String)-> Unit) {
    val name by vm.name.collectAsState()
    Column(
        Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Hello $name!",
            modifier = Modifier
                .padding(30.dp)
                .clickable { onNameClick(name) }

        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppBar(text: String, onUpClick:(()->Unit)? = null,logout:(()->Unit)? = null) {
    val navIcon:(@Composable () -> Unit)?   =
        if (onUpClick != null) {
            { ArrowBackIcon(onUpClick)}
        } else {
            null
        }
    TopAppBar(
        title = { Text(text) },
        navigationIcon = navIcon ?: {},
        actions = {
            if (logout != null){
                AppBarAction(
                    imageVector = Icons.Default.Logout,
                    onClick = { logout() }
                )
            }
        }
    )
}
@Composable
fun ArrowBackIcon(onUpClick: () -> Unit) {
    IconButton(onClick = onUpClick) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = null
        )
    }
}
@Composable
private fun AppBarAction(
    imageVector: ImageVector,
    onClick: () -> Unit
) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = imageVector,
            contentDescription = null
        )
    }
}
@Composable
fun AdContent(size: AdSize = AdSize.BANNER, id: String = "ca-app-pub-3940256099942544/6300978111"){
    AndroidView(
        // on below line specifying width for ads.
        modifier = Modifier.fillMaxWidth(),
        factory = { context ->
            // on below line specifying ad view.
            AdView(context).apply {
                // on below line specifying ad size
                setAdSize(size)
                // on below line specifying ad unit id
                // currently added a test ad unit id.
                adUnitId = id
                // calling load ad to load our ad.
                loadAd(AdRequest.Builder().build())
            }
        }
    )
}
@Composable
fun Title(itemName: String,modifier :Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .background(Color.Blue)
            .padding(dimensionResource(R.dimen.padding_medium))
    ) {
        Text(text = itemName)
    }
}
@Composable
fun Thumb(itemThumb: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
    ) {

        AsyncImage(
            model = itemThumb,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
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




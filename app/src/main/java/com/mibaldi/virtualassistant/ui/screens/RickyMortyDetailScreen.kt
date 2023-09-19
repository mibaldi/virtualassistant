package com.mibaldi.virtualassistant.ui.screens

import android.app.Activity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.mibaldi.virtualassistant.domain.MyCharacter
import com.mibaldi.virtualassistant.ui.common.GifImage
import com.mibaldi.virtualassistant.ui.common.UserViewModel
import com.mibaldi.virtualassistant.ui.common.goToHome
import com.mibaldi.virtualassistant.ui.rickyMorty.RickyMortyDetailViewModel

@ExperimentalFoundationApi
@Composable
fun RickyMortyDetailScreen(
    userViewModel: UserViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    userViewModel.isLoggedIn.observe(LocalLifecycleOwner.current) { isLoggedIn ->
        if (!isLoggedIn) {
            // User is logged out, perform necessary actions
            context.goToHome()
            (context as Activity).finish()
        }
    }

    Column {
        RickyMortyDetailContent()
    }
}


@Composable
fun RickyMortyDetailContent(viewModel: RickyMortyDetailViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()

    ItemDetailScreen(
        loading = state.loading,
        item = state.character
    )
}

@Composable
fun ItemDetailScreen(loading: Boolean, item: MyCharacter?) {

    if (loading) {
        CircularProgressIndicator()
    }
    if (item != null) {
        Header(marvelItem = item)
    }
}

@Composable
private fun Header(marvelItem: MyCharacter) {
    Column(
        modifier = Modifier.fillMaxWidth().background(Color.White)
    ) {
        AsyncImage(
            model = marvelItem.image,
            contentDescription = marvelItem.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray)
                .aspectRatio(1f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = marvelItem.name,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 0.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = marvelItem.name,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(16.dp, 0.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}
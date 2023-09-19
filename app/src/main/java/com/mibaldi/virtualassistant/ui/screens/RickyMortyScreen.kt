package com.mibaldi.virtualassistant.ui.screens

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.mibaldi.virtualassistant.R
import com.mibaldi.virtualassistant.domain.MyCharacter
import com.mibaldi.virtualassistant.ui.common.UserViewModel
import com.mibaldi.virtualassistant.ui.common.goToHome
import com.mibaldi.virtualassistant.ui.rickyMorty.RickyMortyViewModel
import kotlinx.coroutines.delay

@ExperimentalFoundationApi
@Composable
fun RickyMortyScreen(
    userViewModel: UserViewModel = hiltViewModel(),
    onNavigate: (MyCharacter) -> Unit
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
        RickyMortyContent(onNavigate = onNavigate)
    }
}

@ExperimentalFoundationApi
@Composable
fun RickyMortyContent(
    modifier: Modifier = Modifier,
    vm: RickyMortyViewModel = hiltViewModel(),
    onNavigate: (MyCharacter) -> Unit
) {
    LaunchedEffect(Unit) {
        vm.getCharacters()
    }
    CharacterList(
        onClick = { onNavigate(it) },
        modifier = modifier
    )
}

@Composable
fun CharacterList(
    modifier: Modifier = Modifier,
    vm: RickyMortyViewModel = hiltViewModel(),
    onClick: (MyCharacter) -> Unit,
) {
    val listState = rememberLazyListState()
    var loadingBlocked by remember { mutableStateOf(false) }


    LaunchedEffect(listState.layoutInfo) {
        val totalItems = listState.layoutInfo.totalItemsCount
        val lastVisibleItemIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
        Log.d("LOADING", loadingBlocked.toString())
        if (lastVisibleItemIndex == totalItems - 1 && !loadingBlocked) {
            loadingBlocked = true
            vm.getCharacters()
        }
    }


    val state by vm.state.collectAsState()
    val list = state.characters
    LaunchedEffect(state) {
        delay(1000)
        loadingBlocked = false
    }

    LazyColumn(state = listState) {
        items(list) {
            CharacterListItem(
                myCharacterItem = it,
                onClick = { onClick(it) },
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_xsmall))
            )
        }
    }
}

@Composable
fun CharacterListItem(
    myCharacterItem: MyCharacter,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Imagen en el lado izquierdo
            AsyncImage(
                model = myCharacterItem.image,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(48.dp)
                    .clip(shape = RoundedCornerShape(24.dp))
                    .background(Color.Gray)
                    .padding(4.dp)
                    .shadow(4.dp, shape = RoundedCornerShape(24.dp))
            )

            Spacer(modifier = Modifier.width(16.dp)) // Espaciado entre la imagen y el nombre

            // Nombre centrado verticalmente
            Text(
                text = myCharacterItem.name, // Reemplaza con el nombre deseado
                style = TextStyle(fontSize = 18.sp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        }
    }
}
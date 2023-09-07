package com.mibaldi.virtualassistant.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mibaldi.virtualassistant.R
import com.mibaldi.virtualassistant.domain.InstagramProfile
import com.mibaldi.virtualassistant.ui.common.GifImage
import com.mibaldi.virtualassistant.ui.common.Thumb
import com.mibaldi.virtualassistant.ui.common.Title
import com.mibaldi.virtualassistant.ui.common.theme.SmallDP
import com.mibaldi.virtualassistant.ui.instagrams.InstagramsViewModel


@OptIn(ExperimentalMaterial3Api::class)
@ExperimentalFoundationApi
@Composable
fun InstagramsScreen(onNavigate: (String) -> Unit) {
    /*MyAppComposable {
        Scaffold(
            topBar = { MainAppBar(stringResource(id = R.string.app_name), logout = { logout() }) }
        ) { padding ->


        }
    }*/

    Column {
        GifImage(url="https://firebasestorage.googleapis.com/v0/b/virtualassistant-b1514.appspot.com/o/sam-sam-from-samsung.gif?alt=media&token=47bd221f-89ca-4673-b170-70a136af7d16")
        Text(text = "Cuentas de instagram Importantes",
            modifier = Modifier.fillMaxWidth().padding(SmallDP),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center)
        InstagramsContent(onNavigate = onNavigate, modifier = Modifier.padding(10.dp))
    }
}

@ExperimentalFoundationApi
@Composable
fun InstagramsContent(modifier: Modifier = Modifier, onNavigate: (String) -> Unit, ) {
    InstagramList(
        onClick = { onNavigate(it.name) },
        modifier = modifier
    )
}
@ExperimentalFoundationApi
@Composable
fun InstagramList(
    modifier: Modifier = Modifier,
    vm: InstagramsViewModel = hiltViewModel(),
    onClick: (InstagramProfile) -> Unit,
) {
    vm.getInstagrams()
    val instagramProfileClicked = remember { mutableStateOf(InstagramProfile(-1,"","")) }

    val state by vm.state.collectAsState()
    val list = state.instagrams ?: emptyList()
    LazyVerticalGrid(
        columns = GridCells.Adaptive(dimensionResource(R.dimen.cell_min_width)),
        contentPadding = PaddingValues(dimensionResource(R.dimen.padding_xsmall)),
        modifier = modifier
    ) {
        items(list) {
            InstagramProfileListItem(
                profileItem = it,
                onClick = {
                    instagramProfileClicked.value = it
                    onClick(it) },
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_xsmall))
            )
        }
    }
}

@Composable
fun InstagramProfileListItem(
    profileItem: InstagramProfile,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.clickable { onClick() }
    ) {
        Column {
            Thumb(
                itemThumb = profileItem.thumb,
                modifier = modifier.height(dimensionResource(R.dimen.cell_thumb_height))
            )
            Title(profileItem.name)
        }
    }
}
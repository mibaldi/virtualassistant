package com.mibaldi.virtualassistant.ui.detail

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.mibaldi.virtualassistant.MyAppComposable
import com.mibaldi.virtualassistant.ui.common.Greeting
import com.mibaldi.virtualassistant.ui.common.MainAppBar

@OptIn(ExperimentalMaterial3Api::class)
@ExperimentalFoundationApi
@Composable
fun DetailScreen(onBackPressed: ()->Unit,onClick: (String) -> Unit) {
    MyAppComposable {
        Scaffold(
            topBar = { MainAppBar("Detail Screen",onBackPressed) }
        ) { padding ->
            DetailContent(modifier = Modifier.padding(padding),onClick)
        }
    }
}

@Composable
fun DetailContent(modifier: Modifier,onClick:(String)->Unit){

    Card(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Greeting(onNameClick = onClick)
    }

}
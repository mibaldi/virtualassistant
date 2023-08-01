package com.mibaldi.virtualassistant.ui.common

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
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
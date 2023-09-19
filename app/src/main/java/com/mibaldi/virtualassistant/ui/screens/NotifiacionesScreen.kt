package com.mibaldi.virtualassistant.ui.screens

import android.app.Activity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mibaldi.virtualassistant.R
import com.mibaldi.virtualassistant.domain.Notificacion
import com.mibaldi.virtualassistant.ui.common.UserViewModel
import com.mibaldi.virtualassistant.ui.common.goToHome
import com.mibaldi.virtualassistant.ui.notificaciones.NotificationsViewModel
import kotlinx.coroutines.delay

@ExperimentalFoundationApi
@Composable
fun NotificacionesScreen(
    userViewModel: UserViewModel = hiltViewModel(),
    onNavigate: (Notificacion) -> Unit
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
        NotificacionesContent(onNavigate = onNavigate)
    }
}

@ExperimentalFoundationApi
@Composable
fun NotificacionesContent(
    modifier: Modifier = Modifier,
    vm: NotificationsViewModel = hiltViewModel(),
    onNavigate: (Notificacion) -> Unit
) {
    LaunchedEffect(Unit) {
        vm.getNotifications()
    }
    NotificationList(
        onClick = { onNavigate(it) },
        modifier = modifier
    )
}

@Composable
fun NotificationList(
    modifier: Modifier = Modifier,
    vm: NotificationsViewModel = hiltViewModel(),
    onClick: (Notificacion) -> Unit,
) {
    val listState = rememberLazyListState()
    var loadingBlocked by remember { mutableStateOf(false) }


    LaunchedEffect(listState.layoutInfo) {
        val totalItems = listState.layoutInfo.totalItemsCount
        val lastVisibleItemIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
        if (lastVisibleItemIndex == totalItems - 1 && !loadingBlocked) {
            loadingBlocked = true
            vm.getNotifications()
        }
    }


    val state by vm.state.collectAsState()
    val list = state.notifications
    LaunchedEffect(state) {
        delay(1000)
        loadingBlocked = false
    }

    LazyColumn(state = listState) {
        items(list) {
            NotificationsItem(
                notification = it,
                onClick = { onClick(it) },
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_xsmall))
            )
        }
    }
}

@Composable
fun NotificationsItem(
    notification: Notificacion,
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
            Text(
                text = "Titulo: ${notification.titulo} Cuerpo: ${notification.texto}", // Reemplaza con el nombre deseado
                style = TextStyle(fontSize = 18.sp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        }
    }
}
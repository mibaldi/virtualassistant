package com.mibaldi.virtualassistant.ui

import android.app.Activity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.mibaldi.virtualassistant.R
import com.mibaldi.virtualassistant.ui.common.goToHome
import com.mibaldi.virtualassistant.ui.common.theme.VirtualAssistantTheme
import com.mibaldi.virtualassistant.ui.navigation.AppBarIcon
import com.mibaldi.virtualassistant.ui.navigation.AppBottomNavigation
import com.mibaldi.virtualassistant.ui.navigation.DrawerContent
import com.mibaldi.virtualassistant.ui.navigation.Navigation
import com.mibaldi.virtualassistant.ui.navigation.VirtualAssistantTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VirtualAssistantApp(appState: VirtualAssistantState = rememberVirtualAssistantState()) {
    val logged = appState.isLoggedIn.isLogged
    val context = LocalContext.current
    // Crea una instancia de ScaffoldState
    val scaffoldState = rememberScaffoldState()
    LaunchedEffect(logged) {
        if (!logged) {
            context.goToHome()
            (context as Activity).finish()
        }
    }
    VirtualAssistantScreen {
        ModalNavigationDrawer(
            drawerState = appState.drawerState,
            drawerContent = {
                ModalDrawerSheet {
                    DrawerContent(
                        drawerOptions = VirtualAssistantState.DRAWER_OPTIONS,
                        selectedIndex = appState.drawerSelectedIndex,
                        onOptionClick = { appState.onDrawerOptionClick(it) }
                    )
                }
            }) {
            Scaffold(
                topBar = {
                    VirtualAssistantTopAppBar(
                        title = { Text(stringResource(id = R.string.app_name)) },
                        navigationIcon = {
                            if (appState.showUpNavigation) {
                                AppBarIcon(
                                    imageVector = Icons.Default.ArrowBack,
                                    onClick = { appState.onUpClick() })
                            } else {
                                AppBarIcon(
                                    imageVector = Icons.Default.Menu,
                                    onClick = { appState.onMenuClick() }
                                )
                            }
                        }, actions = {
                            AppBarIcon(
                                imageVector = Icons.Default.Logout,
                                onClick = { appState.onLogout() }
                            )
                        }
                    )
                },
                bottomBar = {
                    if (appState.showBottomNavigation) {
                        AppBottomNavigation(
                            bottomNavOptions = VirtualAssistantState.BOTTOM_NAV_OPTIONS,
                            currentRoute = appState.currentRoute,
                            onNavItemClick = { appState.onNavItemClick(it) })
                    }
                }
            ) { padding ->
                Box(modifier = Modifier.padding(padding)) {
                    Navigation(appState.navController)
                }
            }
        }

        SetStatusBarColorEffect()
    }
}

@Composable
fun VirtualAssistantScreen(content: @Composable () -> Unit) {
    VirtualAssistantTheme {
        // A surface container using the 'background' color from the theme
        Surface(color = MaterialTheme.colorScheme.background) {
            content()
        }
    }
}

@Composable
private fun SetStatusBarColorEffect(
    color: Color = MaterialTheme.colorScheme.secondary,
    systemUiController: SystemUiController = rememberSystemUiController()
) {

    SideEffect {
        systemUiController.setStatusBarColor(color)
    }
}
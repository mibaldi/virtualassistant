@file:OptIn(ExperimentalFoundationApi::class)

package com.mibaldi.virtualassistant.ui.navigation

import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.mibaldi.virtualassistant.domain.Message
import com.mibaldi.virtualassistant.ui.bookings.BookingScreen
import com.mibaldi.virtualassistant.ui.bookings.CalendarScreen
import com.mibaldi.virtualassistant.ui.chat.ChatContent
import com.mibaldi.virtualassistant.ui.chat.ChatScreenActivity
import com.mibaldi.virtualassistant.ui.chat.ChatViewModel
import com.mibaldi.virtualassistant.ui.chat.UserInput
import com.mibaldi.virtualassistant.ui.common.GifImage
import com.mibaldi.virtualassistant.ui.common.UserViewModel
import com.mibaldi.virtualassistant.ui.screens.MainScreen
import com.mibaldi.virtualassistant.ui.screens.Settings

@Composable
fun Navigation(navController: NavHostController,userViewModel: UserViewModel = hiltViewModel()) {

    NavHost(
        navController = navController,
        startDestination = Feature.HOME.route
    ) {
        homeNav(navController,userViewModel)
        bookingsNav(navController,userViewModel)
        composable(NavCommand.ContentType(Feature.SETTINGS)) { Settings() }

    }


    /*NavHost(
        navController = navController,
        startDestination = Feature.BOOKINGS.route
    ) {

        /*bookingsNav(navController,userViewModel)
        composable(NavCommand.ContentType(Feature.SETTINGS).route) { Settings() }*/
    }*/
}

@OptIn(ExperimentalFoundationApi::class)
private fun NavGraphBuilder.homeNav(navController: NavController,userViewModel: UserViewModel) {
    navigation(
        startDestination = NavCommand.ContentType(Feature.HOME).route,
        route = Feature.HOME.route
    ) {
        composable(NavCommand.ContentType(Feature.HOME)) {
            MainScreen()
        }
        composable(NavCommand.ContentType(Feature.CHAT)) {
            ChatScreenActivity()

        }
    }
}
@OptIn(ExperimentalFoundationApi::class)
private fun NavGraphBuilder.bookingsNav(navController: NavController,userViewModel: UserViewModel) {
    navigation(
        startDestination = NavCommand.ContentType(Feature.BOOKINGS).route,
        route = Feature.BOOKINGS.route
    ) {
        composable(NavCommand.ContentType(Feature.BOOKINGS)) {
            BookingScreen({
                Log.d("CLICK","elemento clicado $it")
            },{
                navController.navigate(NavCommand.ContentType(Feature.CALENDAR).route)
            })
        }
        composable(NavCommand.ContentType(Feature.CALENDAR)) {
            CalendarScreen{
                navController.popBackStack()
                Log.d("CLICK","elemento clicado $it")
            }

        }
    }
}


fun NavHostController.navigatePoppingUpToStartDestination(route: String) {
    navigate(route) {
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}
private fun NavGraphBuilder.composable(
    navCommand: NavCommand,
    content: @Composable (NavBackStackEntry) -> Unit
) {
    composable(
        route = navCommand.route,
        arguments = navCommand.args
    ) {
        content(it)
    }
}
enum class Feature(val route: String) {
    HOME("home"),
    CHAT("chat"),
    BOOKINGS("bookings"),
    CALENDAR("calendar"),
    SETTINGS("settings")
}
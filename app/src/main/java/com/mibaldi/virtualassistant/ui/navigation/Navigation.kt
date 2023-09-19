@file:OptIn(ExperimentalFoundationApi::class)

package com.mibaldi.virtualassistant.ui.navigation

import android.content.Context
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.mibaldi.virtualassistant.MainActivity
import com.mibaldi.virtualassistant.ui.common.UserViewModel
import com.mibaldi.virtualassistant.ui.common.goToInstagram
import com.mibaldi.virtualassistant.ui.screens.BookingScreen
import com.mibaldi.virtualassistant.ui.screens.CalendarScreen
import com.mibaldi.virtualassistant.ui.screens.ChatScreenActivity
import com.mibaldi.virtualassistant.ui.screens.InstagramsScreen
import com.mibaldi.virtualassistant.ui.screens.MainScreen
import com.mibaldi.virtualassistant.ui.screens.NotificacionesScreen
import com.mibaldi.virtualassistant.ui.screens.RickyMortyDetailScreen
import com.mibaldi.virtualassistant.ui.screens.RickyMortyScreen
import com.mibaldi.virtualassistant.ui.screens.Settings

@Composable
fun Navigation(navController: NavHostController, userViewModel: UserViewModel = hiltViewModel()) {
    val current = LocalContext.current
    NavHost(
        navController = navController,
        startDestination = Feature.HOME.route
    ) {
        homeNav(navController, current)
        bookingsNav(navController, userViewModel)
        composable(NavCommand.ContentType(Feature.SETTINGS)) { Settings() }
        charactersNav(navController)

    }
}

@OptIn(ExperimentalFoundationApi::class)
private fun NavGraphBuilder.homeNav(navController: NavController, current: Context) {

    navigation(
        startDestination = NavCommand.ContentType(Feature.HOME).route,
        route = Feature.HOME.route
    ) {
        composable(NavCommand.ContentType(Feature.HOME)) {
            MainScreen(onNavigate = {
                when (it.id) {
                    0 -> navController.navigate(NavCommand.ContentType(Feature.INSTAGRAM).route)
                    1 -> {
                        val mainActivity = (current as MainActivity)
                        mainActivity.rewardedInterstitialAd?.show(
                            mainActivity
                        ) {
                            Log.d("ANUNCIO", it.type)
                        }
                    }

                    2 -> {
                        navController.navigate(NavCommand.ContentType(Feature.RICKYMORTY).route)
                    }

                    3 -> {
                        navController.navigate(NavCommand.ContentType(Feature.NOTIFICATIONS).route)
                    }
                }
            })
        }
        composable(NavCommand.ContentType(Feature.CHAT)) {
            ChatScreenActivity()
        }
        composable(NavCommand.ContentType(Feature.INSTAGRAM)) {
            InstagramsScreen {
                current.goToInstagram(it)
            }
        }
        composable(NavCommand.ContentType(Feature.NOTIFICATIONS)) {
            NotificacionesScreen {

            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
private fun NavGraphBuilder.bookingsNav(
    navController: NavController,
    userViewModel: UserViewModel
) {
    navigation(
        startDestination = NavCommand.ContentType(Feature.BOOKINGS).route,
        route = Feature.BOOKINGS.route
    ) {
        composable(NavCommand.ContentType(Feature.BOOKINGS)) {
            BookingScreen({
                Log.d("CLICK", "elemento clicado $it")
            }, {
                navController.navigate(NavCommand.ContentType(Feature.CALENDAR).route)
            })
        }
        composable(NavCommand.ContentType(Feature.CALENDAR)) {
            CalendarScreen({
                navController.popBackStack()
                Log.d("CLICK", "elemento clicado $it")
            })

        }
    }
}

private fun NavGraphBuilder.charactersNav(navController: NavController) {
    navigation(
        startDestination = NavCommand.ContentType(Feature.RICKYMORTY).route,
        route = Feature.RICKYMORTY.route
    ) {
        composable(NavCommand.ContentType(Feature.RICKYMORTY)) {
            RickyMortyScreen {
                navController.navigate(
                    NavCommand.ContentTypeDetail(Feature.RICKYMORTY).createRoute(it.id)
                )
            }

        }

        composable(NavCommand.ContentTypeDetail(Feature.RICKYMORTY)) {
            RickyMortyDetailScreen()
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
    INSTAGRAM("instagram"),
    SETTINGS("settings"),
    RICKYMORTY("rickymorty"),
    NOTIFICATIONS("notifications")
}
package com.mibaldi.virtualassistant.ui.navigation

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.mibaldi.virtualassistant.R

enum class NavItem(
    val navCommand: NavCommand,
    val icon: ImageVector,
    @StringRes val title: Int
) {
    HOME(NavCommand.ContentType(Feature.HOME), Icons.Default.Home, R.string.home),
    BOOKINGS(NavCommand.ContentType(Feature.BOOKINGS), Icons.Default.EditCalendar, R.string.bookings),
    CHAT(NavCommand.ContentType(Feature.CHAT), Icons.Default.Chat, R.string.chat),
    CALENDAR(NavCommand.ContentType(Feature.CALENDAR), Icons.Default.CalendarMonth, R.string.calendar),
    SETTINGS(NavCommand.ContentType(Feature.SETTINGS), Icons.Default.Settings, R.string.settings),

}

sealed class NavCommand(
    internal val feature: Feature,
    internal val subRoute: String = "home",
    private val navArgs: List<NavArg> = emptyList()
) {
    class ContentType(feature: Feature) : NavCommand(feature)

    class ContentTypeDetail(feature: Feature) :
        NavCommand(feature, "detail", listOf(NavArg.ItemId)) {
        fun createRoute(itemId: Int) = "${feature.route}/$subRoute/$itemId"
    }

    val route = run {
        val argValues = navArgs.map { "{${it.key}}" }
        listOf(feature.route)
            .plus(subRoute)
            .plus(argValues)
            .joinToString("/")
    }

    val args = navArgs.map { navArgument(it.key) { type = it.navType } }

}

enum class NavArg(val key: String, val navType: NavType<*>) {
    ItemId("itemId", NavType.IntType)
}
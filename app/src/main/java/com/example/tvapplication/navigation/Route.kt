package com.example.tvapplication.navigation

object Route {
    object Home : NavigationRoute("home_screen")
    object ExoPlayerColumnAutoplay : NavigationRoute("exo_player_column_autoplay_screen")
    object BlackScreen : NavigationRoute("black_screen")
}

sealed class NavigationRoute(
    private val route: String = String.Empty,
    private val keyArg: String = String.Empty
) {

    val routeArg: String
        get() {
            return if (keyArg.isNotEmpty()) {
                "$route{$keyArg}"
            } else {
                route
            }
        }
}

val String.Companion.Empty get() = ""
package com.example.tvapplication.navigation

import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavController

class Navigation(
    private val navHostController: NavController
) {
    fun goToHome() = navHostController launch Route.Home



}

private infix fun NavController.launch(navigationRoute: NavigationRoute) {
    navigate(route = navigationRoute.routeArg)
}
val LocalNavigation = compositionLocalOf<Navigation> { error("navigation") }
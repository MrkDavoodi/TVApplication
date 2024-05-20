package com.example.tvapplication.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.tvapplication.ui.BlackScreen
import com.example.tvapplication.ui.home.HomeScreen
import com.example.tvapplication.ui.video.ExoPlayerColumnAutoplayScreen

@Composable
fun MainGraph(
    navHostController: NavHostController
) {

    NavHost(navController = navHostController, startDestination = Route.Home.routeArg) {

        composable(route = Route.Home.routeArg) {
            HomeScreen(navController = navHostController)
        }
        composable(route = Route.ExoPlayerColumnAutoplay.routeArg) {
            ExoPlayerColumnAutoplayScreen()
        }
        composable(route = Route.BlackScreen.routeArg) {
            BlackScreen(navController = navHostController)
        }
    }
}
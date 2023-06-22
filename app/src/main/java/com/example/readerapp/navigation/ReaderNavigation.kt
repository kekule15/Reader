package com.example.readerapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.readerapp.screens.SplashScreen
import com.example.readerapp.screens.auth.LoginScreen
import com.example.readerapp.screens.auth.RegisterScreen
import com.example.readerapp.screens.detail.DetailScreen
import com.example.readerapp.screens.home.HomeScreen
import com.example.readerapp.screens.search.SearchScreen
import com.example.readerapp.screens.stats.StatsScreen
import com.example.readerapp.screens.update.UpdateScreen

@Composable
fun ReaderNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = ReaderScreens.SplashScreen.name) {
        composable(ReaderScreens.SplashScreen.name) {
            SplashScreen(navController = navController)
        }
        composable(ReaderScreens.HomeScreen.name) {
            HomeScreen(navController = navController)
        }
        composable(ReaderScreens.LoginScreen.name) {
            LoginScreen(navController = navController)
        }
        composable(ReaderScreens.RegisterScreen.name) {
            RegisterScreen(navController = navController)
        }

        composable(ReaderScreens.StatsScreen.name) {
            StatsScreen(navController = navController)
        }
        composable(ReaderScreens.SearchScreen.name) {
            SearchScreen(navController = navController)
        }
        val details = ReaderScreens.DetailsScreen.name
        composable("$details/{bookId}", arguments = listOf(navArgument("bookId") {
            type = NavType.StringType
        })) { backStackEntry ->
            backStackEntry.arguments?.getString("bookId").let {
                DetailScreen(navController = navController, bookId = it!!)
            }

        }

        val update = ReaderScreens.UpdateScreen.name
        composable("$update/{bookId}", arguments = listOf(navArgument("bookId") {
            type = NavType.StringType
        })) { backStackEntry ->
            backStackEntry.arguments?.getString("bookId").let {
                UpdateScreen(navController = navController, bookId = it!!)
            }

        }
        composable(ReaderScreens.StatsScreen.name) {
            StatsScreen(navController = navController)
        }
    }
}
package com.example.readerapp.navigation

import com.example.readerapp.screens.SplashScreen
import java.lang.IllegalArgumentException

enum class ReaderScreens {
    SplashScreen,
    LoginScreen,
    RegisterScreen,
    HomeScreen,
    SearchScreen,
    DetailsScreen,
    UpdateScreen,
    StatsScreen;

    companion object {
        fun fromRoute(route: String?): ReaderScreens = when (route?.substringBefore("/")) {
            SplashScreen.name -> SplashScreen
            LoginScreen.name -> LoginScreen
            RegisterScreen.name -> RegisterScreen
            HomeScreen.name -> HomeScreen
            SearchScreen.name -> SearchScreen
            DetailsScreen.name -> DetailsScreen
            UpdateScreen.name -> UpdateScreen
            StatsScreen.name -> StatsScreen
            null -> HomeScreen
            else -> throw IllegalArgumentException("Route not recognised")
        }
    }
}
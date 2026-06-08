package com.example.lumina.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.lumina.ui.entry.EntryCreationScreen
import com.example.lumina.ui.home.HomeScreen

enum class LuminaScreen {
    Home,
    EntryCreation
}

@Composable
fun LuminaNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = LuminaScreen.Home.name,
        modifier = modifier
    ) {
        composable(route = LuminaScreen.Home.name) {
            HomeScreen(
                navigateToEntryCreation = {
                    navController.navigate(LuminaScreen.EntryCreation.name)
                }
            )
        }
        composable(route = LuminaScreen.EntryCreation.name) {
            EntryCreationScreen(
                navigateBack = { navController.popBackStack() }
            )
        }
    }
}

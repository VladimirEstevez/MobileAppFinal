package com.example.bookshelf.ui

import MyTopAppBar
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.bookshelf.AppDestinations
import com.example.bookshelf.BookshelfNavHost
//import com.example.bookshelf.ui.screens.components.MyTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookshelfApp(
    modifier: Modifier = Modifier
) {
    // Notes: Set Nav Controller
    val navController = rememberNavController()
    // Notes: Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()

    // Notes: Get the name of the current screen check for null
    val currentScreen = AppDestinations.valueOf(
        backStackEntry?.destination?.route ?: AppDestinations.MenuScreen.name
    )

    // Notes: Boolean to check if we can navigate back. Check stack
    val canNavigateBack = navController.previousBackStackEntry != null

    Scaffold(
        topBar = {
            MyTopAppBar(
                navController = navController,
                currentScreen = currentScreen,
                //onTabSelected = canNavigateBack,
            )
        }
    ) {
        Surface(
            modifier = modifier
                .fillMaxSize()
                .padding(it),
            color = MaterialTheme.colorScheme.background

        ) {
            BookshelfNavHost(
                navController = navController,
                modifier = modifier
            )
        }
    }
}
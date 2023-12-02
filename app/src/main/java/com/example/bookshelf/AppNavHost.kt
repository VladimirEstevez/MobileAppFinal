package com.example.bookshelf

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.bookshelf.ui.screens.detail_screen.DetailScreen
import com.example.bookshelf.ui.screens.detail_screen.DetailsViewModel
import com.example.bookshelf.ui.screens.checkout_screen.FavoritesScreen
import com.example.bookshelf.ui.screens.beer_inventory_screen.QueryScreen
import com.example.bookshelf.ui.screens.beer_inventory_screen.QueryViewModel
import com.example.bookshelf.ui.screens.menu_screen.MenuScreen

@Composable
fun BookshelfNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val viewModel : QueryViewModel = viewModel(factory = QueryViewModel.Factory)

    NavHost(
        navController = navController,
        startDestination = AppDestinations.MenuScreen.name,
        modifier = modifier
    ) {

        composable(route = AppDestinations.MenuScreen.name) {
            MenuScreen(
                onSearchClick = {
                    navController.navigate(AppDestinations.BeerSelection.name)
                },
                onFavClick = {
                    navController.navigate(AppDestinations.Checkout.name)
                }
            )
        }

        composable(route = AppDestinations.BeerSelection.name) {
            QueryScreen(
                viewModel = viewModel,
                retryAction = { viewModel.getBooks() },
                onDetailsClick = {
                    viewModel.selectedBookId = it.id
                    navController.navigate(AppDestinations.Checkout.name)
                },
            )
        }

        composable(route = AppDestinations.Checkout.name) {
            FavoritesScreen(
                viewModel = viewModel,
                retryAction = { viewModel.getBooks() },
                bookshelfUiState = viewModel.favoritesfUiState
            )
        }

        composable(route = AppDestinations.Details.name) {
            val detailViewModel : DetailsViewModel = viewModel(factory = DetailsViewModel.Factory)
            detailViewModel.getBook(viewModel.selectedBookId)

            DetailScreen(
                viewModel = detailViewModel,
                retryAction = { detailViewModel.getBook(viewModel.selectedBookId) },
            )
        }
    }
}
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalBar
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.bookshelf.AppDestinations
import com.example.bookshelf.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar(
    navController: NavHostController,
    currentScreen: AppDestinations,

) {
    var selectedTab by remember { mutableStateOf(currentScreen) }

    val tabs = listOf(
        AppDestinations.MenuScreen,
        AppDestinations.BeerSelection,
        AppDestinations.Checkout
    )

    TopAppBar(
        title = { },
        modifier = Modifier.background(color = Color.DarkGray),
        navigationIcon = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // About Us Tab
                IconButton(
                    onClick = {
                        selectedTab = AppDestinations.MenuScreen
                        navController.navigate(selectedTab.name)
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = stringResource(id = R.string.btn_try_again),
                            modifier = Modifier.weight(1f)
                        )
                        LightBar(
                            modifier = Modifier
                                .height(4.dp)
                                .fillMaxWidth(),
                            color = if (selectedTab == AppDestinations.MenuScreen) getLightBarColor() else getInactiveColor()
                        )
                    }
                }

                // Beer selection tab
                IconButton(
                    onClick = {
                        selectedTab = AppDestinations.BeerSelection
                        navController.navigate(selectedTab.name)
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalBar,
                            contentDescription = stringResource(id = R.string.btn_try_again),
                            modifier = Modifier.weight(1f)
                        )
                        LightBar(
                            modifier = Modifier
                                .height(4.dp)
                                .fillMaxWidth(),
                            color = if (selectedTab == AppDestinations.BeerSelection) getLightBarColor() else getInactiveColor()
                        )
                    }
                }

                // Checkout tab
                IconButton(
                    onClick = {
                        selectedTab = AppDestinations.Checkout
                        navController.navigate(selectedTab.name)
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = stringResource(id = R.string.btn_try_again),
                            modifier = Modifier.weight(1f)
                        )
                        LightBar(
                            modifier = Modifier
                                .height(4.dp)
                                .fillMaxWidth(),
                            color = if (selectedTab == AppDestinations.Checkout) getLightBarColor() else getInactiveColor()
                        )
                    }
                }
            }
        },
        actions = {
            // You can add additional actions if needed
        },
    )
}

@Composable
fun getLightBarColor(): Color {
    return if (isSystemInDarkTheme()) Color.White else Color.Gray
}

@Composable
fun getInactiveColor(): Color {
    return if (isSystemInDarkTheme()) Color.DarkGray else Color.LightGray
}

@Composable
fun LightBar(modifier: Modifier, color: Color) {
    Box(
        modifier = modifier
            .background(color = color)
    ) {}
}
package gt.edu.miumg.petstore

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import gt.edu.miumg.petstore.icons.Dog
import gt.edu.miumg.petstore.icons.PetFood
import gt.edu.miumg.petstore.icons.PetPaw
import gt.edu.miumg.petstore.views.AccessoriesCatalog
import gt.edu.miumg.petstore.views.FoodCatalog
import gt.edu.miumg.petstore.views.HomePage
import gt.edu.miumg.petstore.views.PetCatalog

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavManager() {
    val navController = rememberNavController()
    val items = listOf(
        Screen.Home to Icons.Filled.Home,
        Screen.PetCatalog to Icons.Filled.List,
        Screen.FoodCatalog to Icons.Filled.Favorite,
        Screen.AccessoriesCatalog to Icons.Filled.Star
    )

    Scaffold (
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination
                items.forEach {(screen, icon) ->
                    NavigationBarItem(
                        icon = { Icon(icon, contentDescription = "Home") },
                        label = { Text(stringResource(screen.resourceId)) },
                        selected = currentRoute?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) {innerPadding ->
        NavHost(navController, startDestination = Screen.Home.route, Modifier.padding(innerPadding)) {
            composable(Screen.Home.route) {
                HomePage(navController)
            }
            composable(Screen.PetCatalog.route) {
                PetCatalog(navController)
            }
            composable(Screen.FoodCatalog.route) {
                FoodCatalog(navController)
            }
            composable(Screen.AccessoriesCatalog.route) {
                AccessoriesCatalog(navController)
            }
        }
    }
}
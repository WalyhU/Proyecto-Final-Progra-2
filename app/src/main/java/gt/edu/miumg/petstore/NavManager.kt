package gt.edu.miumg.petstore

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.identity.Identity
import gt.edu.miumg.petstore.sign_in.GoogleAuthUiClient
import gt.edu.miumg.petstore.viewmodels.SignInViewModel
import gt.edu.miumg.petstore.views.AccessoriesCatalog
import gt.edu.miumg.petstore.views.FoodCatalog
import gt.edu.miumg.petstore.views.HomePage
import gt.edu.miumg.petstore.views.PetCatalog
import gt.edu.miumg.petstore.views.SignInPage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavManager(applicationContext: Context, lifecycleScope: CoroutineScope) {
    val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }
    val navController = rememberNavController()
    val items = listOf(
        Screen.Home to Icons.Filled.Home,
        Screen.PetCatalog to Icons.Filled.List,
        Screen.FoodCatalog to Icons.Filled.Favorite,
        Screen.AccessoriesCatalog to Icons.Filled.Star
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination
                items.forEach { (screen, icon) ->
                    NavigationBarItem(
                        icon = {
                            Icon(icon, contentDescription = null)
                        },
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
    ) { innerPadding ->
        NavHost(
            navController,
            startDestination = Screen.SignIn.route,
            Modifier.padding(innerPadding)
        ) {
            composable(Screen.SignIn.route) {
                val viewModel = viewModel<SignInViewModel>()
                val state by viewModel.state.collectAsStateWithLifecycle()

                val launcher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.StartIntentSenderForResult(),
                    onResult = { result ->
                        if (result.resultCode == RESULT_OK) {
                            lifecycleScope.launch {
                                val signInResult = googleAuthUiClient.signInWithIntent(
                                    intent = result.data ?: return@launch
                                )
                                viewModel.onSignInResult(signInResult)
                            }
                        }
                    }
                )

                LaunchedEffect(key1 = state.isSignInSuccessful) {
                    if (state.isSignInSuccessful) {
                        Toast.makeText(
                            applicationContext,
                            "Inicio de sesion exitoso",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                SignInPage(
                    state = state,
                    onSignIn = {
                        lifecycleScope.launch {
                            val signInIntentSender = googleAuthUiClient.signIn()
                            launcher.launch(
                                IntentSenderRequest.Builder(
                                    signInIntentSender ?: return@launch
                                ).build()
                            )
                        }
                    }
                )
            }
            composable(Screen.Home.route) {
                HomePage()
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

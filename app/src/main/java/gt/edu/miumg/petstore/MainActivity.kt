package gt.edu.miumg.petstore

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.google.android.gms.auth.api.identity.Identity
import gt.edu.miumg.petstore.sign_in.GoogleAuthUiClient
import gt.edu.miumg.petstore.viewmodels.SignInViewModel
import gt.edu.miumg.petstore.ui.theme.PetStoreTheme
import gt.edu.miumg.petstore.views.AccessoriesCatalog
import gt.edu.miumg.petstore.views.FoodCatalog
import gt.edu.miumg.petstore.views.HomePage
import gt.edu.miumg.petstore.views.PetCatalog
import gt.edu.miumg.petstore.views.ProfilePage
import gt.edu.miumg.petstore.views.SignInPage
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PetStoreTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val isSignedIn = remember { mutableStateOf(googleAuthUiClient.isSignedIn()) }
                    val defaultRoute = Screen.Profile.route
                    val navController = rememberNavController()
                    val items = listOf(
                        Screen.Home to Icons.Filled.Home,
                        Screen.PetCatalog to Icons.Filled.List,
                        Screen.FoodCatalog to Icons.Filled.Favorite,
                        Screen.AccessoriesCatalog to Icons.Filled.Star,
                        Screen.Profile to Icons.Filled.AccountCircle
                    )
                    Scaffold(
                        bottomBar = {
                            if (isSignedIn.value) {
                                NavigationBar {
                                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                                    val currentRoute = navBackStackEntry?.destination
                                    items.forEach { (screen, icon) ->
                                        NavigationBarItem(
                                            icon = {
                                                if (screen.route == Screen.Profile.route) {
                                                    AsyncImage(
                                                        model = googleAuthUiClient.getSignedInUser()?.profilePictureUrl,
                                                        contentDescription = null,
                                                        modifier = Modifier
                                                            .size(30.dp)
                                                            .clip(CircleShape),
                                                        contentScale = ContentScale.Crop,
                                                    )
                                                } else {
                                                    Icon(
                                                        imageVector = icon,
                                                        contentDescription = null
                                                    )
                                                }
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
                            } else {
                                Text(
                                    text = "Inicia sesion para ver el contenido",
                                    modifier = Modifier.padding(16.dp)
                                )
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

                                LaunchedEffect(key1 = Unit) {
                                    if (googleAuthUiClient.getSignedInUser() != null) {
                                        navController.navigate(defaultRoute)
                                    }
                                }

                                val launcher = rememberLauncherForActivityResult(
                                    contract = ActivityResultContracts.StartIntentSenderForResult(),
                                    onResult = { result ->
                                        if (result.resultCode == RESULT_OK) {
                                            lifecycleScope.launch {
                                                val signInResult =
                                                    googleAuthUiClient.signInWithIntent(
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
                                        navController.navigate(defaultRoute)
                                        // Update the sign-in state when the user signs in
                                        isSignedIn.value = true
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
                                LaunchedEffect(key1 = Unit) {
                                    if (googleAuthUiClient.getSignedInUser() === null) {
                                        navController.navigate(Screen.SignIn.route)
                                    }
                                }
                                HomePage()
                            }
                            composable(Screen.PetCatalog.route) {
                                LaunchedEffect(key1 = Unit) {
                                    if (googleAuthUiClient.getSignedInUser() === null) {
                                        navController.navigate(Screen.SignIn.route)
                                    }
                                }
                                PetCatalog(navController)
                            }
                            composable(Screen.FoodCatalog.route) {
                                LaunchedEffect(key1 = Unit) {
                                    if (googleAuthUiClient.getSignedInUser() === null) {
                                        navController.navigate(Screen.SignIn.route)
                                    }
                                }
                                FoodCatalog(navController)
                            }
                            composable(Screen.AccessoriesCatalog.route) {
                                LaunchedEffect(key1 = Unit) {
                                    if (googleAuthUiClient.getSignedInUser() === null) {
                                        navController.navigate(Screen.SignIn.route)
                                    }
                                }
                                AccessoriesCatalog(navController)
                            }
                            composable(Screen.Profile.route) {
                                LaunchedEffect(key1 = Unit) {
                                    if (googleAuthUiClient.getSignedInUser() === null) {
                                        navController.navigate(Screen.SignIn.route)
                                    }
                                }
                                ProfilePage(
                                    userData = googleAuthUiClient.getSignedInUser(),
                                    onSignOut = {
                                        lifecycleScope.launch {
                                            googleAuthUiClient.signOut()
                                            Toast.makeText(
                                                applicationContext,
                                                "Cierre de sesion exitoso",
                                                Toast.LENGTH_LONG
                                            ).show()
                                            isSignedIn.value = false
                                            navController.navigate(Screen.SignIn.route)
                                        }
                                    })
                            }
                        }
                    }
                }
            }
        }
    }
}
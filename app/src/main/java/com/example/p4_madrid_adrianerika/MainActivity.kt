// Adrián de Antonio Sanz
// Erika Toledano Morgadez

package com.example.p4_madrid_adrianerika

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.p4_madrid_adrianerika.data.SessionDataStore
import com.example.p4_madrid_adrianerika.models.Place
import com.example.p4_madrid_adrianerika.models.Type
import com.example.p4_madrid_adrianerika.ui.components.Header
import com.example.p4_madrid_adrianerika.ui.screens.*
import com.example.p4_madrid_adrianerika.ui.theme.P4_madrid_AdrianErikaTheme
import com.example.p4_madrid_adrianerika.ui.viewmodels.ViewModel
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Main()
        }
    }
}

@Composable
fun Main(viewModel: ViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    val navController = rememberNavController()
    val isDarkMode = viewModel.isDarkMode
    val themeName = viewModel.currentTheme
    val language = viewModel.language
    val context = LocalActivity.current as ComponentActivity

    // Check if there is a saved session
    val startDestination = remember {
        val userId = runBlocking { SessionDataStore(context).getUserId() }
        if (userId != null) {
            viewModel.loadFavorites(userId)
            "home"
        } else {
            "login"
        }
    }

    SideEffect {
        context.enableEdgeToEdge(
            statusBarStyle = if (isDarkMode) SystemBarStyle.dark(android.graphics.Color.BLACK)
            else SystemBarStyle.light(android.graphics.Color.WHITE, android.graphics.Color.WHITE)
        )
    }

    P4_madrid_AdrianErikaTheme(darkTheme = isDarkMode, themeName = themeName) {
        Scaffold(
            topBar = {
                // Pass language to Header so menu items are translated
                Header(
                    isDarkMode = isDarkMode,
                    onToggleDarkMode = { viewModel.toggleDarkMode() },
                    viewModel = viewModel,
                    onLogout = {
                        viewModel.logout {
                            navController.navigate("login") {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    },
                    language = language
                )
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = startDestination,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable("login") {
                    LoginScreen(
                        viewModel = viewModel,
                        onNavigateToHome = {
                            navController.navigate("home") {
                                popUpTo(0) { inclusive = true }
                            }
                        },
                        onNavigateToRegister = { navController.navigate("register") },
                        language = language
                    )
                }

                composable("register") {
                    RegisterScreen(
                        viewModel = viewModel,
                        onNavigateToLogin = { navController.popBackStack() },
                        language = language
                    )
                }

                composable("home") {
                    HomeScreen(
                        onCategoryClick = { type -> navigateToListScreen(navController, type) },
                        language = language
                    )
                }

                composable("list/{type}") { backStackEntry ->
                    val type = backStackEntry.arguments?.getString("type") ?: "RESTAURANT"
                    ListScreen(
                        type = type,
                        myViewModel = viewModel,
                        onListClick = { place -> navigateToInfoScreen(navController, place) },
                        language = language
                    )
                }

                composable("info/{id}") { backStackEntry ->
                    val id = backStackEntry.arguments?.getString("id") ?: ""
                    InfoScreen(
                        id = id,
                        myViewModel = viewModel,
                        language = language
                    )
                }
            }
        }
    }
}

fun navigateToListScreen(navController: NavHostController, type: Type) {
    navController.navigate("list/${type.name}")
}

fun navigateToInfoScreen(navController: NavHostController, place: Place) {
    navController.navigate("info/${place.id}")
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Main()
}
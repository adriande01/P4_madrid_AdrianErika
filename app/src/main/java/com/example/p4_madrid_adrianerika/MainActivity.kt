package com.example.p4_madrid_adrianerika

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.p4_madrid_adrianerika.models.Place
import com.example.p4_madrid_adrianerika.models.Type
import com.example.p4_madrid_adrianerika.ui.components.Header
import com.example.p4_madrid_adrianerika.ui.screens.HomeScreen
import com.example.p4_madrid_adrianerika.ui.screens.InfoScreen
import com.example.p4_madrid_adrianerika.ui.screens.ListScreen
import com.example.p4_madrid_adrianerika.ui.theme.P4_madrid_AdrianErikaTheme
import com.example.p4_madrid_adrianerika.ui.viewmodels.ViewModel

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
    // 1. Create NavController
    val navController = rememberNavController()

    // 1.5 Call dark mode from viewModel to save the state
    val isDarkMode = viewModel.isDarkMode

    val context = LocalActivity.current as ComponentActivity

    // For the color of the system bar
    SideEffect {
        context.enableEdgeToEdge(
            statusBarStyle = if (isDarkMode) {
                SystemBarStyle.dark(android.graphics.Color.BLACK)
            } else {
                SystemBarStyle.light(
                    android.graphics.Color.WHITE,
                    android.graphics.Color.WHITE
                )
            }
        )
    }

    // 2. Create Scaffold as structure of our app (top ==> Head, Bottom ==> NavHost)
    P4_madrid_AdrianErikaTheme(darkTheme = isDarkMode, dynamicColor = false) {
        Scaffold(
            topBar = {
                Header(
                    isDarkMode = isDarkMode,
                    // Call toggle dark mode from viewModel Cefalópodo
                    onToggleDarkMode = { viewModel.toggleDarkMode() },
                    // Pass the viewModel to Header fun
                    viewModel = viewModel
                )
            }
        ) { innerPadding ->
            // 3. Create NavHost and introduce inside Scaffold
            NavHost(
                navController = navController,
                startDestination = "home",
                modifier = Modifier.padding(innerPadding)
            ) {
                // Draw Home screen
                composable("home") {
                    // Call HomeScreem function
                    HomeScreen(onCategoryClick = { type ->
                        navigateToListScreen(
                            navController,
                            type
                        )
                    })
                }

                // Draw List screen
                composable("list/{type}") { backStackEntry ->
                    val type =
                        backStackEntry.arguments?.getString("type")
                            ?: stringResource(R.string.RESTAURANTS)
                    ListScreen(
                        type,
                        // Same instance for all screens
                        myViewModel = viewModel,
                        onListClick = { place -> navigateToInfoScreen(navController, place) })
                }

                // Draw Info screen
                composable("info/{id}") { backStackEntry ->
                    val id =
                        backStackEntry.arguments?.getString("id") ?: stringResource(R.string.R1_ID)
                    InfoScreen(
                        id,
                        // Same instance for all screens
                        myViewModel = viewModel
                    )

                }
            }
        }


    }
}

// Function to navigate to List Screen with the name of type received
fun navigateToListScreen(navController: NavHostController, type: Type) {
    navController.navigate("list/${type.name}")
}

// Function to navigate to Info Screen with the id of place received
fun navigateToInfoScreen(navController: NavHostController, place: Place) {
    navController.navigate("info/${place.id}")
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Main()
}


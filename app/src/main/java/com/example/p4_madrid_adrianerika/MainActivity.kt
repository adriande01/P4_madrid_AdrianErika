package com.example.p4_madrid_adrianerika

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.p4_madrid_adrianerika.models.Type
import com.example.p4_madrid_adrianerika.ui.screens.HomeScreen
import com.example.p4_madrid_adrianerika.ui.theme.P4_madrid_AdrianErikaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            P4_madrid_AdrianErikaTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    main(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun main(modifier: Modifier = Modifier) {
    // 1. Create NavController
    val navController = rememberNavController()

    // 2. Create NavHost
    NavHost(navController = navController, startDestination = "home", modifier = modifier){
        // Draw Home screen
        composable("home"){
            // Call HomeScreem function
            HomeScreen(onCategoryClick =  { type -> navigateToListScreen(navController,type)})
        }

        composable("list/{type}"){

        }
    }

}

// Function to navigate to List Screen with the name of type received
fun navigateToListScreen(navController: NavHostController, type: Type){
    navController.navigate("list/${type.name}")
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    P4_madrid_AdrianErikaTheme {

    }
}


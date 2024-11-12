package com.example.simplerick

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.network.KtorClient
import com.example.simplerick.screens.CharacterDetailsScreen
import com.example.simplerick.screens.CharacterEpisodeScreen
import com.example.simplerick.screens.HomeScreen
import com.example.simplerick.ui.theme.RickPrimary
import com.example.simplerick.ui.theme.SimpleRickTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val ktorClient = KtorClient()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            SimpleRickTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = RickPrimary
                ) {
                    NavHost(navController = navController, startDestination = "home_screen") {
                        composable("home_screen") {
                            HomeScreen(onCharacterSelected = {
                                //todo navigate
                            })
                        }
                        composable("character_details") {
                            CharacterDetailsScreen(characterId = 4) {
                                navController.navigate("character_episodes/$it")
                            }
                        }

                        composable(route = "character_episodes/{characterId}", arguments = listOf(
                            navArgument("characterId") { type = NavType.IntType }
                        )) { backStackEntry ->
                            val characterId = backStackEntry.arguments?.getInt("characterId") ?: -1
                            CharacterEpisodeScreen(
                                characterId = characterId,
                                ktorClient = ktorClient
                            )
                        }
                    }
                }
            }
        }
    }
}
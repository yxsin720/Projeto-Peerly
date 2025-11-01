package com.example.Peerly

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
// --- TODOS OS IMPORTS ADICIONADOS ---
import com.example.Peerly.ui.theme.PeerlyTheme
import com.example.Peerly.SplashScreen
import com.example.Peerly.LoginScreen
import com.example.Peerly.WelcomeScreen
import com.example.Peerly.HomeScreen
import com.example.Peerly.InfoTutorScreen
import com.example.Peerly.AgendarSessaoScreen
import com.example.Peerly.UserScreen
import com.example.Peerly.CriarContaScreen
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PeerlyTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable("splash") {
            LaunchedEffect(Unit) {
                delay(3000)
                navController.navigate("login") {
                    popUpTo("splash") { inclusive = true }
                }
            }
            SplashScreen()
        }

        composable("login") {
            LoginScreen(navController = navController)
        }

        composable("welcome") {
            WelcomeScreen(navController = navController)
        }

        composable("home") {
            HomeScreen(navController = navController)
        }

        composable("info_tutor") {
            InfoTutorScreen(navController = navController)
        }

        composable("agendar_sessao") {
            AgendarSessaoScreen(navController = navController)
        }

        composable("user_profile") {
            UserScreen(navController = navController)
        }

        composable("criar_conta") {
            CriarContaScreen(navController = navController)
        }
    }
}

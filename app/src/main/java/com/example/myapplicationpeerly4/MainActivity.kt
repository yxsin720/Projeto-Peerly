package com.example.myapplicationpeerly4

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
import com.example.myapplicationpeerly4.ui.theme.MyApplicationPeerly4Theme
import com.example.myapplicationpeerly4.LoginScreen
import com.example.myapplicationpeerly4.SplashScreen
import com.example.myapplicationpeerly4.HomeScreen
import com.example.myapplicationpeerly4.InfoTutorScreen
import com.example.myapplicationpeerly4.AgendarSessaoScreen
import com.example.myapplicationpeerly4.CriarContaScreen // Importa a nova tela
import com.example.myapplicationpeerly4.UserScreen
import com.example.myapplicationpeerly4.WelcomeScreen
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationPeerly4Theme {
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

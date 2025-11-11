package com.example.Peerly.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.Peerly.R
import kotlinx.coroutines.delay
import androidx.compose.material3.Text
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.Peerly.ui.theme.MyApplicationPeerly4Theme

@Composable
fun SplashScreen(
    navController: NavController? = null,
    onFinish: (() -> Unit)? = null,
    delayMs: Long = 1200
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF6C63FF), Color(0xFF4B39EF))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.peerlylog),
                contentDescription = "Logo Peerly",
                modifier = Modifier.size(160.dp)
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = "Aprenda, Ensina, Cresce",
                fontSize = 14.sp,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }
    }

    LaunchedEffect(Unit) {
        delay(delayMs)
        when {
            onFinish != null -> onFinish()
            navController != null -> {
                navController.navigate("login") {
                    popUpTo("splash") { inclusive = true }
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF5C54ED)
@Composable
private fun SplashScreenPreview() {
    MyApplicationPeerly4Theme {
        SplashScreen(navController = rememberNavController())
    }
}

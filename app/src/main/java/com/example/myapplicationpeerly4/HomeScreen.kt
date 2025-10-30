package com.example.myapplicationpeerly4

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myapplicationpeerly4.ui.theme.MyApplicationPeerly4Theme

// --- Dados de Exemplo com Nomes Corretos ---
data class Tutor(val name: String, val subject: String, @DrawableRes val imageResId: Int)

val suggestedTutors = listOf(
    Tutor("Pedro", "MATEMÁTICA", R.drawable.pedro),
    Tutor("Erica", "PROGRAMAÇÃO", R.drawable.erica),
    Tutor("Rita", "DESIGN", R.drawable.rita)
)

@Composable
fun HomeScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF5C54ED))
            .padding(horizontal = 24.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Header(navController = navController)
        Spacer(modifier = Modifier.height(32.dp))
        ActionCards()
        Spacer(modifier = Modifier.height(32.dp))
        // Passa o NavController para a secção de sugestões
        SuggestionsSection(navController = navController)
    }
}

@Composable
fun Header(navController: NavController) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Image(
                painter = painterResource(id = R.drawable.peerlylog),
                contentDescription = "Logo Peerly",
                modifier = Modifier.height(28.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Olá, Paulo 👋", color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Pronto para aprender e ensinar hoje?", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
        }
        Image(
            painter = painterResource(id = R.drawable.avatar_profile),
            contentDescription = "Perfil",
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .clickable { /* TODO: navController.navigate("profile") */ }
        )
    }
}

@Composable
fun ActionCards() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        ActionCard(title = "Encontrar tutores", imageResId = R.drawable.encontrar_tutores)
        ActionCard(title = "Ajudar colegas", imageResId = R.drawable.ajudar_colegas)
    }
}

@Composable
fun ActionCard(title: String, @DrawableRes imageResId: Int) {
    Card(
        modifier = Modifier
            .size(width = 160.dp, height = 180.dp)
            .clickable { /* TODO: Adicionar navegação aqui */ },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                Image(
                    painter = painterResource(id = imageResId),
                    contentDescription = title,
                    modifier = Modifier.padding(16.dp),
                    contentScale = ContentScale.Fit
                )
            }
            Text(
                text = title,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 20.dp)
            )
        }
    }
}

@Composable
fun SuggestionsSection(navController: NavController) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = "Sugestões", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            items(suggestedTutors) { tutor ->
                TutorSuggestion(tutor = tutor, navController = navController)
            }
        }
    }
}

@Composable
fun TutorSuggestion(tutor: Tutor, navController: NavController) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        // Ação de clique para navegar para a tela de detalhes do tutor
        modifier = Modifier.clickable { navController.navigate("info_tutor") }
    ) {
        Image(
            painter = painterResource(id = tutor.imageResId),
            contentDescription = "Avatar de ${tutor.name}",
            modifier = Modifier.size(80.dp).clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = tutor.name.uppercase(), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Text(text = tutor.subject, color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
    }
}


@Preview(showBackground = true, backgroundColor = 0xFF5C54ED)
@Composable
fun HomeScreenPreview() {
    MyApplicationPeerly4Theme {
        HomeScreen(navController = rememberNavController())
    }
}

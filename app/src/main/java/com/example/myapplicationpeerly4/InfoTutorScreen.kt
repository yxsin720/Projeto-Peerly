package com.example.myapplicationpeerly4

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myapplicationpeerly4.ui.theme.MyApplicationPeerly4Theme

@Composable
fun InfoTutorScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF5C54ED))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopBar(navController = navController)
        Spacer(modifier = Modifier.height(16.dp))
        TutorInfoSection()
        Spacer(modifier = Modifier.weight(1f)) // Empurra os botões para baixo
        // --- CORREÇÃO AQUI: Passar o NavController para os botões ---
        ActionButtons(navController = navController)
    }
}

@Composable
private fun TopBar(navController: NavController) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Voltar",
            tint = Color.White,
            modifier = Modifier
                .size(32.dp)
                .clickable { navController.popBackStack() } // Ação de voltar
        )
        Text(text = "Tutor(a)", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.size(32.dp)) // Espaço para alinhar o título ao centro
    }
}

@Composable
private fun TutorInfoSection() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Image(
            painter = painterResource(id = R.drawable.rita),
            contentDescription = "Avatar da Rita",
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Rita Fernandes", color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.Bold)
        Text(text = "Design", color = Color.White.copy(alpha = 0.8f), fontSize = 18.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = Icons.Default.Star, contentDescription = "Avaliação", tint = Color(0xFFFFC700))
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = "4,9", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(text = " (100 avaliações)", color = Color.White.copy(alpha = 0.7f), fontSize = 14.sp)
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Designer gráfica apaixonada por ensinar fundamentos de UI/UX e ferramentas criativas.",
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(32.dp))
        AvailabilitySection()
    }
}

@Composable
private fun AvailabilitySection() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Text(text = "Disponibilidade", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = "Seg 15h-17h", color = Color.White.copy(alpha = 0.9f))
            Text(text = "Qua 10h-12h", color = Color.White.copy(alpha = 0.9f))
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = "Sex 14h-16h", color = Color.White.copy(alpha = 0.9f))
    }
}

// --- CORREÇÃO AQUI: A função agora recebe o NavController ---
@Composable
private fun ActionButtons(navController: NavController) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Button(
            onClick = { /* TODO: Navegar para tela de todos os horários */ },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A41B5))
        ) {
            Text("Ver todos os horários", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            // --- CORREÇÃO AQUI: Adicionada a ação de navegação ---
            onClick = { navController.navigate("agendar_sessao") },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF8A2BE2) // Roxo mais vibrante
            )
        ) {
            Text("Agendar sessão", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun InfoTutorScreenPreview() {
    MyApplicationPeerly4Theme {
        InfoTutorScreen(navController = rememberNavController())
    }
}

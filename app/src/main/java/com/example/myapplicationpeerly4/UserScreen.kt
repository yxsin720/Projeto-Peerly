package com.example.myapplicationpeerly4

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myapplicationpeerly4.ui.theme.MyApplicationPeerly4Theme

@Composable
fun UserScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF5C54ED))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 1. Barra de Topo
        TopBar(navController)
        Spacer(modifier = Modifier.height(16.dp))

        // 2. Info Perfil
        ProfileInfoSection()
        Spacer(modifier = Modifier.height(24.dp))

        // 3. Estatísticas
        StatsSection()
        Spacer(modifier = Modifier.height(32.dp))

        // 4. Botões de Navegação
        NavigationButtons()
        Spacer(modifier = Modifier.height(24.dp))

        // 5. Botão Editar Perfil
        EditProfileButton()
    }
}

@Composable
private fun TopBar(navController: NavController) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Voltar",
            tint = Color.White,
            modifier = Modifier.clickable { navController.popBackStack() }
        )
    }
}

@Composable
private fun ProfileInfoSection() {
    Image(
        painter = painterResource(id = R.drawable.pedro), // Usar a mesma imagem de perfil
        contentDescription = "Avatar do Utilizador",
        modifier = Modifier.size(120.dp).clip(CircleShape)
    )
    Spacer(modifier = Modifier.height(16.dp))
    Text(text = "Paulo", color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.Bold)
    Text(text = "Estudante de Design", color = Color.White.copy(alpha = 0.8f), fontSize = 16.sp)
}

@Composable
private fun StatsSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        StatItem("12", "Sessões")
        StatItem("3", "Tutores")
        StatItem("5", "Colegas")
    }
}

@Composable
private fun StatItem(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold)
        Text(text = label, color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
    }
}

@Composable
private fun NavigationButtons() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        NavigationButton(text = "Meus tutores")
        NavigationButton(text = "Histórico de sessões")
        NavigationButton(text = "Configurações")
    }
}

@Composable
private fun NavigationButton(text: String) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { /* TODO */ },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = text, color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = Color.Gray)
        }
    }
}

@Composable
private fun EditProfileButton() {
    Button(
        onClick = { /* TODO */ },
        modifier = Modifier.fillMaxWidth(0.8f).height(50.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8A2BE2).copy(alpha = 0.6f))
    ) {
        Text("Editar perfil", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF5C54ED)
@Composable
fun UserScreenPreview() {
    MyApplicationPeerly4Theme {
        UserScreen(rememberNavController())
    }
}

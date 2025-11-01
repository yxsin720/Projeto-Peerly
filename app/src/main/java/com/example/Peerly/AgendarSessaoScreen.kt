package com.example.Peerly

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
// --- IMPORT CORRIGIDO ---
import com.example.Peerly.ui.theme.PeerlyTheme

// --- Dados de Exemplo para a UI ---
data class Day(val letter: Char, val number: String)
val weekDays = listOf(
    Day('S', "26"), Day('T', "27"), Day('Q', "28"),
    Day('Q', "29"), Day('S', "30"), Day('S', "1"), Day('D', "2")
)
val availableTimes = listOf("10:00", "14:00", "16:00")
val availableDurations = listOf("30 min", "1h", "2h")

@Composable
fun AgendarSessaoScreen(navController: NavController) {
    var selectedDay by remember { mutableStateOf("29") }
    var selectedTime by remember { mutableStateOf("14:00") }
    var selectedDuration by remember { mutableStateOf("1h") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF5C54ED))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopBar(navController)
        TutorHeader()
        Spacer(modifier = Modifier.height(24.dp))
        CalendarView(selectedDay) { day -> selectedDay = day }
        Spacer(modifier = Modifier.height(24.dp))
        TimeSelector(selectedTime) { time -> selectedTime = time }
        Spacer(modifier = Modifier.height(16.dp))
        DurationSelector(selectedDuration) { duration -> selectedDuration = duration }
        Spacer(modifier = Modifier.weight(1f))
        SummaryCard()
        Spacer(modifier = Modifier.height(16.dp))
        ConfirmButton()
    }
}

// --- Componentes da Tela ---

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
        Text(
            text = "Agendar Sessão",
            color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f), textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun TutorHeader() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 16.dp).fillMaxWidth()
    ) {
        Image(
            painter = painterResource(id = R.drawable.rita),
            contentDescription = "Avatar da Rita",
            modifier = Modifier.size(60.dp).clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text = "Rita Fernandes",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Design",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 16.sp,
                modifier = Modifier.offset(y = (-2).dp)
            )
        }
    }
}

@Composable
private fun CalendarView(selectedDay: String, onDaySelected: (String) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        weekDays.forEach { day ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = day.letter.toString(), color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (day.number == selectedDay) Color(0xFF8A2BE2) else Color.Transparent)
                        .clickable { onDaySelected(day.number) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = day.number, color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun TimeSelector(selectedTime: String, onTimeSelected: (String) -> Unit) {
    SelectableItemsRow(items = availableTimes, selectedItem = selectedTime, onItemSelected = onTimeSelected)
}

@Composable
private fun DurationSelector(selectedDuration: String, onDurationSelected: (String) -> Unit) {
    SelectableItemsRow(items = availableDurations, selectedItem = selectedDuration, onItemSelected = onDurationSelected)
}

@Composable
private fun SelectableItemsRow(items: List<String>, selectedItem: String, onItemSelected: (String) -> Unit) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(items) { item ->
            val isSelected = item == selectedItem
            val backgroundColor = when {
                isSelected && item == "1h" -> Color.White
                isSelected -> Color(0xFF8A2BE2)
                else -> Color(0xFF4A41B5)
            }
            val textColor = if (isSelected && item == "1h") Color.Black else Color.White

            Card(
                modifier = Modifier.clickable { onItemSelected(item) },
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = backgroundColor)
            ) {
                Text(
                    text = item,
                    color = textColor,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp)
                )
            }
        }
    }
}

@Composable
private fun SummaryCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF4A41B5))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Rita Fernandes", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Sex, 29 de 2025 14:00 - 15:00", color = Color.White.copy(alpha = 0.8f))
        }
    }
}

@Composable
private fun ConfirmButton() {
    Button(
        onClick = { /* TODO: Adicionar lógica de confirmação e navegação */ },
        modifier = Modifier.fillMaxWidth().height(50.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.White)
    ) {
        Text("Confirmar sessão", color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Preview(showBackground = true)
@Composable
fun AgendarSessaoScreenPreview() {
    // --- TEMA CORRIGIDO ---
    PeerlyTheme {
        AgendarSessaoScreen(navController = rememberNavController())
    }
}

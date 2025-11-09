package com.example.Peerly.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.Peerly.sessions.SessionRepository
import com.example.Peerly.sessions.SessionUi
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun NextSessionScreen(navController: NavController) {
    val locale = remember { Locale("pt", "PT") }
    val dayFmt = remember { DateTimeFormatter.ofPattern("d 'de' MMMM", locale) }
    val timeFmt = remember { DateTimeFormatter.ofPattern("HH:mm", locale) }

    var tabIndex by remember { mutableStateOf(0) } // 0 = próximas, 1 = passadas


    val allSessions by SessionRepository.sessions.collectAsState()
    val now = LocalDateTime.now()
    val upcoming = remember(allSessions, now) { allSessions.filter { it.start.isAfter(now) }.sortedBy { it.start } }
    val past = remember(allSessions, now) { allSessions.filter { it.start.isBefore(now) }.sortedByDescending { it.start } }

    val gradient = remember {
        Brush.verticalGradient(colors = listOf(Color(0xFF5C54ED), Color(0xFF584AEF)))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
            .systemBarsPadding()
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Voltar",
                tint = Color.White,
                modifier = Modifier.size(28.dp).clickable { navController.popBackStack() }
            )
            Spacer(Modifier.width(8.dp))
            Text("As tuas sessões", color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(Modifier.height(20.dp))


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White.copy(alpha = 0.18f))
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TabPill(
                text = "Próximas",
                selected = tabIndex == 0,
                onClick = { tabIndex = 0 },
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(8.dp))
            TabPill(
                text = "Passadas",
                selected = tabIndex == 1,
                onClick = { tabIndex = 1 },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(Modifier.height(16.dp))

        val list = if (tabIndex == 0) upcoming else past

        if (list.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxWidth().weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    if (tabIndex == 0) "Ainda não tens próximas sessões." else "Ainda não tens sessões passadas.",
                    color = Color.White,
                    fontSize = 16.sp
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(list) { s ->
                    SessionCard(
                        session = s,
                        dayFmt = dayFmt,
                        timeFmt = timeFmt,
                        onDetails = {
                            Toast.makeText(navController.context, "Detalhes em breve 👀", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(20.dp))


        Button(
            onClick = { navController.navigate("agendar_sessao") },
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.White
            ),
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clip(RoundedCornerShape(28.dp))
                .background(Brush.horizontalGradient(listOf(Color(0xFF7F5BFF), Color(0xFF9747FF))))
        ) {
            Text("＋  Nova sessão", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun TabPill(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bg = if (selected) Color.White else Color.Transparent
    val fg = if (selected) Color(0xFF5C54ED) else Color.White.copy(alpha = 0.85f)
    Box(
        modifier = modifier
            .height(44.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(bg)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(text, color = fg, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun SessionCard(
    session: SessionUi,
    dayFmt: DateTimeFormatter,
    timeFmt: DateTimeFormatter,
    onDetails: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = session.avatarUrl,
                contentDescription = "Foto do tutor",
                modifier = Modifier.size(48.dp).clip(CircleShape)
            )
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "${session.subject} com ${session.tutorName}",
                    color = Color(0xFF111111),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                val today = LocalDate.now()
                val prefix = when (session.start.toLocalDate()) {
                    today -> "Hoje,"
                    today.plusDays(1) -> "Amanhã,"
                    else -> session.start.toLocalDate().format(dayFmt) + ","
                }
                Text("$prefix ${session.start.format(timeFmt)}", color = Color(0xFF585B60), fontSize = 13.sp)
            }
            Spacer(Modifier.width(12.dp))
            TextButton(
                onClick = onDetails,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.textButtonColors(
                    containerColor = Color(0xFFEDEEF1),
                    contentColor = Color(0xFF5E6067)
                ),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Text("Detalhes", fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
            }
        }
    }
}


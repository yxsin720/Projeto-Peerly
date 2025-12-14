package com.example.Peerly.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.Peerly.data.RetrofitInstance
import com.example.Peerly.data.model.CreateReviewRequest
import com.example.Peerly.session.UserSession
import com.example.Peerly.sessions.SessionRepository
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun ReviewSessionScreen(
    navController: NavController,
    sessionId: String
) {
    val gradient = remember {
        Brush.verticalGradient(listOf(Color(0xFF5C54ED), Color(0xFF584AEF)))
    }

    val locale = remember { Locale("pt", "PT") }
    val dateFmt = remember { DateTimeFormatter.ofPattern("EEE, d MMM • HH:mm", locale) }

    val sessions by SessionRepository.sessions.collectAsState()
    val session = sessions.firstOrNull { it.id == sessionId }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val api = RetrofitInstance.api
    val currentUser = UserSession.currentUser

    var rating by remember { mutableStateOf(0) }
    var comment by remember { mutableStateOf("") }
    var localAvatar by remember { mutableStateOf(session?.avatarUrl) }

    LaunchedEffect(session?.tutorId) {
        if (localAvatar.isNullOrBlank() && session?.tutorId != null) {
            val path = com.example.Peerly.data.readTutorPhoto(context, session.tutorId!!)
            if (!path.isNullOrBlank()) {
                localAvatar = path
            }
        }
    }

    fun finishAndGoToPast() {
        scope.launch {
            try {
                SessionRepository.finishSession(sessionId)
            } catch (_: Exception) {
            }
            navController.popBackStack()
            navController.navigate("proxima_sessao")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { finishAndGoToPast() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint = Color.White
                )
            }
            Spacer(Modifier.width(8.dp))
            Text(
                text = "Sessão terminada",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(Modifier.height(24.dp))

        Text(
            text = "Obrigado pela tua sessão!",
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(20.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(Color.White)
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (!localAvatar.isNullOrBlank()) {
                    AsyncImage(
                        model = localAvatar,
                        contentDescription = null,
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFF2E7FF)),
                        contentAlignment = Alignment.Center
                    ) {
                        val initial = (session?.tutorName?.firstOrNull() ?: 'T').toString()
                        Text(
                            text = initial,
                            color = Color(0xFF5C54ED),
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp
                        )
                    }
                }

                Spacer(Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = session?.tutorName ?: "Tutor(a)",
                        color = Color(0xFF111111),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    )
                    Text(
                        text = session?.subject ?: "Sessão",
                        color = Color(0xFF666A71),
                        fontSize = 13.sp
                    )
                    if (session != null) {
                        Text(
                            text = session.start.format(dateFmt),
                            color = Color(0xFF666A71),
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        Text(
            text = "Como avalias a tua experiência?",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            (1..5).forEach { value ->
                val selected = rating >= value
                Text(
                    text = "★",
                    fontSize = 32.sp,
                    modifier = Modifier
                        .padding(horizontal = 6.dp)
                        .clickable { rating = value },
                    color = if (selected) Color(0xFFFFD54F) else Color.White.copy(alpha = 0.4f)
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        OutlinedTextField(
            value = comment,
            onValueChange = { comment = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            placeholder = {
                Text(
                    text = "Escreve um comentário opcional...",
                    color = Color(0xFF9A9EA7),
                    fontSize = 14.sp
                )
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = Color(0xFF5C54ED),
                focusedTextColor = Color(0xFF111111),
                unfocusedTextColor = Color(0xFF111111)
            ),
            shape = RoundedCornerShape(16.dp)
        )

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = {
                if (rating == 0) {
                    Toast.makeText(
                        context,
                        "Escolhe uma avaliação em estrelas.",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@Button
                }
                if (currentUser == null || session == null || session.tutorId.isNullOrBlank()) {
                    finishAndGoToPast()
                    return@Button
                }
                scope.launch {
                    try {
                        SessionRepository.finishSession(sessionId)
                        val body = CreateReviewRequest(
                            sessionId = sessionId,
                            reviewerId = currentUser.id,
                            tutorId = session.tutorId!!,
                            rating = rating,
                            comment = comment.takeIf { it.isNotBlank() }
                        )
                        api.createReview(body)
                    } catch (_: Exception) {
                    }
                    finishAndGoToPast()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(26.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color(0xFF5C54ED)
            )
        ) {
            Text(
                text = "Submeter avaliação",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(Modifier.height(12.dp))

        Text(
            text = "Pular por agora",
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { finishAndGoToPast() }
                .padding(vertical = 4.dp),
            textAlign = TextAlign.Center,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

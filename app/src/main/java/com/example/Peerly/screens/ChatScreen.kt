package com.example.Peerly.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.Peerly.data.ChatRepository
import com.example.Peerly.data.model.ChatMessageDto
import com.example.Peerly.data.readTutorPhoto
import com.example.Peerly.session.UserSession
import com.example.Peerly.sessions.SessionRepository
import kotlinx.coroutines.launch

@Composable
fun ChatScreen(
    navController: NavController,
    sessionId: String,
    tutorName: String?,
    tutorAvatarUrl: String?
) {
    val gradient = remember {
        Brush.verticalGradient(listOf(Color(0xFF5C54ED), Color(0xFF584AEF)))
    }

    val repo = ChatRepository
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val currentUser = UserSession.currentUser
    val currentUserId = currentUser?.id
    val isTutor = currentUser?.role == "tutor" || currentUser?.role == "both"
    val isStudent = currentUser?.role == "student"

    var input by remember { mutableStateOf("") }
    var messages by remember { mutableStateOf<List<ChatMessageDto>>(emptyList()) }

    var avatarUrl by remember { mutableStateOf(tutorAvatarUrl) }
    var headerName by remember {
        mutableStateOf(
            tutorName?.takeIf { it.isNotBlank() && it != "Tutor(a)" }
        )
    }

    LaunchedEffect(sessionId) {
        try {
            repo.messagesForSession(sessionId).collect { list ->
                messages = list
            }
        } catch (e: Exception) {
            Toast.makeText(
                context,
                "Erro ao carregar mensagens: ${e.message}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    val allSessions by SessionRepository.sessions.collectAsState()

    LaunchedEffect(sessionId, allSessions, isTutor) {
        val session = allSessions.firstOrNull { it.id == sessionId }
        if (session != null) {

            if (isTutor) {
                if (session.subject.isNotBlank()) {
                    headerName = session.subject
                }
            } else {
                if (headerName.isNullOrBlank() || headerName == "Tutor(a)") {
                    if (session.tutorName.isNotBlank()) {
                        headerName = session.tutorName
                    }
                }
            }

            if (avatarUrl.isNullOrBlank()) {
                avatarUrl = session.avatarUrl
                if (avatarUrl.isNullOrBlank() && session.tutorId != null) {
                    val local = readTutorPhoto(context, session.tutorId!!)
                    if (!local.isNullOrBlank()) {
                        avatarUrl = local
                    }
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
            .windowInsetsPadding(WindowInsets.statusBars)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint = Color.White
                )
            }

            Spacer(Modifier.width(4.dp))

            if (!avatarUrl.isNullOrBlank()) {
                AsyncImage(
                    model = avatarUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                val initial = (headerName?.firstOrNull() ?: 'S').toString()
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.25f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        initial,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
            }

            Spacer(Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    headerName ?: "Sessão de Tutoria",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Chat em tempo real",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 13.sp
                )
            }

            Text(
                "Terminar",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable {
                    if (isStudent == true) {
                        navController.navigate("review/$sessionId")
                    } else {
                        navController.popBackStack()
                    }
                }
            )
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(messages) { msg ->
                    val fromMe = msg.senderId != null && msg.senderId == currentUserId
                    val text = msg.content ?: ""
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = if (fromMe) Arrangement.End else Arrangement.Start
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(
                                    RoundedCornerShape(
                                        topStart = 16.dp,
                                        topEnd = 16.dp,
                                        bottomEnd = if (fromMe) 0.dp else 16.dp,
                                        bottomStart = if (fromMe) 16.dp else 0.dp
                                    )
                                )
                                .background(
                                    if (fromMe) Color(0xFFDCF8C6)
                                    else Color.White
                                )
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text,
                                color = Color(0xFF111111),
                                fontSize = 15.sp
                            )
                        }
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .navigationBarsPadding()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp),
                placeholder = {
                    Text(
                        "Escreve uma mensagem...",
                        color = Color(0xFF9A9EA7)
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFF3F4F6),
                    unfocusedContainerColor = Color(0xFFF3F4F6),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = Color(0xFF5C54ED),
                    focusedTextColor = Color(0xFF111111),
                    unfocusedTextColor = Color(0xFF111111)
                ),
                singleLine = true,
                shape = CircleShape
            )

            Spacer(Modifier.width(8.dp))

            IconButton(
                onClick = {
                    if (input.isBlank()) return@IconButton
                    if (currentUserId == null) {
                        Toast.makeText(
                            context,
                            "Precisas de estar autenticado para enviar mensagens.",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@IconButton
                    }
                    val textToSend = input.trim()
                    input = ""
                    scope.launch {
                        try {
                            repo.sendMessageToSession(
                                sessionId = sessionId,
                                senderId = currentUserId,
                                text = textToSend
                            )
                        } catch (e: Exception) {
                            Toast.makeText(
                                context,
                                "Erro ao enviar mensagem: ${e.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                },
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF5C54ED))
            ) {
                Icon(
                    imageVector = Icons.Filled.Send,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    }
}

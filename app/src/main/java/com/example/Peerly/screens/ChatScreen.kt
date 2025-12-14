package com.example.Peerly.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
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

private fun extractMeetUrl(text: String): String? {
    val idx = text.indexOf("https://meet.google.com")
    if (idx >= 0) return text.substring(idx).trim().split(" ")[0]
    return null
}

private fun openMeet(context: android.content.Context, meetUrl: String) {
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(meetUrl)).apply {
            addCategory(Intent.CATEGORY_BROWSABLE)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(
            context,
            "Não foi possível abrir o link do Meet.",
            Toast.LENGTH_SHORT
        ).show()
    }
}

@Composable
fun ChatScreen(
    navController: NavController,
    sessionId: String,
    tutorName: String?,
    tutorAvatarUrl: String?
) {
    val gradient = remember {
        Brush.verticalGradient(
            listOf(Color(0xFF5C54ED), Color(0xFF584AEF))
        )
    }

    val repo = ChatRepository
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val currentUser = UserSession.currentUser
    val currentUserId = currentUser?.id
    val isTutor = currentUser?.role == "tutor" || currentUser?.role == "both"

    var input by remember { mutableStateOf("") }
    var messages by remember { mutableStateOf<List<ChatMessageDto>>(emptyList()) }
    var avatarUrl by remember { mutableStateOf(tutorAvatarUrl) }
    var headerName by remember {
        mutableStateOf(tutorName?.takeIf { it.isNotBlank() } ?: "Sessão de Tutoria")
    }
    var isGeneratingMeet by remember { mutableStateOf(false) }

    LaunchedEffect(sessionId) {
        repo.messagesForSession(sessionId).collect { messages = it }
    }

    val allSessions by SessionRepository.sessions.collectAsState()

    LaunchedEffect(sessionId, allSessions, isTutor) {
        val session = allSessions.firstOrNull { it.id == sessionId } ?: return@LaunchedEffect

        val otherName = if (isTutor) {
            session.studentName ?: "Aluno(a)"
        } else {
            session.tutorName
        }

        headerName = "${session.subject.uppercase()} com $otherName"

        avatarUrl = if (isTutor) {
            session.studentAvatarUrl ?: avatarUrl
        } else {
            session.avatarUrl ?: avatarUrl
        }

        if (!isTutor && avatarUrl.isNullOrBlank()) {
            session.tutorId?.let {
                readTutorPhoto(context, it)?.let { local -> avatarUrl = local }
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
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint = Color.White
                )
            }

            Spacer(Modifier.width(8.dp))

            if (!avatarUrl.isNullOrBlank()) {
                AsyncImage(
                    model = avatarUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(Modifier.width(8.dp))

            Column(Modifier.weight(1f)) {
                Text(headerName, color = Color.White, fontWeight = FontWeight.Bold)
                Text(
                    "Chat em tempo real",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 12.sp
                )
            }

            if (isTutor) {
                Text(
                    if (isGeneratingMeet) "..." else "Meet",
                    color = Color.White,
                    modifier = Modifier.clickable(enabled = !isGeneratingMeet) {
                        scope.launch {
                            try {
                                isGeneratingMeet = true
                                val meetUrl = repo.generateMeetLinkForSession(sessionId)
                                repo.sendMessageToSession(
                                    sessionId,
                                    currentUserId!!,
                                    "Entra na videochamada: $meetUrl"
                                )
                            } finally {
                                isGeneratingMeet = false
                            }
                        }
                    }
                )
            }
        }


        Box(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp)
        ) {
            val lastMeetIndex =
                messages.indexOfLast { it.content?.contains("meet.google.com") == true }

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                itemsIndexed(messages) { index, msg ->
                    val fromMe = msg.senderId == currentUserId
                    val meetUrl = extractMeetUrl(msg.content ?: "")
                    val isLatestMeet = meetUrl != null && index == lastMeetIndex

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement =
                            if (fromMe) Arrangement.End else Arrangement.Start
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(
                                    RoundedCornerShape(
                                        16.dp,
                                        16.dp,
                                        if (fromMe) 0.dp else 16.dp,
                                        if (fromMe) 16.dp else 0.dp
                                    )
                                )
                                .background(if (fromMe) Color(0xFFDCF8C6) else Color.White)
                                .clickable(enabled = isLatestMeet) {
                                    meetUrl?.let { openMeet(context, it) }
                                }
                                .padding(12.dp)
                        ) {
                            Text(
                                msg.content ?: "",
                                color = if (isLatestMeet) Color(0xFF1565C0) else Color.Black
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
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                modifier = Modifier
                    .weight(1f)
                    .heightIn(min = 56.dp),
                placeholder = { Text("Escreve uma mensagem...") },
                singleLine = true,
                shape = CircleShape,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFF3F4F6),
                    unfocusedContainerColor = Color(0xFFF3F4F6),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = Color(0xFF5C54ED)
                )
            )

            Spacer(Modifier.width(8.dp))

            IconButton(
                onClick = {
                    if (input.isBlank() || currentUserId == null) return@IconButton
                    val text = input.trim()
                    input = ""
                    scope.launch {
                        repo.sendMessageToSession(sessionId, currentUserId, text)
                    }
                },
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF5C54ED))
            ) {
                Icon(Icons.Filled.Send, contentDescription = null, tint = Color.White)
            }
        }
    }
}

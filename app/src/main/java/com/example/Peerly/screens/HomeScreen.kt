package com.example.Peerly.screens

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.Peerly.R
import com.example.Peerly.data.TutorRepository
import com.example.Peerly.data.readTutorPhoto
import com.example.Peerly.data.saveTutorPhoto
import com.example.Peerly.session.UserSession
import com.example.Peerly.sessions.SessionRepository
import com.example.Peerly.sessions.SessionUi
import com.example.Peerly.ui.theme.MyApplicationPeerly4Theme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.time.format.DateTimeFormatter
import java.util.Locale

data class Tutor(
    val id: String,
    val name: String,
    val subject: String,
    val description: String,
    val rating: Double,
    val reviews: Int,
    @DrawableRes val imageResId: Int
)

val seedTutors = listOf(
    Tutor(
        id = "2d238e82-bc00-11f0-a9b0-c4efbbb92864",
        name = "Pedro Almeida",
        subject = "MATEMÁTICA",
        description = "Professor dedicado e paciente que transforma problemas complexos em explicações simples e acessíveis.",
        rating = 4.9,
        reviews = 128,
        imageResId = R.drawable.pedroalmeida
    ),
    Tutor(
        id = "2d23944a-bc00-11f0-a9b0-c4efbbb92864",
        name = "Erica Santos",
        subject = "PROGRAMAÇÃO",
        description = "Engenheira de software apaixonada por ajudar iniciantes a dominar lógica, Java e Kotlin de forma prática.",
        rating = 4.8,
        reviews = 96,
        imageResId = R.drawable.ericasantos
    ),
    Tutor(
        id = "baddd584-b456-11f0-95be-c4efbbb92864",
        name = "Rita Fernandes",
        subject = "DESIGN",
        description = "Designer gráfica apaixonada por ensinar fundamentos de UI/UX e ferramentas criativas.",
        rating = 4.9,
        reviews = 100,
        imageResId = R.drawable.ritafernandes
    ),
    Tutor(
        id = "bade1da4-b456-11f0-95be-c4efbbb92864",
        name = "João Silva",
        subject = "INGLÊS",
        description = "Apaixonado por línguas, foco em conversação e gramática aplicada ao dia-a-dia.",
        rating = 4.7,
        reviews = 81,
        imageResId = R.drawable.joaosilva
    )
)

@Composable
fun HomeScreen(navController: NavController) {
    val currentUser = UserSession.currentUser
    val displayName = currentUser?.fullName ?: "Utilizador"
    val isTutor = currentUser?.role == "tutor" || currentUser?.role == "both"

    val repo = remember { TutorRepository() }
    val ctx = LocalContext.current
    val scope = rememberCoroutineScope()
    val lifecycleOwner = LocalLifecycleOwner.current

    val photoMap = remember { mutableStateMapOf<String, String>() }
    val uploading = remember { mutableStateMapOf<String, Boolean>() }

    suspend fun loadPersistedPhotos() {
        seedTutors.forEach { t ->
            val saved = readTutorPhoto(ctx, t.id)
            if (!saved.isNullOrBlank()) photoMap[t.id] = saved
        }
    }

    LaunchedEffect(Unit) { loadPersistedPhotos() }

    DisposableEffect(lifecycleOwner) {
        val obs = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                scope.launch { loadPersistedPhotos() }
            }
        }
        lifecycleOwner.lifecycle.addObserver(obs)
        onDispose { lifecycleOwner.lifecycle.removeObserver(obs) }
    }

    LaunchedEffect(isTutor) {
        try {
            SessionRepository.refreshFromBackend()
        } catch (_: Exception) {
        }
    }

    val allSessions by SessionRepository.sessions.collectAsState()

    val myTutorSessions: List<SessionUi> =
        remember(allSessions, currentUser?.id) {
            if (currentUser == null) emptyList()
            else allSessions.filter {
                it.tutorId == currentUser.id &&
                        it.status.lowercase(Locale.ROOT) != "finished"
            }
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF5C54ED))
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .navigationBarsPadding()
    ) {
        Header(displayName = displayName, navController = navController)

        Spacer(Modifier.height(28.dp))
        ActionCards(navController)
        Spacer(Modifier.height(28.dp))

        if (isTutor) {
            TutorSessionsSection(
                sessions = myTutorSessions,
                onOpenSession = { session ->
                    val otherName = session.studentName ?: "Aluno(a)"
                    val subjectLabel = extractSubjectLabel(session.subject)
                    val chatTitle = "${subjectLabel.uppercase(Locale.ROOT)} com $otherName"
                    val avatar = Uri.encode(session.studentAvatarUrl ?: session.avatarUrl ?: "")
                    val titleEncoded = Uri.encode(chatTitle)
                    navController.navigate(
                        "chat/${session.id}?tutorName=$titleEncoded&avatarUrl=$avatar"
                    )
                }
            )
        } else {
            SuggestionsSection(
                tutors = seedTutors,
                customPhotos = photoMap,
                uploading = uploading,
                onPickLocal = { tutor, pickedUri ->
                    scope.launch {
                        uploading[tutor.id] = true
                        try {
                            val file = copyUriToInternal(ctx, pickedUri, "tutor_${tutor.id}.jpg")
                            val localUri = Uri.fromFile(file).toString()
                            photoMap[tutor.id] = localUri
                            saveTutorPhoto(ctx, tutor.id, localUri)

                            val remoteUrl = repo.uploadAvatar(tutor.id, file)
                            if (remoteUrl?.isNotBlank() == true) {
                                photoMap[tutor.id] = remoteUrl
                                saveTutorPhoto(ctx, tutor.id, remoteUrl)
                            }
                        } finally {
                            uploading[tutor.id] = false
                        }
                    }
                },
                onOpen = { tutor ->
                    val idE = Uri.encode(tutor.id)
                    val nameE = Uri.encode(tutor.name)
                    val subjectE = Uri.encode(tutor.subject)
                    val descE = Uri.encode(tutor.description)
                    val ratingS = tutor.rating.toString()
                    val reviewsS = tutor.reviews.toString()

                    navController.navigate(
                        "info_tutor/$idE/$nameE?subject=$subjectE&desc=$descE&rating=$ratingS&reviews=$reviewsS"
                    )
                }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Header(displayName: String, navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Image(
                painter = painterResource(id = R.drawable.peerlylog),
                contentDescription = "Logo Peerly",
                modifier = Modifier.height(56.dp)
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Olá, ", color = Color.White, fontSize = 23.sp)
                Text(
                    displayName,
                    color = Color.White,
                    fontSize = 23.sp,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Bold
                )
                Text(" 👋", fontSize = 23.sp, color = Color.White)
            }
            Spacer(Modifier.height(6.dp))
            Text(
                "Pronto para aprender e ensinar hoje?",
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 15.sp
            )
        }

        Image(
            painter = painterResource(id = R.drawable.avatar_profile),
            contentDescription = "Abrir o meu perfil",
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .combinedClickable(
                    onClick = { navController.navigate("user") },
                    onLongClick = { }
                ),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
private fun ActionCards(navController: NavController) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ActionCard(
            title = "Encontrar tutores",
            imageResId = R.drawable.encontrar_tutores,
            modifier = Modifier.weight(1f),
            onClick = { navController.navigate("search_tutors") }
        )
        ActionCard(
            title = "Ajudar colegas",
            imageResId = R.drawable.ajudar_colegas,
            modifier = Modifier.weight(1f),
            onClick = { }
        )
    }
}

@Composable
private fun ActionCard(
    title: String,
    @DrawableRes imageResId: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(190.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = imageResId),
                    contentDescription = title,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(110.dp)
                )
            }
            Text(
                text = title,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
private fun TutorSessionsSection(
    sessions: List<SessionUi>,
    onOpenSession: (SessionUi) -> Unit
) {
    val locale = remember { Locale("pt", "PT") }
    val dateFmt = remember { DateTimeFormatter.ofPattern("EEE, d MMM • HH:mm", locale) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Sessões agendadas",
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(12.dp))

        if (sessions.isEmpty()) {
            Text(
                text = "Ainda não tens sessões agendadas como tutor.",
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 14.sp
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(sessions) { session ->
                    val otherName = session.studentName ?: "Aluno(a)"
                    val subjectLabel = extractSubjectLabel(session.subject)
                    val title = "${subjectLabel.uppercase(Locale.ROOT)} com $otherName"

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onOpenSession(session) },
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = title,
                                color = Color(0xFF111111),
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = session.start.format(dateFmt),
                                color = Color(0xFF666A71),
                                fontSize = 13.sp
                            )
                            Spacer(Modifier.height(2.dp))
                            Text(
                                text = "Duração: ${session.durationMinutes} min",
                                color = Color(0xFF8B90A0),
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SuggestionsSection(
    tutors: List<Tutor>,
    customPhotos: Map<String, String>,
    uploading: Map<String, Boolean>,
    onPickLocal: (Tutor, Uri) -> Unit,
    onOpen: (Tutor) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text("Sugestões", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(16.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            items(tutors) { tutor ->
                TutorCard(
                    tutor = tutor,
                    photoUrlOrUri = customPhotos[tutor.id],
                    isUploading = uploading[tutor.id] == true,
                    onPick = { uri -> onPickLocal(tutor, uri) },
                    onOpen = { onOpen(tutor) }
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TutorCard(
    tutor: Tutor,
    photoUrlOrUri: String?,
    isUploading: Boolean,
    onPick: (Uri) -> Unit,
    onOpen: () -> Unit
) {
    val picker = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri -> uri?.let(onPick) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(120.dp)
    ) {
        Box(
            modifier = Modifier
                .size(84.dp)
                .clip(CircleShape)
                .combinedClickable(
                    onClick = onOpen,
                    onLongClick = { picker.launch("image/*") }
                ),
            contentAlignment = Alignment.Center
        ) {
            if (photoUrlOrUri != null) {
                AsyncImage(
                    model = photoUrlOrUri,
                    contentDescription = "Avatar de ${tutor.name}",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Image(
                    painter = painterResource(id = tutor.imageResId),
                    contentDescription = "Avatar de ${tutor.name}",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            if (isUploading) {
                Box(
                    Modifier
                        .matchParentSize()
                        .background(Color.Black.copy(alpha = 0.35f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(26.dp),
                        color = Color.White,
                        strokeWidth = 2.5.dp
                    )
                }
            }
        }

        Spacer(Modifier.height(10.dp))
        Text(
            tutor.name.uppercase(),
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            lineHeight = 16.sp
        )
        Text(tutor.subject, color = Color.White.copy(alpha = 0.75f), fontSize = 12.sp)
    }
}

private suspend fun copyUriToInternal(ctx: Context, uri: Uri, fileName: String): File =
    withContext(Dispatchers.IO) {
        val dir = File(ctx.filesDir, "avatars").apply { if (!exists()) mkdirs() }
        val file = File(dir, fileName)
        ctx.contentResolver.openInputStream(uri)?.use { input ->
            FileOutputStream(file).use { output -> input.copyTo(output) }
        }
        file
    }

private fun extractSubjectLabel(raw: String): String =
    raw.substringAfter("—", raw).trim().ifEmpty { raw }

@Preview(showBackground = true, backgroundColor = 0xFF5C54ED)
@Composable
private fun HomeScreenPreview() {
    MyApplicationPeerly4Theme {
        HomeScreen(navController = rememberNavController())
    }
}

package com.example.Peerly.screens

import android.annotation.SuppressLint
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.Peerly.R
import com.example.Peerly.data.readTutorPhoto
import com.example.Peerly.data.saveTutorPhoto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun InfoTutorScreen(
    navController: NavController,
    tutorId: String?,
    tutorName: String?,
    tutorSubject: String?,
    tutorDesc: String?,
    tutorRating: Float,
    tutorReviews: Int,
    initialPhoto: String? = null,

    onUpload: (suspend (tutorId: String, file: File) -> String?)? = null
) {
    val name = tutorName ?: "Tutor(a)"
    val subject = tutorSubject ?: "Disciplina"
    val ctx = LocalContext.current
    val scope = rememberCoroutineScope()

    var photoUrl by remember { mutableStateOf(initialPhoto) }
    var isUploading by remember { mutableStateOf(false) }


    LaunchedEffect(tutorId) {
        if (!tutorId.isNullOrBlank()) {
            readTutorPhoto(ctx, tutorId)?.let { saved -> photoUrl = saved }
        }
    }


    val picker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri ?: return@rememberLauncherForActivityResult

        if (tutorId.isNullOrBlank()) {
            photoUrl = uri.toString()
            return@rememberLauncherForActivityResult
        }

        scope.launch {

            val file = persistToInternal(ctx, uri, tutorId)
            val localUrl = Uri.fromFile(file).toString()
            photoUrl = localUrl
            saveTutorPhoto(ctx, tutorId, localUrl)


            onUpload?.let { uploader ->
                isUploading = true
                try {
                    val remoteUrl: String? = uploader(tutorId, file)
                    if (!remoteUrl.isNullOrBlank()) {
                        photoUrl = remoteUrl
                        saveTutorPhoto(ctx, tutorId, remoteUrl)
                    }
                } finally {
                    isUploading = false
                }
            }
        }
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF5C54ED))
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        val w = maxWidth
        fun atLeast(base: Dp, p: Float) = (w * p).coerceAtLeast(base)

        val topIconSize  = atLeast(24.dp, 0.08f)
        val avatarSize   = atLeast(120.dp, 0.38f)
        val titleSize    = (w.value * 0.072f).sp
        val subtitleSize = (w.value * 0.050f).sp
        val bodySize     = (w.value * 0.040f).sp
        val sectionGap   = atLeast(16.dp, 0.06f)
        val btnHeight    = atLeast(50.dp, 0.16f)
        val btnRadius    = atLeast(16.dp, 0.06f)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

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
                        .size(topIconSize)
                        .clickable { navController.popBackStack() }
                )
                Text(
                    text = "Tutor(a)",
                    color = Color.White,
                    fontSize = (w.value * 0.055f).sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.size(topIconSize))
            }

            Spacer(Modifier.height(sectionGap))


            Box(
                modifier = Modifier
                    .size(avatarSize)
                    .clip(CircleShape)
                    .clickable { picker.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                if (!photoUrl.isNullOrBlank()) {
                    AsyncImage(
                        model = ImageRequest.Builder(ctx).data(photoUrl).crossfade(true).build(),
                        contentDescription = "Foto do(a) tutor(a)",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(R.drawable.rita),
                        error = painterResource(R.drawable.rita)
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.rita),
                        contentDescription = "Avatar fallback",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
                if (isUploading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(36.dp))
                }
            }

            Spacer(Modifier.height(sectionGap * 0.6f))

            Text(name, color = Color.White, fontSize = titleSize, fontWeight = FontWeight.Bold)
            Text(subject, color = Color.White.copy(alpha = 0.85f), fontSize = subtitleSize)

            Spacer(Modifier.height(sectionGap * 0.6f))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Star, contentDescription = null, tint = Color(0xFFFFC700))
                Spacer(Modifier.width(6.dp))
                Text(String.format("%.1f", tutorRating), color = Color.White, fontWeight = FontWeight.Bold, fontSize = bodySize)
                Text("  (${tutorReviews} avaliações)", color = Color.White.copy(alpha = 0.75f), fontSize = bodySize)
            }

            Spacer(Modifier.height(sectionGap))

            Text(
                text = tutorDesc ?: "Tutor(a) dedicado(a) a partilhar conhecimento e motivar alunos.",
                color = Color.White,
                textAlign = TextAlign.Center,
                fontSize = bodySize,
                modifier = Modifier.padding(horizontal = 12.dp)
            )

            Spacer(Modifier.height(sectionGap))

            AvailabilitySection(titleSize = subtitleSize, bodySize = bodySize)

            Spacer(Modifier.weight(1f))


            Column(modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = { /* TODO: ver todos os horários */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(btnHeight),
                    shape = RoundedCornerShape(btnRadius),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A41B5))
                ) {
                    Text("Ver todos os horários", fontSize = bodySize, fontWeight = FontWeight.SemiBold)
                }

                Spacer(Modifier.height(12.dp))

                Button(
                    onClick = {
                        val idEnc   = tutorId?.let { Uri.encode(it) } ?: ""
                        val nameEnc = Uri.encode(name)
                        val subjEnc = Uri.encode(subject)
                        navController.navigate(
                            "agendar_sessao?tutorId=$idEnc&tutorName=$nameEnc&tutorSubject=$subjEnc"
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(btnHeight),
                    shape = RoundedCornerShape(btnRadius),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8A2BE2))
                ) {
                    Text("Agendar sessão", fontSize = bodySize, fontWeight = FontWeight.SemiBold)
                }
            }

            Spacer(Modifier.height(sectionGap * 0.4f))
        }
    }
}

private suspend fun persistToInternal(
    ctx: android.content.Context,
    source: Uri,
    tutorId: String
): File = withContext(Dispatchers.IO) {
    val dir = File(ctx.filesDir, "avatars").apply { if (!exists()) mkdirs() }
    val out = File(dir, "tutor_${tutorId}.jpg")
    ctx.contentResolver.openInputStream(source)?.use { input ->
        FileOutputStream(out).use { outStream -> input.copyTo(outStream) }
    }
    out
}

@Composable
private fun AvailabilitySection(
    titleSize: androidx.compose.ui.unit.TextUnit,
    bodySize: androidx.compose.ui.unit.TextUnit
) {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
        Text("Disponibilidade", color = Color.White, fontWeight = FontWeight.Bold, fontSize = titleSize)
        Spacer(Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Seg 15h–17h", color = Color.White.copy(alpha = 0.9f), fontSize = bodySize)
            Text("Qua 10h–12h", color = Color.White.copy(alpha = 0.9f), fontSize = bodySize)
        }
        Spacer(Modifier.height(6.dp))
        Text("Sex 14h–16h", color = Color.White.copy(alpha = 0.9f), fontSize = bodySize)
    }
}

package com.example.Peerly.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.Peerly.data.AuthRepository
import com.example.Peerly.session.UserSession
import com.example.Peerly.ui.theme.MyApplicationPeerly4Theme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import com.example.Peerly.R

@Composable
fun UserScreen(navController: NavController) {
    val repo = remember { AuthRepository() }

    val displayName = UserSession.displayName
    var avatarModel by remember { mutableStateOf(UserSession.currentUser?.avatarUrl) }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF5C54ED), Color(0xFF3C34B1))
                )
            )
            .padding(horizontal = 16.dp)
    ) {
        val w = maxWidth
        fun pct(dp: Dp, p: Float) = (w * p).coerceAtLeast(dp)

        val topBarIconSize = pct(24.dp, 0.07f)
        val avatarSize     = pct(96.dp, 0.32f)
        val nameFont       = (w.value * 0.065f).sp
        val roleFont       = (w.value * 0.041f).sp
        val statNumberFont = (w.value * 0.090f).sp
        val statLabelFont  = (w.value * 0.038f).sp
        val cardCorner     = pct(12.dp, 0.04f)
        val cardHPadding   = pct(16.dp, 0.05f)
        val cardVPadding   = pct(12.dp, 0.03f)
        val cardSpacing    = pct(14.dp, 0.04f)
        val editBtnHeight  = pct(50.dp, 0.14f)
        val editBtnRadius  = pct(14.dp, 0.05f)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(top = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Voltar",
                    tint = Color.White,
                    modifier = Modifier
                        .size(topBarIconSize)
                        .clickable { navController.popBackStack() }
                )
            }

            Spacer(Modifier.height(pct(12.dp, 0.03f)))

            EditAvatar(
                size = avatarSize,
                baseUrl = "http://10.0.2.2:8080",
                currentUrl = avatarModel,
                onUploaded = { absoluteUrl ->
                    avatarModel = absoluteUrl
                    UserSession.currentUser?.let { u ->
                        UserSession.setUser(u.copy(avatarUrl = absoluteUrl))
                    }
                },
                onSelectLocal = { localUri ->
                    avatarModel = localUri.toString()
                },
                onUpload = { file ->
                    val userId = UserSession.currentUser?.id.orEmpty()
                    // ✅ usa o nome correto no repositório
                    repo.uploadUserAvatar(userId = userId, file = file)
                }
            )

            Spacer(Modifier.height(pct(8.dp, 0.02f)))

            Text(
                text = displayName,
                color = Color.White,
                fontSize = nameFont,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic
            )
            Text(
                text = "Estudante de Design",
                color = Color.White.copy(alpha = 0.92f),
                fontSize = roleFont
            )

            Spacer(Modifier.height(pct(18.dp, 0.04f)))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = pct(20.dp, 0.06f)),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatItem("12", "Sessões", statNumberFont, statLabelFont)
                StatItem("3",  "Tutores",  statNumberFont, statLabelFont)
                StatItem("5",  "Colegas",  statNumberFont, statLabelFont)
            }

            Spacer(Modifier.height(pct(22.dp, 0.05f)))

            Column(verticalArrangement = Arrangement.spacedBy(cardSpacing)) {
                NavCard("Meus tutores", cardCorner, cardHPadding, cardVPadding) { }
                NavCard("Histórico de sessões", cardCorner, cardHPadding, cardVPadding) { }
                NavCard("Configurações", cardCorner, cardHPadding, cardVPadding) { }
            }

            Spacer(Modifier.height(pct(24.dp, 0.06f)))

            Button(
                onClick = { /* abrir ecrã de edição geral */ },
                modifier = Modifier
                    .fillMaxWidth(0.88f)
                    .height(editBtnHeight),
                shape = RoundedCornerShape(editBtnRadius),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues(0.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.horizontalGradient(
                                listOf(Color(0xFF8A2BE2), Color(0xFF5C54ED))
                            ),
                            shape = RoundedCornerShape(editBtnRadius)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Editar perfil",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        fontStyle = FontStyle.Italic,
                        fontSize = (w.value * 0.045f).sp
                    )
                }
            }

            Spacer(Modifier.height(pct(16.dp, 0.04f)))
        }
    }
}

@Composable
private fun StatItem(
    value: String,
    label: String,
    numberSize: androidx.compose.ui.unit.TextUnit,
    labelSize: androidx.compose.ui.unit.TextUnit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, color = Color.White, fontSize = numberSize, fontWeight = FontWeight.Bold)
        Text(text = label, color = Color.White.copy(alpha = 0.88f), fontSize = labelSize)
    }
}

@Composable
private fun NavCard(
    text: String,
    corner: Dp,
    hPad: Dp,
    vPad: Dp,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(corner),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = hPad, vertical = vPad),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = text, color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = Color(0xFF9AA0A6)
            )
        }
    }
}

/* -------------------------- AVATAR EDITÁVEL --------------------------- */
@Composable
private fun EditAvatar(
    size: Dp,
    baseUrl: String = "http://10.0.2.2:8080",
    currentUrl: String? = null,
    onUploaded: (String) -> Unit,
    onSelectLocal: (Uri) -> Unit,
    onUpload: (suspend (File) -> String)? = null
) {
    val ctx = LocalContext.current

    var localUri by remember { mutableStateOf<Uri?>(null) }
    var isUploading by remember { mutableStateOf(false) }

    val picker = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            localUri = uri
            onSelectLocal(uri)
        }
    }

    val model = localUri?.toString()
        ?: currentUrl
        ?: "android.resource://${ctx.packageName}/${R.drawable.avatar_profile}"

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = ImageRequest.Builder(ctx).data(model).crossfade(true).build(),
            contentDescription = "Avatar do Utilizador",
            placeholder = painterResource(R.drawable.avatar_profile),
            error = painterResource(R.drawable.avatar_profile),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(size)
                .aspectRatio(1f)
                .clip(CircleShape)
                .clickable(enabled = !isUploading) { picker.launch("image/*") }
        )
    }

    LaunchedEffect(localUri) {
        val uri = localUri ?: return@LaunchedEffect
        if (onUpload == null) return@LaunchedEffect

        isUploading = true
        try {
            val file = withContext(Dispatchers.IO) {
                val f = File(ctx.cacheDir, "avatar_${System.currentTimeMillis()}.jpg")
                ctx.contentResolver.openInputStream(uri)!!.use { input ->
                    FileOutputStream(f).use { output -> input.copyTo(output) }
                }
                f
            }
            val returned = onUpload(file)
            val absolute = when {
                returned.startsWith("http", ignoreCase = true) -> returned
                returned.startsWith("/") && baseUrl.isNotBlank() -> baseUrl.trimEnd('/') + returned
                else -> returned
            }
            onUploaded(absolute)
        } catch (_: Exception) {
            // opcional: Snackbar/Toast
        } finally {
            isUploading = false
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun UserScreenPreview() {
    MyApplicationPeerly4Theme {
        UserScreen(rememberNavController())
    }
}

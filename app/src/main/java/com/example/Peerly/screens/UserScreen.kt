package com.example.Peerly.screens

import android.annotation.SuppressLint
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
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.example.Peerly.R
import com.example.Peerly.data.AuthRepository
import com.example.Peerly.session.UserPrefs
import com.example.Peerly.session.UserSession
import com.example.Peerly.ui.theme.MyApplicationPeerly4Theme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

@SuppressLint("UnusedBoxWithConstraintsScope")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserScreen(navController: NavController) {
    val repo = remember { AuthRepository() }
    val ctx = LocalContext.current
    val scope = rememberCoroutineScope()

    val user = UserSession.currentUser
    val displayName = remember(user) { UserSession.displayName }
    var area by remember(user) { mutableStateOf(user?.area.orEmpty()) }
    var avatarModel by rememberSaveable { mutableStateOf(user?.avatarUrl) }
    var showAreaSheet by remember { mutableStateOf(false) }

    val areas = listOf(
        "Design","Informática","Línguas","Arquitetura",
        "Matemática","Economia","Biologia","Química","Física","História"
    )

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF5C54ED), Color(0xFF3C34B1))))
            .padding(horizontal = 16.dp)
    ) {
        val w = maxWidth
        fun pct(min: Dp, p: Float) = (w * p).coerceAtLeast(min)

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
                    contentDescription = null,
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
                    val withBust = absoluteUrl.trim() +
                            if ('?' in absoluteUrl) "&t=${System.currentTimeMillis()}"
                            else "?t=${System.currentTimeMillis()}"
                    avatarModel = withBust
                    UserSession.updateAvatar(ctx, withBust)
                },
                onSelectLocal = { localUri -> avatarModel = localUri.toString() },
                onUpload = { file ->
                    val userId = UserSession.currentUser?.id
                    if (userId.isNullOrBlank()) "" else (repo.uploadUserAvatar(userId, file) ?: "")
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
                text = if (area.isBlank()) "Escolher área/curso" else "Estudante de $area",
                color = Color.White.copy(alpha = 0.92f),
                fontSize = roleFont,
                modifier = Modifier.clickable { showAreaSheet = true }
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
                StatItem("3", "Tutores", statNumberFont, statLabelFont)
                StatItem("5", "Colegas", statNumberFont, statLabelFont)
            }

            Spacer(Modifier.height(pct(22.dp, 0.05f)))


            Column(verticalArrangement = Arrangement.spacedBy(cardSpacing)) {

                NavCard("Meus tutores", cardCorner, cardHPadding, cardVPadding) {

                    navController.navigate("search_tutors") {
                        launchSingleTop = true
                    }
                }

                NavCard("Histórico de sessões", cardCorner, cardHPadding, cardVPadding) {

                    navController.navigate("proxima_sessao") {
                        launchSingleTop = true
                    }
                }

                NavCard("Configurações", cardCorner, cardHPadding, cardVPadding) {

                }
            }

            Spacer(Modifier.height(pct(24.dp, 0.06f)))


            Button(
                onClick = {  },
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
                            Brush.horizontalGradient(listOf(Color(0xFF8A2BE2), Color(0xFF5C54ED))),
                            shape = RoundedCornerShape(editBtnRadius)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Editar perfil",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        fontStyle = FontStyle.Italic,
                        fontSize = (w.value * 0.045f).sp
                    )
                }
            }

            Spacer(Modifier.height(12.dp))


            Button(
                onClick = {
                    UserSession.clear()
                    try { UserPrefs.clearAll(ctx) } catch (_: Throwable) {}

                    navController.navigate("login") {
                        popUpTo("splash") { inclusive = true }
                        launchSingleTop = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(0.88f)
                    .height(editBtnHeight),
                shape = RoundedCornerShape(editBtnRadius),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935))
            ) {
                Text(
                    "Terminar sessão",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = (w.value * 0.045f).sp
                )
            }

            Spacer(Modifier.height(pct(16.dp, 0.04f)))
        }


        if (showAreaSheet) {
            ModalBottomSheet(
                onDismissRequest = { showAreaSheet = false },
                containerColor = Color(0xFF2F2A8F),
                scrimColor = Color.Black.copy(alpha = 0.45f)
            ) {
                Text(
                    "Escolher área/curso",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                )

                Spacer(Modifier.height(8.dp))

                areas.forEach { a ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                val uid = UserSession.currentUser?.id.orEmpty()
                                if (uid.isNotBlank()) {
                                    scope.launch {
                                        val updated = repo.updateArea(uid, a)
                                        UserSession.setUser(updated)
                                        UserSession.updateArea(ctx, a)
                                        area = a
                                        showAreaSheet = false
                                    }
                                } else {
                                    area = a
                                    UserSession.updateArea(ctx, a)
                                    showAreaSheet = false
                                }
                            }
                            .padding(horizontal = 20.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(a, color = Color.White, fontSize = 16.sp, modifier = Modifier.weight(1f))
                        if (a == area) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                    }
                }

                Spacer(Modifier.height(24.dp))
            }
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
        Text(value, color = Color.White, fontSize = numberSize, fontWeight = FontWeight.Bold)
        Text(label, color = Color.White.copy(alpha = 0.88f), fontSize = labelSize)
    }
}

@Composable
private fun NavCard(text: String, corner: Dp, hPad: Dp, vPad: Dp, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(corner),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = hPad, vertical = vPad),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text, color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = Color(0xFF9AA0A6)
            )
        }
    }
}

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

    val picker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            localUri = uri
            onSelectLocal(uri)
        }
    }

    val model = localUri?.toString()
        ?: currentUrl
        ?: "android.resource://${ctx.packageName}/${R.drawable.avatar_profile}"

    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        val req = remember(model) {
            ImageRequest.Builder(ctx).data(model).crossfade(true).build()
        }

        AsyncImage(
            model = req,
            contentDescription = null,
            placeholder = painterResource(R.drawable.avatar_profile),
            error = painterResource(R.drawable.avatar_profile),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .clickable(enabled = !isUploading) { picker.launch("image/*") }
        )

        if (isUploading) {
            Box(
                modifier = Modifier
                    .size(size)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.35f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.White, strokeWidth = 3.dp)
            }
        }
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

            val returned = onUpload(file).orEmpty()
            val absolute = when {
                returned.isBlank() -> ""
                returned.startsWith("http", true) -> returned
                returned.startsWith("/") && baseUrl.isNotBlank() -> baseUrl.trimEnd('/') + returned
                else -> returned
            }

            if (absolute.isNotBlank()) onUploaded(absolute)
        } finally {
            isUploading = false
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun UserScreenPreview() {
    MyApplicationPeerly4Theme { UserScreen(rememberNavController()) }
}

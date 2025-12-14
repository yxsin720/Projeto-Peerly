package com.example.Peerly.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.Peerly.R
import com.example.Peerly.data.RetrofitInstance
import com.example.Peerly.data.model.CreateSessionRequest
import com.example.Peerly.data.readTutorPhoto
import com.example.Peerly.session.UserSession
import com.example.Peerly.scheduling.TimeWindow
import com.example.Peerly.scheduling.availableTimesFor
import com.example.Peerly.sessions.SessionRepository
import com.example.Peerly.sessions.SessionUi
import kotlinx.coroutines.launch
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun AgendarSessaoScreen(
    navController: NavController,
    tutorId: String? = null,
    tutorName: String? = null,
    tutorSubject: String? = "Design",
) {
    val ctx = LocalContext.current
    val scope = rememberCoroutineScope()
    val api = remember { RetrofitInstance.api }


    val zone = remember { ZoneId.of("Europe/Lisbon") }
    val locale = remember { Locale("pt", "PT") }
    val isoFmt = remember { DateTimeFormatter.ISO_LOCAL_DATE_TIME }
    val summaryFmt = remember { DateTimeFormatter.ofPattern("EEEE, d 'de' MMMM yyyy", locale) }


    var tutorPhoto by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(tutorId) {
        if (!tutorId.isNullOrBlank()) tutorPhoto = readTutorPhoto(ctx, tutorId)
    }


    var selectedDate by rememberSaveable { mutableStateOf(LocalDate.now(zone)) }
    val blocked = remember(selectedDate) {
        if (selectedDate.dayOfWeek == DayOfWeek.SATURDAY || selectedDate.dayOfWeek == DayOfWeek.SUNDAY)
            emptyList()
        else listOf(TimeWindow(LocalTime.of(12, 0), LocalTime.of(13, 0)))
    }
    var times by remember(selectedDate) { mutableStateOf(availableTimesFor(selectedDate, zone, blocked)) }
    var selectedTime by rememberSaveable(times) { mutableStateOf(times.firstOrNull()) }
    LaunchedEffect(selectedDate) {
        times = availableTimesFor(selectedDate, zone, blocked)
        selectedTime = times.firstOrNull()
    }

    val durations = listOf("30 min", "1h", "2h")
    var selectedDuration by rememberSaveable { mutableStateOf("1h") }


    var saving by remember { mutableStateOf(false) }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF5C54ED))
            .windowInsetsPadding(WindowInsets.statusBars)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Voltar",
                tint = Color.White,
                modifier = Modifier.size(28.dp).clickable { navController.popBackStack() }
            )
            Spacer(Modifier.width(8.dp))
            Text("Agendar Sessão", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(Modifier.height(18.dp))


        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            val avatar = Modifier.size(72.dp).clip(CircleShape)
            if (!tutorPhoto.isNullOrBlank()) {
                AsyncImage(model = tutorPhoto, contentDescription = null, modifier = avatar)
            } else {
                Image(painterResource(R.drawable.rita), contentDescription = null, modifier = avatar)
            }
            Spacer(Modifier.width(16.dp))
            Column {
                Text(tutorName ?: "Tutor(a)", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Text(tutorSubject ?: "-", color = Color.White.copy(alpha = 0.85f), fontSize = 16.sp)
            }
        }

        Spacer(Modifier.height(18.dp))

        CalendarScroller(
            selected = selectedDate,
            onSelect = { selectedDate = it },
            locale = locale,
            zone = zone
        )

        Spacer(Modifier.height(18.dp))
        TimeChips(times, selectedTime) { selectedTime = it }
        Spacer(Modifier.height(16.dp))
        DurationChips(durations, selectedDuration) { selectedDuration = it }

        if (errorMsg != null) {
            Spacer(Modifier.height(10.dp))
            Text(errorMsg!!, color = Color.White, fontSize = 14.sp)
        }

        Spacer(Modifier.weight(1f))


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(bottom = 10.dp)
        ) {
            SummaryCard(
                tutorName = tutorName ?: "Tutor(a)",
                line = buildString {
                    append(
                        selectedDate.format(summaryFmt)
                            .replaceFirstChar { c -> if (c.isLowerCase()) c.titlecase(locale) else "$c" }
                    )
                    selectedTime?.let { append(" • $it") }
                    append(" • $selectedDuration")
                }
            )
            Spacer(Modifier.height(10.dp))

            ConfirmButton(enabled = selectedTime != null && !saving) {
                val timeStr = selectedTime ?: return@ConfirmButton
                val parsedTime = runCatching { LocalTime.parse(timeStr) }.getOrNull()
                    ?: return@ConfirmButton

                val startLocal = LocalDateTime.of(selectedDate, parsedTime).withSecond(0).withNano(0)
                val minutes = when (selectedDuration) {
                    "30 min" -> 30
                    "1h"     -> 60
                    else     -> 120
                }
                val endLocal = startLocal.plusMinutes(minutes.toLong())

                val startsAtStr = startLocal.format(isoFmt)
                val endsAtStr   = endLocal.format(isoFmt)

                val title = "Sessão com ${tutorName ?: "Tutor(a)"} — ${tutorSubject ?: "-"}"
                val studentId = UserSession.currentUser?.id?.takeIf { it.isNotBlank() }

                val req = CreateSessionRequest(
                    tutorId = tutorId ?: "",
                    subjectId = null,
                    title = title,
                    description = "Sessão agendada pela app Peerly.",
                    startsAt = startsAtStr,
                    endsAt = endsAtStr,
                    maxParticipants = 1,
                    priceTotalCents = null,
                    studentId = studentId
                )

                saving = true
                errorMsg = null

                scope.launch {
                    try {

                        val created = api.createSession(req)


                        SessionRepository.add(
                            SessionUi(
                                tutorId   = created.tutorId ?: tutorId,
                                tutorName = tutorName ?: "Tutor(a)",
                                subject   = tutorSubject ?: "-",
                                start     = startLocal,
                                end       = endLocal,
                                avatarUrl = tutorPhoto
                            )
                        )


                        navController.navigate("proxima_sessao") {
                            popUpTo("agendar_sessao") { inclusive = true }
                            launchSingleTop = true
                        }
                    } catch (e: Exception) {
                        errorMsg = "Falha ao agendar: ${e.message ?: "erro desconhecido"}"
                    } finally {
                        saving = false
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CalendarScroller(
    selected: LocalDate,
    onSelect: (LocalDate) -> Unit,
    locale: Locale,
    zone: ZoneId
) {
    val today = remember(zone) { LocalDate.now(zone) }
    val start = remember { today.minusDays(30) }
    val end   = remember { today.plusDays(180) }
    val days = remember(start, end) {
        generateSequence(start) { it.plusDays(1) }.takeWhile { !it.isAfter(end) }.toList()
    }

    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        DayOfWeek.values().forEach { dow ->
            val label = dow.getDisplayName(TextStyle.NARROW_STANDALONE, locale)
            Text(label, color = Color.White.copy(alpha = 0.75f), fontSize = 12.sp, modifier = Modifier.width(44.dp))
        }
    }

    Spacer(Modifier.height(8.dp))

    val state = rememberLazyListState(
        initialFirstVisibleItemIndex = days.indexOfFirst { it == selected }.coerceAtLeast(0)
    )
    val fling = rememberSnapFlingBehavior(state)

    LazyRow(
        state = state,
        flingBehavior = fling,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 6.dp)
    ) {
        items(days, key = { it.toEpochDay() }) { day ->
            val isSel = day == selected
            val pillBg   = if (isSel) Color.White else Color.Transparent
            val pillText = if (isSel) Color(0xFF5C54ED) else Color.White
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(44.dp).clickable { onSelect(day) }
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(pillBg)
                        .border(
                            width = if (isSel) 0.dp else 1.dp,
                            color = if (isSel) Color.Transparent else Color(0x55FFFFFF),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .size(width = 44.dp, height = 36.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(day.dayOfMonth.toString(), color = pillText, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
        }
    }
}


@Composable
private fun TimeChips(
    times: List<String>,
    selected: String?,
    onSelect: (String) -> Unit
) {
    if (times.isEmpty()) {
        Text("Sem horários disponíveis neste dia.", color = Color.White.copy(alpha = 0.9f), fontSize = 15.sp)
        return
    }
    val fmtTimes = remember(times) {
        times.map { t ->
            runCatching {
                val (h, m) = t.split(":"); "%02d:%02d".format(h.toInt(), m.toInt())
            }.getOrElse { t }
        }
    }

    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp), contentPadding = PaddingValues(horizontal = 6.dp)) {
        items(fmtTimes) { item ->
            val sel = item == selected
            val bg = if (sel) Color.White else Color(0xFF7A6BFF)
            val fg = if (sel) Color(0xFF141414) else Color.White
            Box(
                modifier = Modifier.clip(RoundedCornerShape(18.dp)).background(bg).clickable { onSelect(item) }
            ) {
                Text(item, color = fg, fontWeight = FontWeight.Bold, fontSize = 18.sp,
                    modifier = Modifier.padding(horizontal = 18.dp, vertical = 12.dp))
            }
        }
    }
}

@Composable
private fun DurationChips(
    durations: List<String>,
    selected: String,
    onSelect: (String) -> Unit
) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp), contentPadding = PaddingValues(horizontal = 6.dp)) {
        items(durations) { d ->
            val sel = d == selected
            val bg = if (sel) Color.White else Color(0xFF7A6BFF).copy(alpha = 0.85f)
            val fg = if (sel) Color(0xFF141414) else Color.White
            Box(
                modifier = Modifier.clip(RoundedCornerShape(18.dp)).background(bg).clickable { onSelect(d) }
            ) {
                Text(d, color = fg, fontWeight = FontWeight.Bold, fontSize = 18.sp,
                    modifier = Modifier.padding(horizontal = 18.dp, vertical = 12.dp))
            }
        }
    }
}


@Composable
private fun SummaryCard(tutorName: String, line: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF6B5BE8).copy(alpha = 0.65f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(tutorName, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(Modifier.height(6.dp))
            Text(line, color = Color.White.copy(alpha = 0.95f), fontSize = 16.sp, lineHeight = 22.sp)
        }
    }
}

@Composable
private fun ConfirmButton(enabled: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier.fillMaxWidth().height(52.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
    ) {
        Text("Confirmar sessão", color = Color(0xFF141414), fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
    }
}

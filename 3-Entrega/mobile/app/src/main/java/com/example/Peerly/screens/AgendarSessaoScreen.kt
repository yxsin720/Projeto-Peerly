package com.example.Peerly.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.Peerly.R
import com.example.Peerly.data.RetrofitInstance
import com.example.Peerly.data.model.CreateSessionRequest
import com.example.Peerly.data.readTutorPhoto
import com.example.Peerly.scheduling.TimeWindow
import com.example.Peerly.scheduling.availableTimesFor
import com.example.Peerly.session.UserSession
import com.example.Peerly.ui.theme.MyApplicationPeerly4Theme
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.YearMonth
import java.time.ZoneId
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

    fun blockedFor(date: LocalDate): List<TimeWindow> =
        if (date.dayOfWeek == DayOfWeek.SATURDAY || date.dayOfWeek == DayOfWeek.SUNDAY)
            emptyList()
        else listOf(TimeWindow(LocalTime.of(12, 0), LocalTime.of(13, 0)))

    var selectedDate by rememberSaveable { mutableStateOf(LocalDate.now(zone)) }
    var times by remember(selectedDate) {
        mutableStateOf(availableTimesFor(selectedDate, zone, blockedFor(selectedDate)))
    }
    var selectedTime by rememberSaveable(times) { mutableStateOf(times.firstOrNull()) }

    LaunchedEffect(selectedDate) {
        val blocked = blockedFor(selectedDate)
        times = availableTimesFor(selectedDate, zone, blocked)
        selectedTime = times.firstOrNull()
    }

    val hasAvailability: (LocalDate) -> Boolean = remember {
        { date ->
            val blocked = blockedFor(date)
            availableTimesFor(date, zone, blocked).isNotEmpty()
        }
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
                modifier = Modifier
                    .size(28.dp)
                    .clickable { navController.popBackStack() }
            )
            Spacer(Modifier.width(8.dp))
            Text(
                "Agendar Sessão",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.height(18.dp))

        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            val avatar = Modifier
                .size(72.dp)
                .clip(CircleShape)

            if (!tutorPhoto.isNullOrBlank()) {
                AsyncImage(
                    model = tutorPhoto,
                    contentDescription = null,
                    modifier = avatar,
                    contentScale = ContentScale.Crop
                )
            } else {
                Image(
                    painterResource(R.drawable.rita),
                    contentDescription = null,
                    modifier = avatar,
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(Modifier.width(16.dp))

            Column {
                Text(
                    tutorName ?: "Tutor(a)",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    tutorSubject ?: "-",
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 16.sp
                )
            }
        }

        Spacer(Modifier.height(18.dp))

        MonthCalendar(
            selected = selectedDate,
            onSelect = { selectedDate = it },
            locale = locale,
            zone = zone,
            hasAvailability = hasAvailability
        )

        Spacer(Modifier.height(18.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp)
        ) {
            Text(
                "Escolhe o horário da sessão",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(4.dp))
            Text(
                "Desliza para o lado para ver mais horários disponíveis",
                color = Color.White.copy(alpha = 0.75f),
                fontSize = 13.sp
            )
        }

        Spacer(Modifier.height(12.dp))

        TimeChips(
            times = times,
            selected = selectedTime,
            selectedDate = selectedDate,
            zone = zone
        ) { selectedTime = it }

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
                            .replaceFirstChar { c ->
                                if (c.isLowerCase()) c.titlecase(locale) else "$c"
                            }
                    )
                    selectedTime?.let { append(" • $it") }
                    append(" • $selectedDuration")
                }
            )

            Spacer(Modifier.height(10.dp))

            ConfirmButton(enabled = selectedTime != null && !saving) {
                if (tutorId.isNullOrBlank()) {
                    errorMsg = "Tutor inválido. Volta atrás e escolhe novamente."
                    return@ConfirmButton
                }

                val timeStr = selectedTime ?: return@ConfirmButton
                val parsedTime = runCatching { LocalTime.parse(timeStr) }.getOrNull()
                    ?: return@ConfirmButton

                val startLocal = LocalDateTime.of(selectedDate, parsedTime)
                    .withSecond(0)
                    .withNano(0)

                val minutes = when (selectedDuration) {
                    "30 min" -> 30
                    "1h" -> 60
                    else -> 120
                }

                val endLocal = startLocal.plusMinutes(minutes.toLong())
                val startsAtStr = startLocal.format(isoFmt)
                val endsAtStr = endLocal.format(isoFmt)
                val title = "Sessão com ${tutorName ?: "Tutor(a)"} — ${tutorSubject ?: "-"}"
                val studentId = UserSession.currentUser?.id?.takeIf { it.isNotBlank() }

                val req = CreateSessionRequest(
                    tutorId = tutorId,
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
                        api.createSession(req)
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

@Composable
private fun MonthCalendar(
    selected: LocalDate,
    onSelect: (LocalDate) -> Unit,
    locale: Locale,
    zone: ZoneId,
    hasAvailability: (LocalDate) -> Boolean
) {
    var month by remember { mutableStateOf(YearMonth.from(selected)) }
    val today = remember(zone) { LocalDate.now(zone) }

    LaunchedEffect(selected.year, selected.monthValue) {
        month = YearMonth.from(selected)
    }

    val daysOfWeek = remember {
        listOf(
            DayOfWeek.MONDAY,
            DayOfWeek.TUESDAY,
            DayOfWeek.WEDNESDAY,
            DayOfWeek.THURSDAY,
            DayOfWeek.FRIDAY,
            DayOfWeek.SATURDAY,
            DayOfWeek.SUNDAY
        )
    }

    val firstDayOfMonth = month.atDay(1)
    val daysInMonth = month.lengthOfMonth()
    val firstWeekDayIndex = daysOfWeek.indexOf(DayOfWeek.MONDAY)
    val firstDayIndex = daysOfWeek.indexOf(firstDayOfMonth.dayOfWeek)
    val startOffset = (firstDayIndex - firstWeekDayIndex + 7) % 7
    val totalCells = startOffset + daysInMonth
    val rows = (totalCells + 6) / 7

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextButton(onClick = { month = month.minusMonths(1) }) {
            Text("<", color = Color.White, fontSize = 18.sp)
        }
        Text(
            text = month.month.getDisplayName(TextStyle.FULL_STANDALONE, locale)
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(locale) else "$it" } +
                    " " + month.year,
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )
        TextButton(onClick = { month = month.plusMonths(1) }) {
            Text(">", color = Color.White, fontSize = 18.sp)
        }
    }

    Spacer(Modifier.height(8.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        daysOfWeek.forEach { dow ->
            val label = dow.getDisplayName(TextStyle.NARROW_STANDALONE, locale)
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    label,
                    color = Color.White.copy(alpha = 0.75f),
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }

    Spacer(Modifier.height(4.dp))

    Column {
        var currentDay = 1

        repeat(rows) { rowIndex ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                for (colIndex in 0 until 7) {
                    val cellIndex = rowIndex * 7 + colIndex

                    if (cellIndex < startOffset || currentDay > daysInMonth) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp)
                        )
                    } else {
                        val date = LocalDate.of(month.year, month.month, currentDay)
                        val isPast = date.isBefore(today)
                        val enabled = !isPast && hasAvailability(date)
                        val isSelected = date == selected

                        val bgColor = when {
                            isSelected -> Color.White
                            !enabled -> Color(0x22FFFFFF)
                            else -> Color.Transparent
                        }
                        val borderColor =
                            if (isSelected || !enabled) Color.Transparent else Color(0x55FFFFFF)
                        val textColor = when {
                            isSelected -> Color(0xFF5C54ED)
                            !enabled -> Color.White.copy(alpha = 0.35f)
                            else -> Color.White
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp)
                                .padding(horizontal = 2.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(bgColor)
                                .border(
                                    width = if (bgColor == Color.Transparent) 1.dp else 0.dp,
                                    color = borderColor,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clickable(enabled = enabled) { onSelect(date) },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                currentDay.toString(),
                                color = textColor,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                        currentDay++
                    }
                }
            }
            Spacer(Modifier.height(6.dp))
        }
    }
}

@Composable
private fun TimeChips(
    times: List<String>,
    selected: String?,
    selectedDate: LocalDate,
    zone: ZoneId,
    onSelect: (String) -> Unit
) {
    if (times.isEmpty()) {
        Text(
            "Sem horários disponíveis neste dia.",
            color = Color.White.copy(alpha = 0.9f),
            fontSize = 15.sp
        )
        return
    }

    val today = remember(zone) { LocalDate.now(zone) }
    val nowTime = remember { LocalTime.now(zone) }

    val fmtTimes = remember(times) {
        times.map { value ->
            runCatching {
                val parts = value.split(":")
                "%02d:%02d".format(parts[0].toInt(), parts[1].toInt())
            }.getOrElse { value }
        }
    }

    val nextIndex = remember(fmtTimes, selectedDate, today) {
        if (selectedDate != today) -1 else {
            val now = LocalTime.now(zone)
            fmtTimes.indexOfFirst { value ->
                runCatching { LocalTime.parse(value) }.getOrNull()?.isAfter(now) ?: false
            }
        }
    }

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 6.dp)
    ) {
        itemsIndexed(fmtTimes) { index, item ->
            val timeObj = runCatching { LocalTime.parse(item) }.getOrNull()
            val isPast = selectedDate.isBefore(today) ||
                    (selectedDate == today && timeObj != null && timeObj.isBefore(nowTime))

            val sel = item == selected
            val isNext = !isPast && !sel && index == nextIndex

            val bg = when {
                sel -> Color.White
                isPast -> Color(0x252525FF).copy(alpha = 0.25f)
                else -> Color(0xFF7A6BFF)
            }
            val fg = when {
                sel -> Color(0xFF141414)
                isPast -> Color.White.copy(alpha = 0.45f)
                else -> Color.White
            }

            val borderColor = when {
                sel -> Color.Transparent
                isNext -> Color.White
                else -> Color.Transparent
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(18.dp))
                    .background(bg)
                    .border(
                        width = if (borderColor == Color.Transparent) 0.dp else 2.dp,
                        color = borderColor,
                        shape = RoundedCornerShape(18.dp)
                    )
                    .clickable(enabled = !isPast) { onSelect(item) }
            ) {
                Text(
                    item,
                    color = fg,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(horizontal = 18.dp, vertical = 12.dp)
                )
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
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 6.dp)
    ) {
        itemsIndexed(durations) { index, duration ->
            val sel = duration == selected
            val bg = if (sel) Color.White else Color(0xFF7A6BFF).copy(alpha = 0.85f)
            val fg = if (sel) Color(0xFF141414) else Color.White
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(18.dp))
                    .background(bg)
                    .clickable { onSelect(duration) }
            ) {
                Text(
                    duration,
                    color = fg,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(horizontal = 18.dp, vertical = 12.dp)
                )
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
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
    ) {
        Text(
            "Confirmar sessão",
            color = Color(0xFF141414),
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF5C54ED)
@Composable
private fun AgendarSessaoScreenPreview() {
    MyApplicationPeerly4Theme {
        AgendarSessaoScreen(navController = rememberNavController())
    }
}

// app/src/main/java/com/example/Peerly/scheduling/TimeUtils.kt
package com.example.Peerly.scheduling

import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Janela bloqueada de tempo.
 * endExclusive: o fim NÃO é incluído (ex.: [12:00,13:00) bloqueia 12:00, 12:30… mas não 13:00).
 */
data class TimeWindow(
    val start: LocalTime,
    val endExclusive: LocalTime
)

/**
 * Gera os slots de hora (HH:mm) para um dia.
 *
 * @param date          dia alvo
 * @param zone          fuso (não é usado no cálculo simples, mas mantemos a assinatura)
 * @param blocked       janelas a excluir (ex.: almoço)
 * @param startHour     hora inicial do dia (incluída)
 * @param endHour       hora final do dia (excluída)
 * @param stepMinutes   tamanho do passo em minutos (tipicamente 30)
 */
fun availableTimesFor(
    date: LocalDate,
    zone: ZoneId = ZoneId.systemDefault(),
    blocked: List<TimeWindow> = emptyList(),
    startHour: Int = 9,
    endHour: Int = 18,
    stepMinutes: Int = 30
): List<String> {
    val start = LocalTime.of(startHour, 0)
    val end   = LocalTime.of(endHour, 0)

    // gera sequência 09:00, 09:30, 10:00, …
    val all = mutableListOf<LocalTime>()
    var t = start
    while (t.isBefore(end)) {
        all += t
        t = t.plusMinutes(stepMinutes.toLong())
    }

    // remove os que caem em janelas bloqueadas
    val filtered = all.filter { time ->
        blocked.none { w -> time >= w.start && time < w.endExclusive }
    }

    val fmt = DateTimeFormatter.ofPattern("HH:mm", Locale("pt", "PT"))
    return filtered.map { it.format(fmt) }
}

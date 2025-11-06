package com.example.Peerly.sessions

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDateTime

/**
 * Sessões marcadas localmente (por enquanto sem backend).
 * Depois basta trocar isto por chamadas Retrofit que buscam sessões reais.
 */
object SessionRepository {

    private val _sessions = MutableStateFlow<List<SessionUi>>(emptyList())
    val sessions = _sessions.asStateFlow()

    /** Adiciona uma sessão */
    fun add(session: SessionUi) {
        _sessions.value = _sessions.value + session
    }

    /** Obtém a próxima sessão futura (ou null se não existir) */
    fun next(): SessionUi? {
        val now = LocalDateTime.now()
        return _sessions.value
            .filter { it.start.isAfter(now) }
            .minByOrNull { it.start }
    }

    /** Lista apenas as sessões passadas */
    fun past(): List<SessionUi> {
        val now = LocalDateTime.now()
        return _sessions.value.filter { it.start.isBefore(now) }
    }

    /** Lista apenas as sessões futuras */
    fun upcoming(): List<SessionUi> {
        val now = LocalDateTime.now()
        return _sessions.value.filter { it.start.isAfter(now) }
    }
}

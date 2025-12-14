package com.example.Peerly.sessions

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDateTime

object SessionRepository {

    private val _sessions = MutableStateFlow<List<SessionUi>>(emptyList())
    val sessions = _sessions.asStateFlow()


    fun add(session: SessionUi) {
        _sessions.value = _sessions.value + session
    }


    fun next(): SessionUi? {
        val now = LocalDateTime.now()
        return _sessions.value
            .filter { it.start.isAfter(now) }
            .minByOrNull { it.start }
    }


    fun past(): List<SessionUi> {
        val now = LocalDateTime.now()
        return _sessions.value.filter { it.start.isBefore(now) }
    }


    fun upcoming(): List<SessionUi> {
        val now = LocalDateTime.now()
        return _sessions.value.filter { it.start.isAfter(now) }
    }
}

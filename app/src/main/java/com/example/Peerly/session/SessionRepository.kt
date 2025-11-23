package com.example.Peerly.sessions

import com.example.Peerly.data.RetrofitInstance
import com.example.Peerly.data.model.SessionDto
import com.example.Peerly.data.model.toUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object SessionRepository {

    private val api = RetrofitInstance.api

    private val _sessions = MutableStateFlow<List<SessionUi>>(emptyList())
    val sessions = _sessions.asStateFlow()

    suspend fun refreshFromBackend() {
        val dtos: List<SessionDto> = api.getSessions()
        _sessions.value = dtos.map { it.toUi() }
    }

    fun findById(id: String): SessionUi? =
        _sessions.value.firstOrNull { it.id == id }

    /**
     * Vai ao backend buscar UMA sessão, faz o toUi e atualiza a lista local.
     * Devolve o SessionUi ou null se não existir.
     */
    suspend fun fetchSessionFromBackend(id: String): SessionUi? {
        return try {
            val dto = api.getSessionById(id)
            val ui = dto.toUi()

            val list = _sessions.value.toMutableList()
            val index = list.indexOfFirst { it.id == id }
            if (index >= 0) {
                list[index] = ui
            } else {
                list.add(ui)
            }
            _sessions.value = list
            ui
        } catch (_: Exception) {
            null
        }
    }

    suspend fun finishSession(id: String) {
        val updated = api.updateSessionStatus(
            id,
            mapOf("status" to "finished")
        )
        val ui = updated.toUi()
        val list = _sessions.value.toMutableList()
        val index = list.indexOfFirst { it.id == id }
        if (index >= 0) {
            list[index] = ui
        } else {
            list.add(ui)
        }
        _sessions.value = list
    }

    fun clear() {
        _sessions.value = emptyList()
    }
}

package com.example.Peerly.data

import com.example.Peerly.data.model.ChatMessageDto
import com.example.Peerly.data.model.SendMessageRequest
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException

object ChatRepository {

    private val api = RetrofitInstance.api

    fun messagesForSession(sessionId: String): Flow<List<ChatMessageDto>> = flow {
        while (true) {
            try {
                val msgs = api.getMessagesForSession(sessionId)
                emit(msgs)
            } catch (e: HttpException) {
                if (e.code() == 404) {
                    emit(emptyList())
                } else {
                    emit(emptyList())
                }
            } catch (e: Exception) {
                emit(emptyList())
            }
            delay(1500)
        }
    }

    suspend fun sendMessageToSession(
        sessionId: String,
        senderId: String?,
        text: String
    ): ChatMessageDto {
        val body = SendMessageRequest(
            senderId = senderId,
            content = text,
            type = "text"
        )
        return api.sendMessageToSession(sessionId, body)
    }

    suspend fun generateMeetLinkForSession(sessionId: String): String {
        val resp = api.createMeetLink(sessionId)
        return resp.meetUrl
    }
}

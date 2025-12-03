package com.example.Peerly.data

import com.example.Peerly.data.model.ChatMessageDto
import com.example.Peerly.data.model.CreateReviewRequest
import com.example.Peerly.data.model.CreateSessionRequest
import com.example.Peerly.data.model.LoginRequest
import com.example.Peerly.data.model.MeetLinkResponse
import com.example.Peerly.data.model.SendMessageRequest
import com.example.Peerly.data.model.SessionDto
import com.example.Peerly.data.model.UserCreateRequest
import com.example.Peerly.data.model.UserResponse
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {

    @POST("api/auth/login")
    suspend fun login(@Body body: LoginRequest): UserResponse

    @GET("api/users")
    suspend fun getUsers(): List<UserResponse>

    @POST("api/users")
    suspend fun createUser(@Body body: UserCreateRequest): UserResponse

    @POST("api/users/{id}/update")
    suspend fun updateUser(
        @Path("id") userId: String,
        @Body body: Map<String, @JvmSuppressWildcards Any?>
    ): UserResponse

    @Multipart
    @POST("api/users/{id}/avatar")
    suspend fun uploadUserAvatar(
        @Path("id") userId: String,
        @Part file: MultipartBody.Part
    ): UserResponse

    @Multipart
    @POST("api/tutors/{id}/avatar")
    suspend fun uploadTutorAvatar(
        @Path("id") tutorId: String,
        @Part file: MultipartBody.Part
    ): Map<String, String>

    @POST("api/sessions")
    suspend fun createSession(@Body body: CreateSessionRequest): SessionDto

    @GET("api/sessions")
    suspend fun getSessions(): List<SessionDto>

    @GET("api/sessions/{id}")
    suspend fun getSessionById(@Path("id") id: String): SessionDto

    @PATCH("api/sessions/{id}/status")
    suspend fun updateSessionStatus(
        @Path("id") id: String,
        @Body body: Map<String, String>
    ): SessionDto

    @GET("api/chat/sessions/{sessionId}/messages")
    suspend fun getMessagesForSession(
        @Path("sessionId") sessionId: String
    ): List<ChatMessageDto>

    @POST("api/chat/sessions/{sessionId}/messages")
    suspend fun sendMessageToSession(
        @Path("sessionId") sessionId: String,
        @Body body: SendMessageRequest
    ): ChatMessageDto

    @POST("api/reviews")
    suspend fun createReview(
        @Body body: CreateReviewRequest
    )

    @POST("api/meet/sessions/{sessionId}")
    suspend fun createMeetLink(
        @Path("sessionId") sessionId: String
    ): MeetLinkResponse
}

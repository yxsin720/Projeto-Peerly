package com.example.Peerly.data

import com.example.Peerly.data.model.*
import okhttp3.MultipartBody
import retrofit2.http.*

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
}

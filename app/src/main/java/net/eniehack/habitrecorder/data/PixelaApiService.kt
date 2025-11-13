package net.eniehack.habitrecorder.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.Path

@Serializable
data class PixelaGeneralResponse (
    val message: String,
    val isSuccess: Boolean,
)

@Serializable
data class CreateCheckInRequest (
    val date: String,
    val quantity: String,
)

@Serializable
data class GraphDefinition (
    val id: String,
    val name: String,
    val unit: String,
    val type: String,
    val color: String,
    val timezone: String,
    val startOnMonday: Boolean,
    val purgeCacheURLs: List<String>?,
    val selfSufficient: String,
    val isSecret: Boolean,
    val publishOptionalData: Boolean,
)

@Serializable
data class GetGraphDefinitionsResponse (
    val graphs : List<GraphDefinition>
)

private val BASE_URL = "https://pixe.la/"
private val retrofit = Retrofit.Builder()
    .addConverterFactory(
        Json.asConverterFactory("application/json; charset=UTF-8".toMediaType())
    )
    .baseUrl(BASE_URL)
    .build()

object PixelaApi {
    val retrofitService : PixelaApiService by lazy {
        retrofit.create(PixelaApiService::class.java)
    }
}

interface PixelaApiService {
    @PUT("v1/users/{userId}/graphs/{graphId}/increment")
    suspend fun increment(
        @Header("X-USER-TOKEN") userToken: String,
        @Path("userId") userId: String,
        @Path("graphId") graphId: String,
        @Header("Content-Length") contentLength: Int = 0,
    ): Response<PixelaGeneralResponse>

    @GET("v1/users/{userId}/graphs")
    suspend fun getGraphDefinitions(
        @Header("X-USER-TOKEN") userToken: String,
        @Path("userId") userId: String,
    ): Response<GetGraphDefinitionsResponse>
}
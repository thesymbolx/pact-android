package co.uk.dale.pact_android.data

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Path

interface DisneyService {
    @GET("character")
    suspend fun getCharacters(): DisneyResponse

    @GET("character/{characterId}")
    suspend fun getCharacter(@Path("characterId") characterId: Int): DisneyCharacter
}

data class DisneyResponse(
    val info: Info,
    val data: List<DisneyCharacter>
)

data class Info(
    val totalPages: Int,
    val count: Int,
    val previousPage: String,
    val nextPage: String,
)

data class DisneyCharacter(
    @SerializedName("_id")
    val id: Int,
    val name: String,
    val films: List<String>,
    val tvShows: List<String>,
    val imageUrl: String,
)
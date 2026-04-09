package co.uk.dale.pact_android.data

import retrofit2.Retrofit
import retrofit2.Retrofit.Builder
import retrofit2.converter.gson.GsonConverterFactory

class DisneyRepository(
    val baseUrl: String = "https://api.disneyapi.dev"
) {
    val retrofit: Retrofit = Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    suspend fun getCharacters() : List<DisneyCharacter> {
        val service = retrofit.create(DisneyService::class.java)
        val charactersResponse = service.getCharacters()
        return charactersResponse.data
    }

    suspend fun getCharacter(characterId: Int) : DisneyCharacter {
        val service = retrofit.create(DisneyService::class.java)
        val charactersResponse = service.getCharacter(characterId)
        return charactersResponse
    }
}
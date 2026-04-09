package co.uk.dale.pact_android

import au.com.dius.pact.consumer.MockServer
import au.com.dius.pact.consumer.dsl.PactDslJsonBody
import au.com.dius.pact.consumer.dsl.PactDslWithProvider
import au.com.dius.pact.consumer.junit5.PactConsumerTest
import au.com.dius.pact.consumer.junit5.PactTestFor
import au.com.dius.pact.core.model.RequestResponsePact
import au.com.dius.pact.core.model.annotations.Pact
import co.uk.dale.pact_android.data.DisneyRepository
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test


@PactTestFor(providerName = "disney_api")
@PactConsumerTest
class Junit5PactTest {

    @Pact(consumer = "disney_android_app", provider = "disney_api")
    fun getDisneyCharactersPact(builder: PactDslWithProvider): RequestResponsePact {
        val expectedResponseBody = PactDslJsonBody()
            .eachLike("data")
            .integerType("_id", 112)
            .stringType("name", "Mickey Mouse")
            .stringType("imageUrl", "https://example.com/mickey.png")
            .closeObject()
            ?.closeArray()
            ?: throw Exception("Invalid json")

        return builder
            .given("a list of Disney characters exists")
            .uponReceiving("a request to retrieve all characters")
            .path("/character")
            .method("GET")
            .willRespondWith()
            .status(200)
            .body(expectedResponseBody)
            .toPact()
    }

    @Test
    @PactTestFor(pactMethod = "getDisneyCharactersPact")
    fun `test getting a all characters`(mockServer: MockServer) = runTest {
        val repository = DisneyRepository(baseUrl = mockServer.getUrl())
        val characters = repository.getCharacters()
        val character = characters.first()

        assertEquals(112, character.id)
        assertEquals("Mickey Mouse", character.name)
        assertEquals("https://example.com/mickey.png", character.imageUrl)
    }

    @Pact(consumer = "disney_android_app", provider = "disney_api")
    fun getDisneyCharacter(builder: PactDslWithProvider): RequestResponsePact {
        val expectedResponseBody = PactDslJsonBody()
            .integerType("_id", 112)
            .stringType("name", "Mickey Mouse")
            .stringType("imageUrl", "https://example.com/mickey.png")
            .closeObject()
            ?: throw Exception("Invalid json")

        return builder
            .given("a disney character with id 112 exists")
            .uponReceiving("a request to retrieve character with id 112")
            .path("/character/112")
            .method("GET")
            .willRespondWith()
            .status(200)
            .body(expectedResponseBody)
            .toPact()
    }

    @Test
    @PactTestFor(pactMethod = "getDisneyCharacter")
    fun `test getting a single character`(mockServer: MockServer) = runTest {
        val repository = DisneyRepository(baseUrl = mockServer.getUrl())
        val character = repository.getCharacter(112)

        assertEquals(112, character.id)
        assertEquals("Mickey Mouse", character.name)
        assertEquals("https://example.com/mickey.png", character.imageUrl)
    }
}

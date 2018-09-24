package no.nav.dagpenger.joark.mottak

import au.com.dius.pact.consumer.ConsumerPactBuilder
import au.com.dius.pact.consumer.MockServer
import au.com.dius.pact.consumer.PactTestRun
import au.com.dius.pact.consumer.runConsumerTest
import au.com.dius.pact.model.MockProviderConfig
import org.jetbrains.annotations.NotNull
import org.junit.Assert.assertNotNull
import org.junit.Test
import java.io.IOException
import au.com.dius.pact.consumer.PactVerificationResult
import org.junit.Assert.assertEquals

class JournalPostArkivHttpClientTest {

    @Test
    fun hentInngåendeJournalpost() {

        val pact = ConsumerPactBuilder
                .consumer("joark-v1")
                .hasPactWith("dagpenger-joark-mottak")
                .uponReceiving("GET journalpost basert på jpid")
                .path("/rest/journalfoerinngaaende/v1/journalposter/1")
                .matchHeader("Authorization", "Bearer\\s[\\d|a-f]{8}-([\\d|a-f]{4}-){3}[\\d|a-f]{12}")
                .method("GET")
                .willRespondWith()
                .status(200)
                .body("{\"dokument\": \"dagpenger\"}")
                .toPact()

        val config = MockProviderConfig.createDefault()

        val result = runConsumerTest(pact, config, object : PactTestRun {
            @Throws(IOException::class)
            override fun run(@NotNull mockServer: MockServer) {
                val joarkClient = JournalPostArkivHttpClient(config.url())
                val result = joarkClient.hentInngåendeJournalpost("1")
                assertNotNull(result)
            }
        })

        if (result is PactVerificationResult.Error) {
            throw RuntimeException(result.getDescription(), result.error)
        }

        assertEquals(PactVerificationResult.Ok, result)
    }
}
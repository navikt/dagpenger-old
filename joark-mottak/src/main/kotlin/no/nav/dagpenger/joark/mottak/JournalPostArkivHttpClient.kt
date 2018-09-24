package no.nav.dagpenger.joark.mottak

import okhttp3.OkHttpClient
import okhttp3.Request
import java.lang.RuntimeException
import java.util.*

class JournalPostArkivHttpClient(final val joarkBaseUrl: String) : JournalpostArkiv {

    companion object {
        val okHttpClient: OkHttpClient = OkHttpClient()
    }

    override fun hentInngåendeJournalpost(journalpostId: String): String {

        val token = UUID.randomUUID().toString()
        val request: Request = Request.Builder()
                .url("$joarkBaseUrl/rest/journalfoerinngaaende/v1/journalposter/$journalpostId")
                .header("Authorization", BearerToken(token).value())
                .build()

        val payload = okHttpClient.newCall(request).execute().use { response ->
            if (response.isSuccessful) {
                response.body()?.string()
            } else {
                when (response.code()) {
                    400 -> throw BadRequestExeption(response.message())
                    401 -> throw NotAuthorizedException(response.message())
                    403 -> throw ForbiddenException(response.message())
                    404 -> throw NotFoundException(response.message())
                    else -> throw ClientException(response.message())
                }
            }
        }

        return payload.orEmpty()
    }
}

data class BearerToken(val token: String) {
    fun value(): String = "Bearer $token"
}

open class ClientException(override val message: String) : RuntimeException(message)
class NotFoundException(override val message: String) : ClientException(message)
class ForbiddenException(override val message: String) : ClientException(message)
class NotAuthorizedException(override val message: String) : ClientException(message)
class BadRequestExeption(override val message: String) : ClientException(message)

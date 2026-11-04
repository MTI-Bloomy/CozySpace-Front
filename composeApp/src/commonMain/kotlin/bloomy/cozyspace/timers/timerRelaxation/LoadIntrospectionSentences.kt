package bloomy.cozyspace.timers.timerRelaxation

import cozyspace.composeapp.generated.resources.Res
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class IntrospectionSentences(
    val sentences: List<String>
)

private val json = Json { ignoreUnknownKeys = true }

suspend fun loadIntrospectionSentences(): List<String> =
    runCatching {
        val text = Res.readBytes("files/introspection_sentences.json").decodeToString()
        json.decodeFromString<IntrospectionSentences>(text).sentences
    }.getOrDefault(emptyList())

package bloomy.cozyspace.network

import bloomy.cozyspace.config.Environment
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.http.isSuccess
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

class NetworkMonitor(
    private val client: HttpClient,
    private val scope: CoroutineScope,
    private val healthCheckUrl: String = "${Environment.API_URL}/actuator/health",
    private val intervalMs: Duration = 15_000L.milliseconds,
) {
    private val _isOnline = MutableStateFlow(true) // optimiste, évite un flash "offline" au démarrage
    val isOnline: StateFlow<Boolean> = _isOnline.asStateFlow()

    private var job: Job? = null

    fun start() {
        if (job?.isActive == true) return
        job = scope.launch {
            while (isActive) {
                _isOnline.value = check()
                delay(intervalMs)
            }
        }
    }

    fun stop() = job?.cancel()

    // Appelé par safeApiCall juste après un échec, pour réagir plus vite que l'intervalle
    suspend fun refreshNow() {
        _isOnline.value = check()
    }

    private suspend fun check(): Boolean = try {
        withTimeout(5_000L.milliseconds) {
            client.get(healthCheckUrl) { skipAuth() }.status.isSuccess()
        }
    } catch (e: Exception) { false }
}

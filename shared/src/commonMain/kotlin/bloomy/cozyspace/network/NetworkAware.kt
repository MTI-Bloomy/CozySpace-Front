package bloomy.cozyspace.network

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

object NetworkAwareAction

class NetworkAwareBootstrapper : CoroutineBootstrapper<NetworkAwareAction>() {
    override fun invoke() {
        dispatch(NetworkAwareAction)
    }
}

abstract class NetworkAwareExecutor<Intent : Any, State : Any, Msg : Any, Label : Any>(
    private val networkMonitor: NetworkMonitor,
) : CoroutineExecutor<Intent, NetworkAwareAction, State, Msg, Label>() {

    final override fun executeAction(action: NetworkAwareAction) {
        scope.launch {
            networkMonitor.isOnline
                .filter { it }
                .collect { onReconnect() }
        }
    }

    /** Appelé à chaque retour en ligne. Implémente ici le rejeu propre à CE store. */
    protected abstract suspend fun onReconnect()
}

package bloomy.cozyspace.utils

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import com.arkivanov.mvikotlin.core.rx.observer
import com.arkivanov.mvikotlin.core.store.Store
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun <T : Any> ObserveErrors(
    store: Store<*, *, T>,
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope,
    extractError: (T) -> String?,
) {
    DisposableEffect(store) {
        val disposable = store.labels(observer { label ->
            extractError(label)?.let { message ->
                scope.launch { snackbarHostState.showSnackbar(message) }
            }
        })
        onDispose { disposable.dispose() }
    }
}

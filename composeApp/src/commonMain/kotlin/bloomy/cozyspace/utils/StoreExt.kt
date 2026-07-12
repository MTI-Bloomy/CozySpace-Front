package bloomy.cozyspace.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.arkivanov.mvikotlin.core.rx.observer
import com.arkivanov.mvikotlin.core.store.Store

@Composable
fun <State : Any> Store<*, State, *>.observeState(): State {
    var state by remember(this) { mutableStateOf(this.state) }

    DisposableEffect(this) {
        val disposable = states(observer { newState -> state = newState })
        onDispose { disposable.dispose() }
    }

    return state
}

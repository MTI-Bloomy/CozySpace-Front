package bloomy.cozyspace

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import bloomy.cozyspace.data.JokeRepository
import bloomy.cozyspace.network.ApiService
import bloomy.cozyspace.network.createHttpClient
import bloomy.cozyspace.store.JokeStore
import bloomy.cozyspace.store.JokeStoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow

@Composable
@Preview
fun App() {
    MaterialTheme {
        val jokeStore =
            remember {
                JokeStoreFactory(
                    JokeRepository(
                        ApiService(
                            createHttpClient()
                        )
                    )
                ).create().also { it.init() }
            }
        val scope = rememberCoroutineScope()
        val jokeFlow = remember(jokeStore, scope) { jokeStore.stateFlow(scope) }
        val joke by jokeFlow.collectAsState()

        DisposableEffect(jokeStore) {
            onDispose {
                jokeStore.dispose()
            }
        }

        Column(
            modifier =
                Modifier
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .safeContentPadding()
                    .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Button(
                onClick = {
                    jokeStore.accept(JokeStore.Intent.LoadJoke)
                }
            ) {
                Text("Load joke")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    jokeStore.accept(JokeStore.Intent.LoadDevJoke)
                }
            ) {
                Text("Load dev joke")
            }

            Text(text = if (joke.loading) "Loading..." else joke.joke.text)
            Text(text = if (joke.loading) "" else joke.joke.answer)

            joke.error?.let { error ->
                Text(text = error)
            }
        }
    }
}

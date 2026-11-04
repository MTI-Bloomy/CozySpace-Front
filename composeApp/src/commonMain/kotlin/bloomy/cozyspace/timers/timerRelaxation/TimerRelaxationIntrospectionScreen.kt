package bloomy.cozyspace.timers.timerRelaxation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bloomy.cozyspace.theme.DarkGreen
import bloomy.cozyspace.theme.WhiteBackground
import bloomy.cozyspace.timers.components.CancelButton
import bloomy.cozyspace.timers.components.CharacterInteraction
import cozyspace.composeapp.generated.resources.Res
import cozyspace.composeapp.generated.resources.happyCharacter
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.minutes

private val QUESTION_INTERVAL = 5.minutes
private val SENTENCE_INTERVAL = 2.minutes

@Composable
fun TimerRelaxationIntrospectionScreen(
    onBack: () -> Unit = {},
    onFinish: () -> Unit = {}
) {
    var showQuestion by remember { mutableStateOf(false) }

    var sentences by remember { mutableStateOf<List<String>>(emptyList()) }
    var sentenceIndex by remember { mutableStateOf(0) }

    // Load json
    LaunchedEffect(Unit) {
        sentences = loadIntrospectionSentences().shuffled()
    }

    // Changes the sentence
    LaunchedEffect(sentences) {
        if (sentences.size > 1) {
            while (true) {
                delay(SENTENCE_INTERVAL)
                sentenceIndex = (sentenceIndex + 1) % sentences.size
            }
        }
    }

    // showQuestion changes :
    // - at the start of the exercise (false) => waiting for 5 minutes, then showing the popup
    // - when closing the popup (false) => waiting for 5 minutes
    // - when the popup is shown (true) => waiting for the popup to be closed
    LaunchedEffect(showQuestion) {
        if (!showQuestion) {
            delay(QUESTION_INTERVAL)
            showQuestion = true
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(WhiteBackground)
            .windowInsetsPadding(WindowInsets.systemBars)
            .padding(horizontal = 24.dp, vertical = 16.dp),
        contentAlignment = Alignment.Center,
    ) {
        AnimatedContent(
            targetState = sentences.getOrNull(sentenceIndex).orEmpty(),
            transitionSpec = {
                fadeIn(tween(800)) togetherWith fadeOut(tween(800))
            },
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(),
            label = "introspectionSentence"
        ) { sentence ->
            Text(
                text = sentence,
                color = DarkGreen,
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp,
                lineHeight = 42.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth()
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AnimatedVisibility(
                visible = showQuestion,
                enter = fadeIn() + slideInVertically { it / 2 },
                exit = fadeOut() + slideOutVertically { it / 2 }
            ) {
                CharacterInteraction(
                    character = Res.drawable.happyCharacter,
                    question = "Are you feeling better ?",
                    confirmAnswer = "Yes",
                    cancelAnswer = "No",
                    onConfirm = onFinish,
                    onCancel = { showQuestion = false },
                )
            }

            CancelButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = onBack
            )
        }
    }
}

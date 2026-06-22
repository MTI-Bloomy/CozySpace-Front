package bloomy.cozyspace.auth.common

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import bloomy.cozyspace.theme.WhiteBackground
import androidx.compose.ui.Modifier

@Composable
fun AuthLayout(
    isCompact: Boolean,
    keyboardOpen: Boolean,
    isSignUp: Boolean = false,
    header: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    val headerWeight by animateFloatAsState(
        targetValue = if (keyboardOpen) 0.65f else 1.5f
    )

    val contentWeight by animateFloatAsState(
        targetValue = if (keyboardOpen) 1.35f else 1f
    )

    if (isCompact) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(WhiteBackground)
                .padding(horizontal = 20.dp)
                .imePadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(if (keyboardOpen) 0.4f else 1f),
                contentAlignment = Alignment.Center
            ) {
                if (isSignUp) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        header()
                    }
                } else {
                    header()
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.TopCenter
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    content()
                }
            }
        }
    } else {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(WhiteBackground)
                .padding(20.dp)
                .imePadding(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.weight(
                    if (keyboardOpen) 1f else 1.5f
                ),
                contentAlignment = Alignment.Center
            ) {
                header()
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .widthIn(max = 500.dp)
                    .fillMaxWidth()
            ) {
                content()
            }
        }
    }
}

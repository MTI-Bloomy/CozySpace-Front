package bloomy.cozyspace.auth.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import bloomy.cozyspace.theme.WhiteBackground
import androidx.compose.ui.Modifier

@Composable
fun AuthLayout(isCompact: Boolean, isSignUp: Boolean = false, headerWeight: Float = 1.5f, header: @Composable () -> Unit, content: @Composable () -> Unit) {
    if (isCompact) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(WhiteBackground)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.weight(headerWeight),
                contentAlignment = Alignment.Center
            ) {
                if (isSignUp) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        header()
                    }
                }

                else {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        header()
                    }
                }
            }

            Box(modifier = Modifier
                .weight(1f)
                .widthIn(max = 500.dp)
                .fillMaxWidth()
                .imePadding()
            ) {
                content()
            }
        }
    } else {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(WhiteBackground)
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.weight(1.5f),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    header()
                }
            }

            Box(modifier = Modifier
                .weight(1f)
                .widthIn(max = 500.dp)
                .fillMaxWidth()
                .imePadding()
            ) {
                content()
            }
        }
    }
}

package bloomy.cozyspace

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bloomy.cozyspace.theme.DarkGreen
import bloomy.cozyspace.theme.WhiteBackground
import cozyspace.composeapp.generated.resources.Res
import cozyspace.composeapp.generated.resources.cozyspace_logo
import org.jetbrains.compose.resources.painterResource

@Composable
fun InitScreen(onLoginSuccess: () -> Unit, onLoginFailed: () -> Unit) {
    val email by remember { mutableStateOf("") }
    val password by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().background(WhiteBackground).padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly) {

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(40.dp))

            Image(
                painter = painterResource(Res.drawable.cozyspace_logo),
                contentDescription = "CozySpace Logo",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.size(250.dp)
                    .clip(RoundedCornerShape(24.dp))
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text("Welcome to CozySpace !",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = DarkGreen,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(64.dp))
        }

        Column(modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = onLoginSuccess,
                modifier = Modifier.fillMaxWidth().height(55.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = DarkGreen,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "Sign In",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onLoginSuccess,
                modifier = Modifier.fillMaxWidth().height(55.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = DarkGreen,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "Sign Up",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

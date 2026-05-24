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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bloomy.cozyspace.theme.DarkGreen
import bloomy.cozyspace.theme.WhiteBackground
import cozyspace.composeapp.generated.resources.Res
import cozyspace.composeapp.generated.resources.cozyspace_logo
import cozyspace.composeapp.generated.resources.signIn_lock
import cozyspace.composeapp.generated.resources.signIn_person
import cozyspace.composeapp.generated.resources.signIn_visibility
import cozyspace.composeapp.generated.resources.signIn_visibility_off
import org.jetbrains.compose.resources.painterResource

@Composable
@Preview
fun SignInScreen(onLoginSuccess: () -> Unit = {}, onSignUp: () -> Unit = {}) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

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

            Text("Welcome back !",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = DarkGreen,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(64.dp))
        }

        Column(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("Username / Email")
                },
                leadingIcon = {
                    Icon(
                        painter = painterResource(Res.drawable.signIn_person),
                        contentDescription = "",
                    )
                },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = DarkGreen,
                    focusedLabelColor = DarkGreen,
                    cursorColor = DarkGreen
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Password") },
                visualTransformation =
                    if (passwordVisible)
                        VisualTransformation.None
                    else
                        PasswordVisualTransformation(),

                leadingIcon = {
                    Icon(
                        painter = painterResource(Res.drawable.signIn_lock),
                        contentDescription = "",
                    )
                },
                trailingIcon = {
                    IconButton(onClick = {
                        passwordVisible = !passwordVisible
                    }) {
                        Icon(
                            painter = painterResource(
                                if (passwordVisible)
                                    Res.drawable.signIn_visibility
                                else
                                    Res.drawable.signIn_visibility_off
                            ),
                            contentDescription = null
                        )
                    }
                },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = DarkGreen,
                    focusedLabelColor = DarkGreen,
                    cursorColor = DarkGreen
                )
            )

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = onLoginSuccess,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = DarkGreen
                )
            ) {
                Text(
                    text = "Login",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            TextButton(
                onClick = onSignUp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = "Don't have an account ? Register",
                    color = DarkGreen
                )
            }
        }
    }
}

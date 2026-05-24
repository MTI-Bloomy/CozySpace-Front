package bloomy.cozyspace.login

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bloomy.cozyspace.login.signUp.isCreatedEmailValid
import bloomy.cozyspace.login.signUp.isCreatedPasswordConfirmationValid
import bloomy.cozyspace.login.signUp.isCreatedPasswordValid
import bloomy.cozyspace.theme.DarkGreen
import bloomy.cozyspace.theme.WhiteBackground
import cozyspace.composeapp.generated.resources.Res
import cozyspace.composeapp.generated.resources.cozyspace_logo
import cozyspace.composeapp.generated.resources.createAccount_check
import cozyspace.composeapp.generated.resources.createAccount_email
import cozyspace.composeapp.generated.resources.signIn_lock
import cozyspace.composeapp.generated.resources.signIn_person
import cozyspace.composeapp.generated.resources.signIn_visibility
import cozyspace.composeapp.generated.resources.signIn_visibility_off
import org.jetbrains.compose.resources.painterResource

@Composable
@Preview
fun SignUpScreen(onSignUpSuccess: () -> Unit = {}, onSignInSuccess: () -> Unit = {}) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordConfirmation by remember { mutableStateOf("") }

    var isPasswordVisible by remember { mutableStateOf(false) }
    var isPasswordConfirmationVisible by remember { mutableStateOf(false) }

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

            Spacer(modifier = Modifier.height(32.dp))
        }

        Column(modifier = Modifier.fillMaxWidth()) {

            // USERNAME
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("Username")
                },
                leadingIcon = {
                    Icon(
                        painter = painterResource(
                            if (isCreatedPasswordValid(password).isSuccess)
                                Res.drawable.createAccount_check
                            else
                                Res.drawable.signIn_person
                        ),
                        contentDescription = "",
                        tint = if (isCreatedPasswordConfirmationValid(password, passwordConfirmation))
                            DarkGreen
                        else
                            Color.Black
                    )
                },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = DarkGreen,
                    focusedLabelColor = DarkGreen,
                    cursorColor = DarkGreen
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            // EMAIL
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("Email")
                },
                leadingIcon = {
                    Icon(
                        painter = painterResource(
                            if (isCreatedEmailValid(email))
                                Res.drawable.createAccount_check
                            else
                                Res.drawable.createAccount_email
                        ),
                        contentDescription = "",
                        tint = if (isCreatedEmailValid(email))
                            DarkGreen
                        else
                            Color.Black
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

            // PASSWORD
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Password") },
                visualTransformation =
                    if (isPasswordVisible)
                        VisualTransformation.None
                    else
                        PasswordVisualTransformation(),

                leadingIcon = {
                    Icon(
                        painter = painterResource(
                            if (isCreatedPasswordValid(password).isSuccess)
                                Res.drawable.createAccount_check
                            else
                                Res.drawable.signIn_lock
                        ),
                        contentDescription = "",
                        tint = if (isCreatedPasswordValid(password).isSuccess)
                            DarkGreen
                        else
                            Color.Black
                    )},

                trailingIcon = {
                    IconButton(onClick = {
                        isPasswordVisible = !isPasswordVisible
                    }) {
                        Icon(
                            painter = painterResource(
                                if (isPasswordVisible)
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

            Spacer(modifier = Modifier.height(10.dp))

            // PASSWORD CONFIRMATION
            OutlinedTextField(
                value = passwordConfirmation,
                onValueChange = { passwordConfirmation = it },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Password confirmation") },
                visualTransformation =
                    if (isPasswordConfirmationVisible)
                        VisualTransformation.None
                    else
                        PasswordVisualTransformation(),

                leadingIcon = {
                    Icon(
                        painter = painterResource(
                            if (isCreatedPasswordConfirmationValid(password, passwordConfirmation))
                                Res.drawable.createAccount_check
                            else
                                Res.drawable.signIn_lock
                        ),
                        contentDescription = "",
                        tint = if (isCreatedPasswordConfirmationValid(password, passwordConfirmation))
                            DarkGreen
                        else
                            Color.Black
                    )
                },
                trailingIcon = {
                    IconButton(onClick = {
                        isPasswordConfirmationVisible = !isPasswordConfirmationVisible
                    }) {
                        Icon(
                            painter = painterResource(
                                if (isPasswordConfirmationVisible)
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
                onClick = onSignUpSuccess,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = DarkGreen
                )
            ) {
                Text(
                    text = "Let's get started !",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            TextButton(
                onClick = onSignInSuccess,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = "Already an account ? Sign in",
                    color = DarkGreen
                )
            }
        }
    }
}

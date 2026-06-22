package bloomy.cozyspace.auth.signUp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bloomy.cozyspace.auth.component.PasswordStrengthBar
import bloomy.cozyspace.theme.DarkGreen
import bloomy.cozyspace.theme.LightGreen
import bloomy.cozyspace.theme.WhiteBackground
import cozyspace.composeapp.generated.resources.*
import org.jetbrains.compose.resources.painterResource

@Composable
fun SignUpForm(onSignUpSuccess: () -> Unit = {}, onSignInSuccess: () -> Unit = {}) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordConfirmation by remember { mutableStateOf("") }

    var isPasswordVisible by remember { mutableStateOf(false) }
    var isPasswordConfirmationVisible by remember { mutableStateOf(false) }
    var isPasswordTouched by remember { mutableStateOf(false) }

    val isFormValid =
        username.isNotBlank() &&
            isCreatedEmailValid(email) &&
            isCreatedPasswordValid(password) &&
            isCreatedPasswordConfirmationValid(password, passwordConfirmation)

    Card(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.6f)
        )
    ) {
        Column(modifier = Modifier.padding(24.dp)
        ) {
            // USERNAME
            TextField(
                value = username,
                onValueChange = { username = it },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text("Username")
                },
                textStyle = TextStyle.Default.copy(fontSize = 20.sp),
                leadingIcon = {
                    Icon(
                        painter = painterResource(
                            if (isCreatedUsernameValid(username))
                                Res.drawable.createAccount_check
                            else
                                Res.drawable.signIn_person
                        ),
                        contentDescription = "",
                        tint = if (isCreatedUsernameValid(username))
                            DarkGreen
                        else
                            WhiteBackground
                    )
                },
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = LightGreen,
                    unfocusedContainerColor = LightGreen.copy(alpha = 0.85f),

                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,

                    focusedTextColor = WhiteBackground,
                    unfocusedTextColor = WhiteBackground,

                    focusedPlaceholderColor = WhiteBackground,
                    unfocusedPlaceholderColor = WhiteBackground.copy(alpha = 0.7f),

                    cursorColor = DarkGreen
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            // EMAIL
            TextField(
                value = email,
                onValueChange = { email = it },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text("Email")
                },
                textStyle = TextStyle.Default.copy(fontSize = 20.sp),
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
                            WhiteBackground
                    )
                },
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = LightGreen,
                    unfocusedContainerColor = LightGreen.copy(alpha = 0.85f),

                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,

                    focusedTextColor = WhiteBackground,
                    unfocusedTextColor = WhiteBackground,

                    focusedPlaceholderColor = WhiteBackground,
                    unfocusedPlaceholderColor = WhiteBackground.copy(alpha = 0.7f),

                    cursorColor = DarkGreen
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            // PASSWORD
            TextField(
                value = password,
                onValueChange = {
                    password = it
                    isPasswordTouched = true
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text("Password")
                },
                textStyle = TextStyle.Default.copy(fontSize = 20.sp),
                visualTransformation =
                    if (isPasswordVisible)
                        VisualTransformation.None
                    else
                        PasswordVisualTransformation(),
                leadingIcon = {
                    Icon(
                        painter = painterResource(
                            if (isCreatedPasswordValid(password))
                                Res.drawable.createAccount_check
                            else
                                Res.drawable.signIn_lock
                        ),
                        contentDescription = "",
                        tint = if (isCreatedPasswordValid(password))
                            DarkGreen
                        else
                            WhiteBackground
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
                            contentDescription = null,
                            tint = WhiteBackground
                        )
                    }
                },
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = LightGreen,
                    unfocusedContainerColor = LightGreen.copy(alpha = 0.85f),

                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,

                    focusedTextColor = WhiteBackground,
                    unfocusedTextColor = WhiteBackground,

                    focusedPlaceholderColor = WhiteBackground,
                    unfocusedPlaceholderColor = WhiteBackground.copy(alpha = 0.7f),

                    cursorColor = DarkGreen
                )
            )

            Spacer(Modifier.height(8.dp))

            PasswordStrengthBar(password)

            Spacer(Modifier.height(8.dp))

            // PASSWORD CONFIRMATION
            TextField(
                value = passwordConfirmation,
                onValueChange = { passwordConfirmation = it },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text("Password confirmation")
                },
                textStyle = TextStyle.Default.copy(fontSize = 20.sp),
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
                            WhiteBackground
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
                            contentDescription = null,
                            tint = WhiteBackground
                        )
                    }
                },
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = LightGreen,
                    unfocusedContainerColor = LightGreen.copy(alpha = 0.85f),

                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,

                    focusedTextColor = WhiteBackground,
                    unfocusedTextColor = WhiteBackground,

                    focusedPlaceholderColor = WhiteBackground,
                    unfocusedPlaceholderColor = WhiteBackground.copy(alpha = 0.7f),

                    cursorColor = DarkGreen
                )
            )

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = {
                    if (isFormValid) onSignUpSuccess()
                },
                enabled = isFormValid,
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

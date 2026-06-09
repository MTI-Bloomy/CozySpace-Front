package bloomy.cozyspace.auth.signUp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bloomy.cozyspace.theme.DarkGreen
import cozyspace.composeapp.generated.resources.Res
import cozyspace.composeapp.generated.resources.createAccount_check
import cozyspace.composeapp.generated.resources.createAccount_email
import cozyspace.composeapp.generated.resources.signIn_lock
import cozyspace.composeapp.generated.resources.signIn_person
import cozyspace.composeapp.generated.resources.signIn_visibility
import cozyspace.composeapp.generated.resources.signIn_visibility_off
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
    val passwordError = if (isPasswordTouched) getPasswordError(password) else null

    val isFormValid =
        username.isNotBlank() &&
            isCreatedEmailValid(email) &&
            isCreatedPasswordValid(password) &&
            isCreatedPasswordConfirmationValid(password, passwordConfirmation)

    Column(modifier = Modifier
        .widthIn(max = 500.dp)
        .fillMaxWidth()
        .imePadding(),
        verticalArrangement = Arrangement.Bottom
    ) {
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
                        if (isCreatedUsernameValid(username))
                            Res.drawable.createAccount_check
                        else
                            Res.drawable.signIn_person
                    ),
                    contentDescription = "",
                    tint = if (isCreatedUsernameValid(username))
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

        Spacer(modifier = Modifier.height(10.dp))

        // PASSWORD
        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                isPasswordTouched = true
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Password") },
            supportingText = {
                if (passwordError != null) {
                    Text(
                        text = passwordError,
                        color = Color.Red
                    )
                }
            },
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

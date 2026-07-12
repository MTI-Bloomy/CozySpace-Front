package bloomy.cozyspace.auth.signIn

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import bloomy.cozyspace.data.dto.LoginRequestDto
import bloomy.cozyspace.navigation.screenRoutes.Screen
import bloomy.cozyspace.store.Stores
import bloomy.cozyspace.store.UserStore
import bloomy.cozyspace.theme.DarkGreen
import bloomy.cozyspace.theme.LightGreen
import bloomy.cozyspace.theme.WhiteBackground
import cozyspace.composeapp.generated.resources.Res
import cozyspace.composeapp.generated.resources.signIn_lock
import cozyspace.composeapp.generated.resources.signIn_person
import cozyspace.composeapp.generated.resources.signIn_visibility
import cozyspace.composeapp.generated.resources.signIn_visibility_off
import org.jetbrains.compose.resources.painterResource

@Composable
fun SignInForm(navController: NavHostController, stores: Stores) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

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
            TextField(
                value = email,
                onValueChange = { email = it },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text("Username / Email")
                },
                textStyle = TextStyle.Default.copy(fontSize = 20.sp),
                leadingIcon = {
                    Icon(
                        painter = painterResource(Res.drawable.signIn_person),
                        contentDescription = "",
                        tint = WhiteBackground,
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

            Spacer(modifier = Modifier.height(20.dp))

            TextField(
                value = password,
                onValueChange = { password = it },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text("Password")
                },
                textStyle = TextStyle.Default.copy(fontSize = 20.sp),
                visualTransformation =
                    if (passwordVisible)
                        VisualTransformation.None
                    else
                        PasswordVisualTransformation(),

                leadingIcon = {
                    Icon(
                        painter = painterResource(Res.drawable.signIn_lock),
                        contentDescription = "",
                        tint = WhiteBackground,
                    )
                },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            passwordVisible = !passwordVisible
                        }
                    ) {
                        Icon(
                            painter = painterResource(
                                if (passwordVisible)
                                    Res.drawable.signIn_visibility
                                else
                                    Res.drawable.signIn_visibility_off
                            ),
                            contentDescription = null,
                            tint = WhiteBackground,
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
                    stores.user.accept(
                        UserStore.Intent.Login(
                            LoginRequestDto(
                                email = email,
                                password = password
                            )
                    ))
                },
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
                onClick = {
                    navController.navigate(Screen.CreateAccount.route)
                },
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

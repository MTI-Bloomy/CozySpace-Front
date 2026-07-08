package bloomy.cozyspace.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import bloomy.cozyspace.store.Stores
import bloomy.cozyspace.store.UserStore

@Composable
fun HomeMain(stores: Stores) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Button(
            onClick = {
                stores.user.accept(UserStore.Intent.Logout)
            }
        ) {
            Text("Log out")
        }
    }
}

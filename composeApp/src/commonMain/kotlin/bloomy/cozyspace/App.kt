package bloomy.cozyspace

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import bloomy.cozyspace.navigation.NavGraph

@Composable
@Preview
fun App() {
    val navController = rememberNavController()
    NavGraph(navController)
}

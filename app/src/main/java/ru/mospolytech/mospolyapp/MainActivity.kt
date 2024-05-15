package ru.mospolytech.mospolyapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.mospolytech.mospolyapp.apicalls.LoginViewModel
import ru.mospolytech.mospolyapp.screens.LoadingInfo
import ru.mospolytech.mospolyapp.screens.LoginScreen
import ru.mospolytech.mospolyapp.screens.MainScreen
import ru.mospolytech.mospolyapp.ui.theme.MosPolyAppTheme

data class TabBarItem(
    val title: String,
    val icon: ImageVector,
)
class MainActivity : ComponentActivity() {

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TokenManager.initialize(this)
        setContent {
            val navController = rememberNavController()
            MosPolyAppTheme {
                AppNavigation(navController)

            }
        }
    }
}

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "loginScreen") {
        composable("loginScreen") {
            val loginViewModel: LoginViewModel = viewModel()
            LoginScreen(loginViewModel, navController)
        }
        composable("mainScreen") {
            LoadingInfo()
        }
    }
}
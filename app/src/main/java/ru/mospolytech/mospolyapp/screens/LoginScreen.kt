package ru.mospolytech.mospolyapp.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import ru.mospolytech.mospolyapp.ButtonComponent
import ru.mospolytech.mospolyapp.PasswordFieldComponent
import ru.mospolytech.mospolyapp.TextFieldComponent
import ru.mospolytech.mospolyapp.apicalls.LoginViewModel
import ru.mospolytech.mospolyapp.apicalls.UIEvent
import androidx.compose.ui.unit.sp
import ru.mospolytech.mospolyapp.R
import ru.mospolytech.mospolyapp.TokenManager


@Composable
fun LoginScreen(loginViewModel: LoginViewModel = viewModel(), navController: NavHostController){
    val isChecked = remember { mutableStateOf(true) }
    val resultState = loginViewModel.tokenLiveData.observeAsState()
    val tokenlogin = resultState.value
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFF2e2e2e))
            .padding(30.dp)
    ){
        Column(modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFF2e2e2e))){
            Image(painter = painterResource(id = R.drawable.logo_darkmode), contentDescription = "logo", modifier = Modifier
                .size(50.dp) // Set desired image size
                .align(Alignment.CenterHorizontally))
            Spacer(modifier = Modifier.weight(1f))
            TextFieldComponent(
                labelValue = "Введите логин",
                onTextSelected = {loginViewModel.onEvent((UIEvent.LoginChanged(it)))},
                errorStatus = !loginViewModel.loginUIState.value.inputError
                )
            Spacer(modifier = Modifier.height(10.dp))
            PasswordFieldComponent(labelValue = "Введите пароль",
                onTextSelected = {loginViewModel.onEvent((UIEvent.PasswordChanged(it)))},
                errorStatus = !loginViewModel.loginUIState.value.inputError
            )
            Row {
                Checkbox( // Checkbox element
                    checked = isChecked.value,
                    onCheckedChange = { isChecked.value = it },
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color(0xFF567dff))
                )
                Spacer(modifier = Modifier.width(8.dp)) // Spacing between checkbox and text
                Text(text = "Оставаться в системе",
                    fontSize = 16.sp,
                    fontFamily = montserrat,
                    color = Color.White,
                    modifier = Modifier.align(CenterVertically)
                )
            }
            ButtonComponent(value = "Войти", onButtonClicked = {
                loginViewModel.onEvent((UIEvent.LoginButtonClicked))
            })
            LaunchedEffect(tokenlogin) {
                if (tokenlogin != null) {
                    if (tokenlogin != "") {
                        Log.d("tag", tokenlogin)
                        navController.navigate("mainScreen")
                    }

                }
            }
            Spacer(modifier = Modifier.weight(1.55f))
        }
    }

}


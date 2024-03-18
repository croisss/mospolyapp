package ru.mospolytech.mospolyapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ru.mospolytech.mospolyapp.ButtonComponent
import ru.mospolytech.mospolyapp.HeadingTextComponent
import ru.mospolytech.mospolyapp.PasswordFieldComponent
import ru.mospolytech.mospolyapp.TextFieldComponent
import ru.mospolytech.mospolyapp.data.LoginViewModel
import ru.mospolytech.mospolyapp.data.UIEvent
import ru.mospolytech.mospolyapp.data.LoginUIState
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.unit.sp
import ru.mospolytech.mospolyapp.R



@Composable
fun LoginScreen(loginViewModel: LoginViewModel = viewModel(), navController: NavHostController){
    val isChecked = remember { mutableStateOf(true) }
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
                errorStatus = loginViewModel.loginUIState.value.loginError
                )
            Spacer(modifier = Modifier.height(10.dp))
            PasswordFieldComponent(labelValue = "Введите пароль",
                onTextSelected = {loginViewModel.onEvent((UIEvent.PasswordChanged(it)))},
                errorStatus = loginViewModel.loginUIState.value.passwordError
            )

            Row (){
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
                if (loginViewModel.loginUIState.value.loginError && loginViewModel.loginUIState.value.passwordError &&
                    loginViewModel.loginUIState.value.login != "" && loginViewModel.loginUIState.value.password != "")
                    navController.navigate("mainScreen")
                //ButtonComponent(value = "Log in", onButtonClicked = {loginViewModel.onEvent(UIEvent.LoginButtonClicked)})
            })
            Spacer(modifier = Modifier.weight(1.55f))
        }
    }

}


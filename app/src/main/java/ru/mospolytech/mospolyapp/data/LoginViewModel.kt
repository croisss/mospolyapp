package ru.mospolytech.mospolyapp.data

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class LoginViewModel: ViewModel() {
    var loginUIState = mutableStateOf(LoginUIState())

    fun onEvent(event:UIEvent){
        validateData()
        when(event){
            is UIEvent.LoginChanged -> {
                loginUIState.value = loginUIState.value.copy(
                    login = event.login
                )
            }
            is UIEvent.PasswordChanged -> {
                loginUIState.value = loginUIState.value.copy(
                    password = event.password
                )
            }
            is UIEvent.LoginButtonClicked -> {
                logIn()
            }
        }
    }

    private fun logIn(){
        validateData()
    }

    private fun validateData() {
        val loginResult = Validator.validateLogin(login = loginUIState.value.login)
        val passwordResult = Validator.validatePassword(password = loginUIState.value.password)
        loginUIState.value = loginUIState.value.copy(
            loginError = loginResult.status,
            passwordError = passwordResult.status
        )
    }

}

package ru.mospolytech.mospolyapp.data

data class LoginUIState(
    var login: String = "",
    var password: String = "",

    var loginError: Boolean = true,
    var passwordError: Boolean = true
)

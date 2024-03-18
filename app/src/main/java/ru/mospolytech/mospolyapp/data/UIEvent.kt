package ru.mospolytech.mospolyapp.data

sealed class UIEvent {
    data class LoginChanged(val login:String) : UIEvent()
    data class PasswordChanged(val password:String) : UIEvent()

    object LoginButtonClicked : UIEvent()
}
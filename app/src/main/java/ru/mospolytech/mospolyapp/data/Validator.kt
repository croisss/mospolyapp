package ru.mospolytech.mospolyapp.data

//простейшая валидация ников. изменить с апи
object Validator {
    fun validateLogin(login:String) :ValidationResult{
        return ValidationResult(
            (login.isNotEmpty())
        )
    }

    fun validatePassword(password:String) :ValidationResult{
        return ValidationResult(
            (password.isNotEmpty())
        )
    }
}

data class ValidationResult(
    val status: Boolean = false
)
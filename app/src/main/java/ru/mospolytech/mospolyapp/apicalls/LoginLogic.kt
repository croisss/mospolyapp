package ru.mospolytech.mospolyapp.apicalls

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.mospolytech.mospolyapp.TokenManager


var retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl("https://e.mospolytech.ru/old/")
    .build()

var postAuthInfo: MPUAPI = retrofit.create(MPUAPI::class.java)

data class LoginUIState(
    var login: String = "",
    var password: String = "",
    var loginSuccess: Boolean = false,
    var inputError: Boolean = false   //needed so that textfields do not default to errorstate
)


sealed class UIEvent {
    data class LoginChanged(val login:String) : UIEvent()
    data class PasswordChanged(val password:String) : UIEvent()
    object LoginButtonClicked : UIEvent()
}


class LoginViewModel: ViewModel() {

    private val _tokenLiveData = MutableLiveData<String>()
    val tokenLiveData: LiveData<String> = _tokenLiveData

    var loginUIState = mutableStateOf(LoginUIState())

    fun onEvent(event: UIEvent){
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
                validateDataCoroutine(loginUIState.value.login, loginUIState.value.password)
            }
        }
    }

    fun validateDataCoroutine(login: String, password: String) {
        CoroutineScope(Dispatchers.IO).async {
            // Call the suspend function within the coroutine
            validateData(login, password)
        }
    }


    suspend fun validateData(login: String, password: String) {

//        withContext(Dispatchers.IO) {
//            try {
//                val response = postAuthInfo.postData(login, password)?.execute()
//                if (response?.isSuccessful == true) {
//                    Log.d("tag", "Response successful")
//                    val responseBody = response.body()
//                    responseBody?.token
//                    responseBody ?: ""
//                    Log.d("tag", responseBody?.token.toString())
//                } else {
//                    Log.d("tag", "response unsuccessful")
//                }
//            } catch (e: IOException) {
//                Log.d("tag", "networkerror")
//            }
//        }


        withContext(Dispatchers.IO) {
            val call = postAuthInfo.postData(login, password)
            call?.enqueue(object : Callback<TokenResponse?> {
                override fun onResponse(
                    call: Call<TokenResponse?>,
                    response: Response<TokenResponse?>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        TokenManager.token = responseBody?.token.toString()
                        _tokenLiveData.value = responseBody?.token.toString()
                        loginUIState.value = loginUIState.value.copy(
                            inputError = false,
                            loginSuccess = true
                        )
                        Log.d("tokenresponse", TokenManager.token)
                    }
                    else {
                        // Log the unsuccessful response
                        Log.e("API", "Response not successful: ${response.code()}")
                        TokenManager.token = ""
                        loginUIState.value = loginUIState.value.copy(
                            inputError = true,
                            loginSuccess = false
                        )
                    }
                }
                override fun onFailure(call: Call<TokenResponse?>, t: Throwable) {
                    Log.e("API", "failure")
                    TokenManager.token = ""
                    loginUIState.value = loginUIState.value.copy(
                        inputError = true,
                        loginSuccess = false
                    )
                }
            })
        }
    }
}
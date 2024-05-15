package ru.mospolytech.mospolyapp.apicalls

import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

data class TokenResponse(
    val token: String,
    val jwt: String,
    val jwt_refresh: String
)
data class Notification(
    val id: String,
    val type: String,
    val title: String,
    val text: String
)

data class Alert(
    val title: String,
    val content: String,
    val date: String,
    val time: String,
    val id: String
)
data class User(
    val user: UserData
)

data class UserData(
    val id: Int,
    val user_status: String,
    val status: String,
    val course: String,
    val name: String,
    val surname: String,
    val patronymic: String,
    val avatar: String,
    val birthday: String,
    val sex: String,
    val code: String,
    val faculty: String,
    val group: String,
    val vacation_start: String?, // You may want to change the type if it's not a String
    val vacation_end: String?, // You may want to change the type if it's not a String
    val specialty: String,
    val specialization: String,
    val degreeLength: String,
    val educationForm: String,
    val finance: String,
    val degreeLevel: String,
    val enterYear: String,
    val orders: List<String>,
    val email: String,
    val phone: String,
    val accounts: List<String>, // You may want to change the type if it's not a String
    val hasAlerts: Boolean,
    val lastaccess: String
)


data class Lesson(
    val name: String,
    val timeInterval: String,
    val place: String,
    val rooms: List<String>,
    val teachers: List<String>,
    val dateInterval: String,
    val link: String?
)
data class Day(
    val lessons: List<Lesson>
)
data class Schedule(
    val Monday: Day,
    val Tuesday: Day,
    val Wednesday: Day,
    val Thursday: Day,
    val Friday: Day,
    val Saturday: Day
)
interface MPUAPI {
    @FormUrlEncoded
    @POST("lk_api.php")
    fun postData(
        @Field("ulogin") login: String?,
        @Field("upassword") password: String?
    ): Call<TokenResponse?>?

    @GET("lk_api.php/")
    fun getSchedule(
        @Query("getSchedule") getSchedule: Boolean,
        @Query("token") token: String?
    ): Call<Schedule>

    @GET("lk_api.php/")
    fun getAlerts(
        @Query("getAlerts") getAlerts: Boolean,
        @Query("token") token: String?
    ): Call<List<Alert>>

    @GET("lk_api.php/")
    fun getNotifications(
        @Query("getNotifications") getNotifications: Boolean,
        @Query("token") token: String?
    ): Call<List<Notification>>

    @GET("lk_api.php/")
    fun getUser(
        @Query("getUser") getUser: Boolean,
        @Query("token") token: String?
    ): Call<User>
}
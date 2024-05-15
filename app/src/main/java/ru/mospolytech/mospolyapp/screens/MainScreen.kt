package ru.mospolytech.mospolyapp.screens

import android.annotation.SuppressLint
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.mospolytech.mospolyapp.BoldTextComponent
import ru.mospolytech.mospolyapp.HeadingTextComponent
import ru.mospolytech.mospolyapp.LightTextComponent
import ru.mospolytech.mospolyapp.R
import ru.mospolytech.mospolyapp.TabBarItem
import ru.mospolytech.mospolyapp.TextComponent
import ru.mospolytech.mospolyapp.TokenManager
import ru.mospolytech.mospolyapp.apicalls.Alert
import ru.mospolytech.mospolyapp.apicalls.Lesson
import ru.mospolytech.mospolyapp.apicalls.MPUAPI
import ru.mospolytech.mospolyapp.apicalls.Notification
import ru.mospolytech.mospolyapp.apicalls.Schedule
import ru.mospolytech.mospolyapp.apicalls.User
import ru.mospolytech.mospolyapp.ui.theme.MosPolyAppTheme
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale


val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)
val fontName = GoogleFont("Montserrat")
val montserrat = FontFamily(
    Font(googleFont = fontName, fontProvider = provider)
)

lateinit var defaultSchedule: Schedule
lateinit var defaultUser: User
lateinit var defaultNotifications: List<Notification>
lateinit var defaultAlerts: List<Alert>

@Composable
fun LoadingInfo() {
    var dataLoaded1 by remember { mutableStateOf(false) }
    var dataLoaded2 by remember { mutableStateOf(false) }
    var dataLoaded3 by remember { mutableStateOf(false) }
    var dataLoaded4 by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://e.mospolytech.ru/old/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val mpuapi = retrofit.create(MPUAPI::class.java)
        val scheduleCall = mpuapi.getSchedule(true, TokenManager.token)
        val userCall = mpuapi.getUser(true, TokenManager.token)
        val alertsCall = mpuapi.getAlerts(true, TokenManager.token)
        val notificationsCall = mpuapi.getNotifications(true, TokenManager.token)
        scheduleCall.enqueue(object : Callback<Schedule> {
            override fun onResponse(call: Call<Schedule>, response: Response<Schedule>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        defaultSchedule = responseBody
                        dataLoaded1 = true
                    }
                } else {
                    Log.e("API", "Response not successful: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Schedule>, t: Throwable) {
                Log.e("API", "failure")
            }
        })
        userCall.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        defaultUser = responseBody
                        dataLoaded2 = true
                    }
                } else {
                    Log.e("API", "Response not successful: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.e("API", "failure")
            }
        })
        notificationsCall.enqueue(object : Callback<List<Notification>> {
            override fun onResponse(call: Call<List<Notification>>, response: Response<List<Notification>>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        defaultNotifications = responseBody
                        dataLoaded3 = true
                    }
                } else {
                    Log.e("API", "Response not successful: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Notification>>, t: Throwable) {
                Log.e("API", "failure")
            }
        })
        alertsCall.enqueue(object : Callback<List<Alert>> {
            override fun onResponse(call: Call<List<Alert>>, response: Response<List<Alert>>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        defaultAlerts = responseBody
                        dataLoaded4 = true
                    }
                } else {
                    Log.e("API", "Response not successful: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Alert>>, t: Throwable) {
                Log.e("API", "failure")
            }
        })
    }
    if (dataLoaded1 && dataLoaded2 && dataLoaded3 && dataLoaded4) {
        MainScreen()
    } else {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0xFF3a3a3a))
        ) { }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen() {

    val homeTab = TabBarItem(title = "Главная", icon = Icons.Filled.Home)
    val scheduleTab = TabBarItem(title = "Расписание", icon = Icons.Filled.DateRange)
    val mapTab = TabBarItem(title = "Карты", icon = Icons.Filled.Place)
    val webTab = TabBarItem(title = "Все разделы", icon = Icons.Filled.Menu)
    val settingsTab = TabBarItem(title = "Профиль", icon = Icons.Filled.Person)
    val tabBarItems = listOf(homeTab, scheduleTab, mapTab, webTab, settingsTab)

    val navController = rememberNavController()

    MosPolyAppTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Scaffold(bottomBar = { TabView(tabBarItems, navController) }) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = it.calculateBottomPadding())
                        .background(color = Color(0xFF3a3a3a))
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = homeTab.title
                    ) {

                        composable(homeTab.title) {
                            var showNotifs by remember { mutableStateOf(false) }
                            var showAlert by remember { mutableStateOf(-1) }

                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                ) {
                                    HeadingTextComponent(value = "Главная")
                                    Spacer(modifier = Modifier.weight(1f))
                                    IconButton( // IconButton inherits from Button
                                        onClick = { showNotifs = true },
                                        modifier = Modifier
                                            .size(30.dp) // Set desired size for the button

                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.Notifications,
                                            contentDescription = "Notifications button"
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.padding(16.dp))
                                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                                    items(1) {
                                        TextComponent(value = "Ближайшая пара")
                                        Spacer(modifier = Modifier.padding(5.dp))
                                        ClosestLesson(defaultSchedule)
                                        Spacer(modifier = Modifier.padding(10.dp))
                                        TextComponent(value = "Последние новости")
                                        Spacer(modifier = Modifier.padding(5.dp))

                                        NewsCard(
                                            title = defaultAlerts[0].title,
                                            subtitle = FormatDate(defaultAlerts[0].date) + " • " + defaultAlerts[0].time,
                                            onPress = { showAlert = 0 }
                                        )
                                        Spacer(modifier = Modifier.padding(5.dp))
                                        NewsCard(
                                            title = defaultAlerts[1].title,
                                            subtitle = FormatDate(defaultAlerts[1].date) + " • " + defaultAlerts[1].time,
                                            onPress = { showAlert = 1 }
                                        )
                                        Spacer(modifier = Modifier.padding(5.dp))
                                        NewsCard(
                                            title = defaultAlerts[2].title,
                                            subtitle = FormatDate(defaultAlerts[2].date) + " • " + defaultAlerts[2].time,
                                            onPress = { showAlert = 2 }
                                        )
                                    }
                                }

                            }
                            if (showNotifs) {
                                NotifScreen(defaultNotifications, onDismiss = { showNotifs = false })
                            }
                            if (showAlert != -1) {
                                AlertPopup(defaultAlerts[showAlert], onDismiss = { showAlert = -1 })
                            }
                        }
                        composable(scheduleTab.title) {
                            //var textFieldValue by remember { mutableStateOf("") }
                            //var outputValue by remember { mutableStateOf("") }


                            Column(
                                modifier = Modifier.padding(
                                    top = 16.dp,
                                    start = 16.dp,
                                    end = 16.dp
                                )
                            ) {
                                Row(modifier = Modifier.fillMaxWidth()) {
                                    HeadingTextComponent(value = "Расписание")
                                    Spacer(modifier = Modifier.weight(1f))
                                    IconButton( // IconButton inherits from Button
                                        onClick = {},
//                                                onClick = { if (defaultSchedule.isNotEmpty())
//                                                    defaultSchedule.clear()
//                                                    for (schedule in dummyList) {
//                                                        scheduleList.add(schedule)
//                                                    } },
                                        modifier = Modifier
                                            .size(30.dp) // Set desired size for the button

                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.Create,
                                            contentDescription = "Schedule choice button"
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.padding(16.dp))
                                ClassScheduleDateCards(defaultSchedule)
                                Spacer(modifier = Modifier.padding(16.dp))
                            }
                        }
                        composable(mapTab.title) {
                            WebViewScreen("https://yandex.ru/maps/213/moscow/?from=mapframe&ll=37.625407%2C55.843704&mode=usermaps&source=mapframe&um=constructor%3Afbb2272bd43874d2f07fb4fe89c7f03cafb56b61a80c76519c92ec62fbce174a&utm_source=mapframe&z=11", {})
                        }
                        composable(webTab.title) {
                            var showWebView by remember { mutableStateOf("") }
                            Column(
                                modifier = Modifier.padding(
                                    top = 16.dp,
                                    start = 16.dp,
                                    end = 16.dp
                                )
                            ) {
                                HeadingTextComponent(value = "Все разделы")
                                Spacer(modifier = Modifier.padding(16.dp))
                                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                                    items(1) {

                                        TextComponent(value = "Основное")
                                        Spacer(modifier = Modifier.size(10.dp))
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            SectionCard(
                                                title = "Веб-ресурс",
                                                cardcolor = Color(95, 109, 236),
                                                icon = Icons.Filled.Info,){
                                                showWebView = "https://mospolytech.ru"
                                            }
                                            SectionCard(
                                                title = "Настройки",
                                                cardcolor = Color(121, 121, 121),
                                                icon = Icons.Filled.Settings,
                                                { })
                                            Spacer(modifier = Modifier.size(100.dp))
                                        }
                                        Spacer(modifier = Modifier.size(24.dp))

                                        TextComponent(value = "Учебная деятельность")
                                        Spacer(modifier = Modifier.size(10.dp))
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            SectionCard(
                                                title = "ЛМС",
                                                cardcolor = Color(238, 158, 68),
                                                icon = Icons.Filled.Search){
                                                showWebView = "https://online.mospolytech.ru"
                                            }
                                            Spacer(modifier = Modifier.size(100.dp))
                                            Spacer(modifier = Modifier.size(100.dp))
                                        }
                                        Spacer(modifier = Modifier.size(24.dp))

                                        TextComponent(value = "Находится в разработке")
                                        Spacer(modifier = Modifier.size(10.dp))
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            SectionCard(
                                                title = "Сообщения",
                                                cardcolor = Color(236, 95, 107),
                                                icon = Icons.Filled.Email,
                                                { })
                                            Spacer(modifier = Modifier.size(100.dp))
                                            Spacer(modifier = Modifier.size(100.dp))
                                        }
                                    }
                                }
                            }
                            if (showWebView != "") {
                                WebViewScreen(showWebView, onDismiss = { showWebView = "" })
                            }
                        }
                        composable(settingsTab.title) {
                            Column(modifier = Modifier.padding(16.dp, 16.dp, 16.dp, 0.dp)) {
                                HeadingTextComponent(value = "Профиль")
                                Spacer(modifier = Modifier.padding(16.dp))
                                LazyColumn(modifier = Modifier.fillMaxSize()) {
                                    items(1) {
                                        Surface(
                                            shape = MaterialTheme.shapes.medium,
                                            color = Color(0xFF2e2e2e),
                                            modifier = Modifier
                                                .height(203.dp)
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(5.dp))
                                        ) {
                                            Column(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .fillMaxHeight()
                                                    .clip(RoundedCornerShape(5.dp))
                                                    .padding(12.dp),
                                                horizontalAlignment = Alignment.CenterHorizontally
                                            ) {
                                                Spacer(modifier = Modifier.weight(1f))
                                                Text(
                                                    text = defaultUser.user.surname + ' ' + defaultUser.user.name + ' ' + defaultUser.user.patronymic,
                                                    style = TextStyle(
                                                        color = Color.White,
                                                        fontSize = 19.sp,
                                                        lineHeight = 25.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        fontFamily = montserrat
                                                    ),
                                                    textAlign = TextAlign.Center
                                                )
                                                val status = if (defaultUser.user.user_status == "stud") "Студент" else ""
                                                Text(
                                                    text = status + " • " + defaultUser.user.degreeLevel,
                                                    style = TextStyle(
                                                        color = Color(0x80ffffff),
                                                        fontSize = 14.sp,
                                                        lineHeight = 16.sp,
                                                        fontWeight = FontWeight.Normal,
                                                        fontFamily = montserrat
                                                    ),
                                                    textAlign = TextAlign.Center
                                                )
                                                Spacer(modifier = Modifier.size(35.dp))
                                                Row(modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(40.dp)) {
                                                    Box(contentAlignment = Alignment.Center,
                                                        modifier = Modifier
                                                            .height(40.dp)
                                                            .weight(1f)
                                                            .clip(RoundedCornerShape(5.dp))
                                                            .background(Color(0xff363636))
                                                            .padding(5.dp)
                                                    ) {
                                                        Text(
                                                            text = "Учетная карточка",
                                                            style = TextStyle(
                                                                color = Color.White,
                                                                fontSize = 14.sp,
                                                                lineHeight = 16.sp,
                                                                fontWeight = FontWeight.Normal,
                                                                fontFamily = montserrat
                                                            ),
                                                            textAlign = TextAlign.Center
                                                        )
                                                    }
                                                    Box(contentAlignment = Alignment.Center,
                                                        modifier = Modifier
                                                            .height(40.dp)
                                                            .weight(1f)
                                                            .clip(RoundedCornerShape(5.dp))
                                                            .background(Color(0xff2e2e2e))
                                                            .padding(5.dp)
                                                    ) {
                                                        Text(
                                                            text = "Приказы",
                                                            style = TextStyle(
                                                                color = Color.White,
                                                                fontSize = 14.sp,
                                                                lineHeight = 16.sp,
                                                                fontWeight = FontWeight.Normal,
                                                                fontFamily = montserrat
                                                            ),
                                                            textAlign = TextAlign.Center
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                        Spacer(modifier = Modifier.size(12.dp))
                                        Surface(
                                            shape = MaterialTheme.shapes.medium,
                                            color = Color(0xFF2e2e2e),
                                            modifier = Modifier
                                                .height(66.dp)
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(5.dp))
                                        ) {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .fillMaxHeight()
                                                    .clip(RoundedCornerShape(5.dp))
                                                    .padding(12.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Box(contentAlignment = Alignment.Center,
                                                    modifier = Modifier
                                                        .height(42.dp)
                                                        .weight(1f)
                                                        .clip(RoundedCornerShape(5.dp))
                                                        .background(Color(0xff363636))
                                                        .padding(10.dp)
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Settings,
                                                        contentDescription = "Settings Icon",
                                                        tint = Color.White
                                                    )
                                                }
                                                Spacer(modifier = Modifier.size(12.dp))
                                                Box(contentAlignment = Alignment.Center,
                                                    modifier = Modifier
                                                        .height(42.dp)
                                                        .weight(1f)
                                                        .clip(RoundedCornerShape(5.dp))
                                                        .background(Color(0xff363636))
                                                        .padding(10.dp)
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Info,
                                                        contentDescription = "Dark mode Icon",
                                                        tint = Color.White
                                                    )
                                                }
                                                Spacer(modifier = Modifier.size(12.dp))
                                                Box(contentAlignment = Alignment.Center,
                                                    modifier = Modifier
                                                        .height(42.dp)
                                                        .weight(1f)
                                                        .clip(RoundedCornerShape(5.dp))
                                                        .background(Color(0xff363636))
                                                        .padding(10.dp)
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Create,
                                                        contentDescription = "Rewrite Icon",
                                                        tint = Color.White
                                                    )
                                                }
                                                Spacer(modifier = Modifier.size(12.dp))
                                                Box(contentAlignment = Alignment.Center,
                                                    modifier = Modifier
                                                        .height(42.dp)
                                                        .weight(1f)
                                                        .clip(RoundedCornerShape(5.dp))
                                                        .background(Color(0xff363636))
                                                        .padding(10.dp)
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.ExitToApp,
                                                        contentDescription = "Exit Icon",
                                                        tint = Color.White
                                                    )
                                                }
                                            }
                                        }
                                        Spacer(modifier = Modifier.size(12.dp))
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(6.dp)
                                        ) {
                                            Spacer(modifier = Modifier.size(10.dp))
                                            LazyRow{
                                                item(1) {
                                                    BoldTextComponent("Статус: ")
                                                    LightTextComponent(defaultUser.user.status)
                                                }
                                            }
                                            Spacer(modifier = Modifier.size(10.dp))
                                            val sexRuString = if (defaultUser.user.sex == "Male") "Мужской" else "Женский"
                                            LazyRow{
                                                item(1) {
                                                    BoldTextComponent("Пол: ")
                                                    LightTextComponent(sexRuString)
                                                }
                                            }
                                            Spacer(modifier = Modifier.size(10.dp))
                                            LazyRow{
                                                item(1) {
                                                    BoldTextComponent("Дата рождения: ")
                                                    LightTextComponent(defaultUser.user.birthday)
                                                }
                                            }
                                            Spacer(modifier = Modifier.size(10.dp))
                                            LazyRow{
                                                item(1) {
                                                    BoldTextComponent("Код студента: ")
                                                    LightTextComponent(defaultUser.user.code)
                                                }
                                            }
                                            Spacer(modifier = Modifier.size(10.dp))
                                            LazyRow{
                                                item(1) {
                                                    BoldTextComponent("Факультет: ")
                                                    LightTextComponent(defaultUser.user.faculty)
                                                }
                                            }
                                            Spacer(modifier = Modifier.size(10.dp))
                                            LazyRow{
                                                item(1) {
                                                    BoldTextComponent("Курс: ")
                                                    LightTextComponent(defaultUser.user.course)
                                                }
                                            }
                                            Spacer(modifier = Modifier.size(10.dp))
                                            LazyRow{
                                                item(1) {
                                                    BoldTextComponent("Группа: ")
                                                    LightTextComponent(defaultUser.user.group)
                                                }
                                            }
                                            Spacer(modifier = Modifier.size(10.dp))
                                            LazyRow{
                                                item(1) {
                                                    BoldTextComponent("Направление: ")
                                                    LightTextComponent(defaultUser.user.specialty)
                                                }
                                            }
                                            Spacer(modifier = Modifier.size(10.dp))
                                            LazyRow{
                                                item(1) {
                                                    BoldTextComponent("Специализация: ")
                                                    LightTextComponent(defaultUser.user.specialization)
                                                }
                                            }
                                            Spacer(modifier = Modifier.size(10.dp))
                                            LazyRow{
                                                item(1) {
                                                    BoldTextComponent("Срок обучения: ")
                                                    LightTextComponent(defaultUser.user.degreeLength)
                                                }
                                            }
                                            Spacer(modifier = Modifier.size(10.dp))
                                            LazyRow{
                                                item(1) {
                                                    BoldTextComponent("Форма обучения: ")
                                                    LightTextComponent(defaultUser.user.educationForm)
                                                }
                                            }
                                            Spacer(modifier = Modifier.size(10.dp))
                                            LazyRow{
                                                item(1) {
                                                    BoldTextComponent("Вид финансирования: ")
                                                    LightTextComponent(defaultUser.user.finance)
                                                }
                                            }
                                            Spacer(modifier = Modifier.size(10.dp))
                                            LazyRow{
                                                item(1) {
                                                    BoldTextComponent("Уровень образования: ")
                                                    LightTextComponent(defaultUser.user.degreeLevel)
                                                }
                                            }
                                            Spacer(modifier = Modifier.size(10.dp))
                                            LazyRow{
                                                item(1) {
                                                    BoldTextComponent("Год набора: ")
                                                    LightTextComponent(defaultUser.user.enterYear)
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun TabView(tabBarItems: List<TabBarItem>, navController: NavController) {
    var selectedTabIndex by rememberSaveable {
        mutableIntStateOf(0)
    }
    NavigationBar {
        // looping over each tab to generate the views and navigation for each item
        tabBarItems.forEachIndexed { index, tabBarItem ->
            NavigationBarItem(
                selected = selectedTabIndex == index,
                onClick = {
                    selectedTabIndex = index
                    navController.navigate(tabBarItem.title)
                },
                icon = {
                    Icon(
                        imageVector = tabBarItem.icon,
                        contentDescription = tabBarItem.title
                    )
                },
                label = { Text(tabBarItem.title, fontSize = 8.sp) })
        }
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewScreen(url: String, onDismiss: () -> Unit) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            IconButton( // IconButton inherits from Button
                onClick = { onDismiss() },
                modifier = Modifier
                    .size(30.dp) // Set desired size for the button

            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Notifications button"
                )
            }
            Spacer(modifier = Modifier.weight(1f))
        }
        AndroidView(factory = { ctx ->
            WebView(ctx).apply {
                settings.javaScriptEnabled = true
                webViewClient = WebViewClient()
                loadUrl(url)
            }
        }, modifier = Modifier
            .fillMaxSize()
            .absoluteOffset()
        )
    }
}

@Composable
fun ClassScheduleDateCard(date: String, weekday: String, index: Int, selectedDay: MutableState<Int>, onSelected: (Int) -> Unit) {
    var isClicked = selectedDay.value == index
    Surface(
        modifier = Modifier
            .size(50.dp, 80.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        isClicked = false
                        Log.d("tag", selectedDay.value.toString() + index.toString() + isClicked)
                        if (!isClicked) {
                            onSelected(index)
                        }
                        Log.d("tag", selectedDay.value.toString() + index.toString() + isClicked)
                    }
                )
            }
    ) {
        Column(
            modifier = Modifier
                .background(color = Color(0xFF3a3a3a))
                .padding(5.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = weekday,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        fontStyle = FontStyle.Normal,
                        // fontFamily = montserrat
                    ),
                    color = Color.White
                )
            }
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(26.dp)
                    .clip(CircleShape)
                    .background(
                        if (isClicked) Color.White else Color(0xFF3a3a3a)
                    )
            ) {
                Text(
                    modifier = Modifier
                        .align(Alignment.CenterVertically),
                    textAlign = TextAlign.Center,
                    text = date,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        fontStyle = FontStyle.Normal,
                        // fontFamily = montserrat
                    ),
                    color = if (isClicked) Color(0xFF3a3a3a) else Color.White
                )
            }
        }
    }
}

@Composable
fun ClassScheduleDateCards(schedule: Schedule) {
    // Remember the latest schedule
    val currentSchedule by remember { mutableStateOf(schedule) }


    val currentDayOfWeek = LocalDate.now().dayOfWeek.value // 1 for Monday, 2 for Tuesday, ..., 7 for Sunday
    val todayIndex = LocalDate.now().dayOfWeek.value % 7 // Index of today in the list (0 for Sun, 1 for Mon, etc.)
    val currentDayOfMonth = LocalDate.now().dayOfMonth
    var mondayDate = currentDayOfMonth - currentDayOfWeek + 1
    if (mondayDate < 1) {
        mondayDate = LocalDate.now().minusMonths(1).lengthOfMonth() - mondayDate
    }
    val daysOfWeek = listOf(
        "Пн", "Вт", "Ср", "Чт", "Пт", "Сб"
    )

    var currentSelectedDay = remember { mutableIntStateOf(if (todayIndex == 0) 0 else todayIndex -1) }

    Row(modifier = Modifier.fillMaxWidth()) {
        // Start the index from the current day of the week
        for (i in 0..5) {
            val day =
                if (mondayDate + i <= LocalDate.now().lengthOfMonth()) mondayDate + i
            else mondayDate + i - LocalDate.now().lengthOfMonth()

            Spacer(modifier = Modifier.weight(1f))
            ClassScheduleDateCard(
                date = day.toString(),
                weekday = daysOfWeek[i],
                index = i,
                selectedDay = currentSelectedDay,
                onSelected = { newIndex -> currentSelectedDay.value = newIndex}
            )
            Spacer(modifier = Modifier.weight(1f))
        }
    }

    // Retrieve lessons for the selected day from the latest schedule
    val selectedDayLessons = when (currentSelectedDay.value) {
        0 -> currentSchedule.Monday.lessons
        1 -> currentSchedule.Tuesday.lessons
        2 -> currentSchedule.Wednesday.lessons
        3 -> currentSchedule.Thursday.lessons
        4 -> currentSchedule.Friday.lessons
        5 -> currentSchedule.Saturday.lessons
        else -> emptyList() // Handle other cases if needed
    }


    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        val currentDate = LocalDate.now()

        for (pair in 1..6) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "$pair пара",
                        fontSize = 10.sp,
                        modifier = Modifier.padding(horizontal = 2.dp)
                    )
                    Divider(
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .weight(1f),
                        color = Color.White,
                        thickness = 1.dp,
                    )
                }
            }

            var isPairInTimespan = false
            val pairTime = when (pair) {
                1 -> "9:00 - 10:30"
                2 -> "10:40 - 12:10"
                3 -> "12:20 - 13:50"
                4 -> "14:30 - 16:00"
                5 -> "16:10 - 17:40"
                6 -> "17:50 - 19:20"
                else -> ""
            }
            selectedDayLessons.forEach { lesson ->
                val dateRange = ScheduleDateToNum(lesson.dateInterval)
                if (
                    (currentDate.monthValue in dateRange[1]..dateRange[3] &&
                            (currentDate.monthValue != dateRange[1] && currentDate.monthValue != dateRange[3])) ||
                    ((currentDate.monthValue == dateRange[1] && currentDate.dayOfMonth >= dateRange[0])) ||
                    ((currentDate.monthValue == dateRange[3] && currentDate.dayOfMonth <= dateRange[2]))
                ) {
                    if (pairTime == lesson.timeInterval) {
                        isPairInTimespan = true
                        item {
                            ClassScheduleItem(lesson)
                            Spacer(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(5.dp)
                            )
                        }
                    }
                }
            }
            if (!isPairInTimespan) {
                item {
                    Spacer(modifier = Modifier.height(108.dp))
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp)
                    )
                }
            }
        }
    }
}

fun ScheduleDateToNum(date: String): MutableList<Int> {
    Log.d("tag", date)

    var currentDate =  date.trim('-')
    val dates = currentDate.split(' ')
    val returnList = mutableListOf<Int>()

    if (dates.size != 5){
        Log.d("tag", dates[0])
        Log.d("tag", "dates=" + dates[0] + ' ' + dates[1] + ' ' + dates[0] + ' ' + dates[1])
        returnList.add(dates[0].toInt())
        returnList.add(when(dates[1]) {
            "Янв" -> 1
            "Фев" -> 2
            "Мар" -> 3
            "Апр" -> 4
            "Май" -> 5
            "Июн" -> 6
            "Июл" -> 7
            "Авг" -> 8
            "Сен" -> 9
            "Окт" -> 10
            "Ноя" -> 11
            "Дек" -> 12
            else -> throw IllegalArgumentException("Invalid month name in 1")
        })
        returnList.add(dates[0].toInt())
        returnList.add(when(dates[1]) {
            "Янв" -> 1
            "Фев" -> 2
            "Мар" -> 3
            "Апр" -> 4
            "Май" -> 5
            "Июн" -> 6
            "Июл" -> 7
            "Авг" -> 8
            "Сен" -> 9
            "Окт" -> 10
            "Ноя" -> 11
            "Дек" -> 12
            else -> throw IllegalArgumentException("Invalid month name in 2")
        })
        Log.d("tag", "dates=" + returnList[0] + ' ' + returnList[1] + ' ' + returnList[2] + ' ' + returnList[3])
        return returnList
    }
    else {
        Log.d("tag", "dates=" + dates[0] + ' ' + dates[1] + ' ' + dates[3] + ' ' + dates[4])
        returnList.add(dates[0].toInt())
        returnList.add(when(dates[1]) {
            "Янв" -> 1
            "Фев" -> 2
            "Мар" -> 3
            "Апр" -> 4
            "Май" -> 5
            "Июн" -> 6
            "Июл" -> 7
            "Авг" -> 8
            "Сен" -> 9
            "Окт" -> 10
            "Ноя" -> 11
            "Дек" -> 12
            else -> throw IllegalArgumentException("Invalid month name in 1")
        })
        returnList.add(dates[3].toInt())
        returnList.add(when(dates[4]) {
            "Янв" -> 1
            "Фев" -> 2
            "Мар" -> 3
            "Апр" -> 4
            "Май" -> 5
            "Июн" -> 6
            "Июл" -> 7
            "Авг" -> 8
            "Сен" -> 9
            "Окт" -> 10
            "Ноя" -> 11
            "Дек" -> 12
            else -> throw IllegalArgumentException("Invalid month name in 2")
        })
        Log.d("tag", "dates=" + returnList[0] + ' ' + returnList[1] + ' ' + returnList[2] + ' ' + returnList[3])
        return returnList
    }
}

fun TeacherTransform(names: List<String>): List<String> {
    return names.map { name ->
        if (name.isEmpty()) {
            ".."
        } else {
            val parts = name.split(" ")
            val abbreviated = parts.mapIndexed { index, part ->
                if (index == 0) {
                    part // Keep the last part as is (surname)
                } else {
                    "${part.firstOrNull()?.uppercaseChar()}."
                }
            }
            abbreviated.joinToString(" ")
        }
    }
}

@Composable
fun ClassScheduleItem(lesson: Lesson) {
    val background = when (lesson.timeInterval) {
        "9:00 - 10:30" -> Color(0x5ca7ffd3)
        "10:40 - 12:10" -> Color(0x59a3fbfb)
        "12:20 - 13:50" -> Color(0x42bac0ff)
        "14:30 - 16:00" -> Color(0x5cdcb7ff)
        "16:10 - 17:40" -> Color(0x61ffbce5)
        "17:50 - 19:20" -> Color(0x59ffbbc1)
        else -> Color.White
    }

    val textcolor = when (lesson.timeInterval) {
        "9:00 - 10:30" -> Color(161, 255, 209)
        "10:40 - 12:10" -> Color(163, 251, 251)
        "12:20 - 13:50" -> Color(186, 192, 255)
        "14:30 - 16:00" -> Color(220, 183, 255)
        "16:10 - 17:40" -> Color(255, 188, 229)
        "17:50 - 19:20" -> Color(255, 187, 193)
        else -> Color.White
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(108.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(background)
            .padding(8.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = lesson.timeInterval,
                style = TextStyle(
                    color = textcolor,
                    fontSize = 12.sp, // 0.8em ≈ 12.8 sp
                    lineHeight = 16.sp, // 1.3 * 12.8 ≈ 16.64 sp
                    fontWeight = FontWeight.Normal,
                    fontFamily = montserrat
                )
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = lesson.rooms.joinToString(separator = " • "),
                style = TextStyle(
                    color = textcolor,
                    fontSize = 12.sp, // 0.8em ≈ 12.8 sp
                    lineHeight = 16.sp, // 1.3 * 12.8 ≈ 16.64 sp
                    fontWeight = FontWeight.Normal,
                    fontFamily = montserrat
                )
            )
        }
        Text(
            text = lesson.name,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = TextStyle(
                color = textcolor,
                fontSize = 16.sp, // 0.95rem ≈ 15.2 sp
                lineHeight = 20.sp, // 1.3 * 15.2 ≈ 19.76 sp
                fontWeight = FontWeight.Normal,
                fontFamily = montserrat
            )
        )
        Text(
            text = TeacherTransform(lesson.teachers).joinToString(separator = " • "),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = TextStyle(
                color = textcolor,
                fontSize = 12.sp, // 0.8em ≈ 12.8 sp
                lineHeight = 16.sp, // 1.3 * 12.8 ≈ 16.64 sp
                fontWeight = FontWeight.Normal,
                fontFamily = montserrat
            )
        )
        Text(text = lesson.dateInterval,
            style = TextStyle(
                color = textcolor,
                fontSize = 12.sp, // 0.8em ≈ 12.8 sp
                lineHeight = 16.sp, // 1.3 * 12.8 ≈ 16.64 sp
                fontWeight = FontWeight.Normal,
                fontFamily = montserrat
            )
        )
    }
}

@Composable
fun NewsCard(title: String, subtitle: String, onPress: () -> Unit) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        color = Color(0xFF2e2e2e),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(15.dp))
            .clickable(onClick = onPress)
    ) {
        Row(modifier = Modifier
            .padding(8.dp)
            .height(80.dp)){
            Column(modifier = Modifier.weight(1f)){
                Text(
                    text = title,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        fontStyle = FontStyle.Normal,
                        fontFamily = montserrat),
                    color = Color.White
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = subtitle,
                    style = TextStyle(
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Light,
                        fontStyle = FontStyle.Normal,
                        fontFamily = montserrat),
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Image(painter = painterResource(id = R.drawable.logo_darkmode), contentDescription = "logo", modifier = Modifier
                .size(50.dp)
                .align(Alignment.CenterVertically)
            )
        }
    }
}

@Composable
fun SectionCard(title: String, cardcolor: Color, icon: ImageVector, onPress: () -> Unit) {
    Surface(
        color = Color(0xFF252525),
        modifier = Modifier
            .size(100.dp, 130.dp)
            .clip(RoundedCornerShape(15.dp))
            .clickable(onClick = onPress)
    ) {
        Column(modifier = Modifier.padding(15.dp), verticalArrangement = Arrangement.Center) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                Surface(modifier = Modifier
                    .size(33.dp)
                    .clip(RoundedCornerShape(12.dp)), color = cardcolor){
                    Icon(
                        modifier = Modifier.scale(0.5f),
                        imageVector = icon,
                        contentDescription = "icon",

                    )
                }
            }
            Spacer(modifier = Modifier.size(15.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Text(
                    text = title,
                    style = TextStyle(
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Normal,
                        fontFamily = montserrat),
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun NotifScreen(notifications: List<Notification>, onDismiss: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(onClick = onDismiss)
            .background(Color.Black.copy(alpha = 0.5f))
            .padding(16.dp, 64.dp, 16.dp, 16.dp)
            .clickable(onClick = { }, enabled = false),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(color = Color(0xFF3a3a3a), RoundedCornerShape(8.dp))
                .padding(16.dp)
        ) {
            Column(modifier = Modifier){
                Row(modifier = Modifier
                    .fillMaxWidth()){
                    HeadingTextComponent(value = "Уведомления")
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton( // IconButton inherits from Button
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(30.dp) // Set desired size for the button

                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Close button"
                        )
                    }
                }
                Spacer(modifier = Modifier.padding(16.dp))
                LazyColumn(modifier = Modifier.fillMaxWidth()){
                    items(1) {
                        for (notification in notifications) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(color = Color(0xFF2e2e2e), RoundedCornerShape(8.dp))
                                    .clickable(onClick = {})
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = notification.title,
                                        style = TextStyle(
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            fontStyle = FontStyle.Normal,
                                            fontFamily = montserrat
                                        ),
                                        textAlign = TextAlign.Center
                                    )
                                    Text(
                                        text = notification.text,
                                        modifier = Modifier,
                                        style = TextStyle(
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Normal,
                                            fontStyle = FontStyle.Normal,
                                            fontFamily = montserrat
                                        )
                                    )
                                }
                                Spacer(modifier = Modifier.size(8.dp))
                                IconButton(onClick = {}, modifier = Modifier.size(30.dp)){
                                    Icon(imageVector = Icons.Filled.Delete,
                                        contentDescription = "trash button")
                                }
                            }
                            Spacer(modifier = Modifier.padding(4.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ClosestLesson(schedule: Schedule) {
    val currentDate = LocalDate.now()
    val currentTime = LocalTime.now()
    var closestLesson: Lesson? = null
    var closestTimeDiff = Int.MAX_VALUE

    // Iterate through each day in the schedule
    for (dayOfWeek in 0..5) {
        val lessons = when (dayOfWeek) {
            0 -> schedule.Monday.lessons
            1 -> schedule.Tuesday.lessons
            2 -> schedule.Wednesday.lessons
            3 -> schedule.Thursday.lessons
            4 -> schedule.Friday.lessons
            5 -> schedule.Saturday.lessons
            else -> emptyList()
        }
        if (dayOfWeek != currentDate.dayOfWeek.value - 1) {
            continue
        }

        // Iterate through each lesson in the day
        for (lesson in lessons) {
            Log.d("tag", lesson.name + " >" + lesson.dateInterval)
            // Check if the lesson is scheduled for the current date
            val lessonDateInterval = ScheduleDateToNum(lesson.dateInterval)
            val formatter = DateTimeFormatter.ofPattern("H:mm")

            if (
                (currentDate.monthValue in lessonDateInterval[1]..lessonDateInterval[3] &&
                        (currentDate.monthValue != lessonDateInterval[1] && currentDate.monthValue != lessonDateInterval[3])) ||
                ((currentDate.monthValue == lessonDateInterval[1] && currentDate.dayOfMonth >= lessonDateInterval[0])) ||
                ((currentDate.monthValue == lessonDateInterval[3] && currentDate.dayOfMonth <= lessonDateInterval[2]))
            ) {

                // Check if the lesson is scheduled for the current time interval
                val lessonStartTime = lesson.timeInterval.split(" - ")[0]
                val lessonEndTime = lesson.timeInterval.split(" - ")[1]
                val startTime = LocalTime.parse(lessonStartTime, formatter)
                val endTime = LocalTime.parse(lessonEndTime, formatter)

                if (endTime < currentTime) {
                    continue
                }

                if (currentTime in startTime..endTime) {
                    // Lesson is currently ongoing
                    closestLesson = lesson
                    break
                } else {
                    // Check if the lesson is the closest to the current time
                    val timeDiff = if (currentTime < startTime) {
                        startTime.toSecondOfDay() - currentTime.toSecondOfDay()
                    } else {
                        currentTime.toSecondOfDay() - endTime.toSecondOfDay()
                    }

                    if (timeDiff < closestTimeDiff) {
                        closestTimeDiff = timeDiff
                        closestLesson = lesson
                    }
                }
            }
        }
    }

    if (closestLesson != null) {
        ClassScheduleItem(closestLesson)
    } else {
        LightTextComponent(value = "Учебный день завершился!")
    }
}

@Composable
fun AlertPopup(alert: Alert, onDismiss: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(onClick = onDismiss)
            .background(Color.Black.copy(alpha = 0.5f))
            .padding(16.dp, 64.dp, 16.dp, 16.dp)
            .clickable(onClick = { }, enabled = false),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(color = Color(0xFF3a3a3a), RoundedCornerShape(8.dp))
                .padding(16.dp)
        ) {
            Column(modifier = Modifier) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = alert.title,
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            fontStyle = FontStyle.Normal,
                            fontFamily = montserrat
                        ),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.size(16.dp))
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .width(30.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Close button"
                        )
                    }
                }
                Spacer(modifier = Modifier.padding(16.dp))
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(1) {
                        TextComponent(alert.content
                            .replace("<p>\r\n\t", "\n")
                            .replace("</p>\r\n", "\n")
                            .replace("&nbsp;", ""))
                    }
                }
            }
        }
    }
}

fun FormatDate(inputDate: String): String {
    // Parse the input string as a LocalDate
    val date = LocalDate.parse(inputDate)

    // Format the date using a custom formatter
    val dateFormatter = DateTimeFormatter.ofPattern("d MMMM u г.", Locale("ru"))
    val formattedDate = dateFormatter.format(date)


    // Combine the formatted date and time
    return formattedDate
}
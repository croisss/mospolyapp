package ru.mospolytech.mospolyapp.screens

import android.annotation.SuppressLint
import android.os.Build
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.Center
import androidx.compose.foundation.layout.Arrangement.SpaceEvenly
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.webkit.WebSettingsCompat
import coil.compose.rememberAsyncImagePainter
import ru.mospolytech.mospolyapp.HeadingTextComponent
import ru.mospolytech.mospolyapp.R
import ru.mospolytech.mospolyapp.TabBarItem
import ru.mospolytech.mospolyapp.TextComponent
import ru.mospolytech.mospolyapp.ui.theme.MosPolyAppTheme
import coil.compose.rememberImagePainter
import coil.decode.SvgDecoder
import coil.request.ImageRequest

val dummyList = listOf(
    ClassSchedule("9:00 - 10:30", "Математический Анализ(Практика)", "Mr. Smith", "пр9998", "29 Янв - 26 Май"),
    ClassSchedule("10:40 - 12:10", "Программирование(Практика)", "Ms. Johnson", "ав9999", "29 Янв - 30 Мар"),
    ClassSchedule("12:20 - 13:50", "Философия(Лекция)", "Mr. Brown", "пк4096", "05 Фев - 30 Мар"),
    ClassSchedule("14:30 - 16:00", "Проектная деятельность(Практика)", "Mr. Smith", "ав5404", "29 Янв - 30 Мар"),
    ClassSchedule("16:10 - 17:40", "Физика(Лекция)", "Ms. Johnson", "бс1011", "29 Янв - 31 Мар"),
    ClassSchedule("17:50 - 19:20", "Английский язык(Практика)", "Mr. White", "пр1515", "29 Янв - 31 Мар")
)

val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

val fontName = GoogleFont("Montserrat")
val montserrat = FontFamily(
    Font(googleFont = fontName, fontProvider = provider)
)

data class ClassSchedule(
    val time: String,
    val name: String,
    val teacher: String,
    val classroom: String,
    val date: String
)

data class TabBarItem(
    val title: String,
    val icon: ImageVector,
)

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
                        Box(modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = it.calculateBottomPadding())
                            .background(color = Color(0xFF3a3a3a))
                        ) {
                            NavHost(navController = navController, startDestination = homeTab.title) {

                                composable(homeTab.title) {
                                    Column(modifier = Modifier.padding(16.dp)){
                                        Row(modifier = Modifier
                                            .fillMaxWidth()){
                                            HeadingTextComponent(value = "Главная")
                                            Spacer(modifier = Modifier.weight(1f))
                                            IconButton( // IconButton inherits from Button
                                                onClick = { /* Handle button click here */ },
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
                                        LazyColumn(modifier = Modifier.fillMaxWidth()){
                                            items(1) {
                                                TextComponent(value = "Ближайшая пара")
                                                Spacer(modifier = Modifier.padding(5.dp))
                                                ClassScheduleItem(dummyList[1])
                                                Spacer(modifier = Modifier.padding(10.dp))
                                                TextComponent(value = "Последние новости")
                                                Spacer(modifier = Modifier.padding(5.dp))

                                                NewsCard(
                                                    title = "Анкетирование в рамках проведения ежегодного самообследования университета",
                                                    subtitle = "5 марта 2024 г. • 18:25"
                                                )
                                                Spacer(modifier = Modifier.padding(5.dp))
                                                NewsCard(
                                                    title = "Китайский Новый год",
                                                    subtitle = "12 февраля 2024 г. • 14:37"
                                                )
                                                Spacer(modifier = Modifier.padding(5.dp))
                                                NewsCard(
                                                    title = "Всероссийский конкурс проектов с открытым кодом",
                                                    subtitle = "8 февраля 2024 г. • 10:23"
                                                )
                                            }
                                        }

                                    }

                                }

                                composable(scheduleTab.title) {
                                    var textFieldValue by remember { mutableStateOf("") }
                                    var outputValue by remember { mutableStateOf("") }
                                    var scheduleList = remember { mutableStateListOf<ClassSchedule>()}

                                    Column(modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp)){
                                        Row(modifier = Modifier.fillMaxWidth()){
                                            HeadingTextComponent(value = "Расписание")
                                            Spacer(modifier = Modifier.weight(1f))
                                            IconButton( // IconButton inherits from Button
                                                onClick = { if (scheduleList.isNotEmpty())
                                                    scheduleList.clear()
                                                    for (schedule in dummyList) {
                                                        scheduleList.add(schedule)
                                                    } },
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
                                        Row(modifier = Modifier.fillMaxWidth()){
                                            ClassScheduleDateCard("18", "Пн")
                                            Spacer(modifier = Modifier.weight(1f))
                                            ClassScheduleDateCard("19", "Вт")
                                            Spacer(modifier = Modifier.weight(1f))
                                            ClassScheduleDateCard("20", "Ср")
                                            Spacer(modifier = Modifier.weight(1f))
                                            ClassScheduleDateCard("21", "Чт")
                                            Spacer(modifier = Modifier.weight(1f))
                                            ClassScheduleDateCard("22", "Пт")
                                            Spacer(modifier = Modifier.weight(1f))
                                            ClassScheduleDateCard("23", "Сб")

                                        }
                                        Spacer(modifier = Modifier.padding(16.dp))
                                        LazyColumn(modifier = Modifier.fillMaxWidth()){
                                            items(scheduleList.size) { index ->
                                                ClassScheduleItem(scheduleList[index])
                                                Spacer(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(5.dp)
                                                )
                                            }
                                        }
                                    }
                                }

                                composable(mapTab.title) {
                                    WebViewScreen("https://yandex.ru/maps/213/moscow/?from=mapframe&ll=37.625407%2C55.843704&mode=usermaps&source=mapframe&um=constructor%3Afbb2272bd43874d2f07fb4fe89c7f03cafb56b61a80c76519c92ec62fbce174a&utm_source=mapframe&z=11")
                                }

                                composable(webTab.title) {
                                    Column(modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp)) {
                                        HeadingTextComponent(value = "Все разделы")
                                        Spacer(modifier = Modifier.padding(16.dp))
                                        LazyColumn(modifier = Modifier.fillMaxWidth()) {
                                            items(1) {

                                                TextComponent(value = "Основное")
                                                Spacer(modifier = Modifier.size(10.dp))
                                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                                                    SectionCard(title = "Веб-ресурс", cardcolor = Color(95, 109, 236), icon = Icons.Filled.Info, { })
                                                    SectionCard(title = "Настройки", cardcolor = Color(121, 121, 121), icon = Icons.Filled.Settings, { })
                                                    Spacer(modifier = Modifier.size(100.dp))
                                                }
                                                Spacer(modifier = Modifier.size(24.dp))

                                                TextComponent(value = "Учебная деятельность")
                                                Spacer(modifier = Modifier.size(10.dp))
                                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                                                    SectionCard(title = "ЛМС", cardcolor = Color(238, 158, 68), icon = Icons.Filled.Search, { })
                                                    Spacer(modifier = Modifier.size(100.dp))
                                                    Spacer(modifier = Modifier.size(100.dp))
                                                }
                                                Spacer(modifier = Modifier.size(24.dp))

                                                TextComponent(value = "Находится в разработке")
                                                Spacer(modifier = Modifier.size(10.dp))
                                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                                                    SectionCard(title = "Сообщения", cardcolor = Color(236, 95, 107), icon = Icons.Filled.Email, { })
                                                    Spacer(modifier = Modifier.size(100.dp))
                                                    Spacer(modifier = Modifier.size(100.dp))
                                                }
                                            }
                                        }
                                    }
                                }
                                    /*
                                    val currentUrl = remember { mutableStateOf("0") }
                                    var showElement by remember { mutableStateOf(false) }

                                    Column(
                                        modifier = Modifier.fillMaxSize(),
                                    ){
                                        if (showElement) {
                                            if(currentUrl.value != "0") {
                                                val urlStatic = currentUrl.value
                                                Column(modifier = Modifier.weight(1f)
                                                ){
                                                    WebViewScreenNEW(url = urlStatic)
                                                }

                                            }
                                        }
                                        else {
                                            Spacer(modifier = Modifier.weight(1f))
                                        }

                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceEvenly
                                        ) {
                                            // Button 1
                                            Button(onClick = {
                                                currentUrl.value = "https://e.mospolytech.ru/old"
                                                showElement = true
                                            }) {
                                                Text(text = "ЛК")
                                            }

                                            // Button 2
                                            Button(onClick = {
                                                currentUrl.value = "https://online.mospolytech.ru"
                                                showElement = true
                                            }) {
                                                Text(text = "ЛМС")
                                            }

                                            // Button 3
                                            Button(onClick = {
                                                currentUrl.value = "https://mospolytech.ru"
                                                showElement = true

                                            }) {
                                                Text(text = "MPU")
                                            }
                                        }

                                    }





                                
                                     */
                                composable(settingsTab.title) {
                                    TextComponent(value = "Настройки/профиль")
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
fun WebViewScreen(url: String) {
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

@Composable
fun WebViewScreenNEW(url: String) {

    AndroidView(
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                webViewClient = WebViewClient()

                settings.loadWithOverviewMode = true
                settings.useWideViewPort = true
                settings.setSupportZoom(true)
            }
        },
        update = { webView ->
            webView.loadUrl(url)
        }
    )
}

@Composable
fun ClassScheduleDateCard(date: String, weekday: String){
    var clicked by remember { mutableStateOf(false) }
    Surface(
        color = Color(0xFF252525),
        modifier = Modifier
            .size(50.dp, 80.dp)
            .clip(RoundedCornerShape(15.dp))
            .clickable { clicked = !clicked }
    ){
        Column(modifier = Modifier.padding(5.dp), verticalArrangement = Arrangement.Center){
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Text(
                    text = weekday,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        fontStyle = FontStyle.Normal,
                        fontFamily = montserrat),
                    color = Color.White
                )
            }
            Row(modifier = Modifier.fillMaxWidth().clip(CircleShape).size(40.dp).background(if (clicked){Color.White} else Color(0xFF252525)), horizontalArrangement = Arrangement.Center) {
                Text(
                    modifier = Modifier.wrapContentSize().align(Alignment.CenterVertically),
                    textAlign = TextAlign.Center,
                    text = date,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        fontStyle = FontStyle.Normal,
                        fontFamily = montserrat),
                    color = (if (clicked){Color(0xFF252525)} else Color.White)
                )
            }
        }
    }
}

@Composable
fun ClassScheduleItem(schedule: ClassSchedule) {
    Column(
        modifier = Modifier
            .background(
                when (schedule.time) {
                    "9:00 - 10:30" -> Color(0x5ca7ffd3)
                    "10:40 - 12:10" -> Color(0x59a3fbfb)
                    "12:20 - 13:50" -> Color(0x42bac0ff)
                    "14:30 - 16:00" -> Color(0x5cdcb7ff)
                    "16:10 - 17:40" -> Color(0x61ffbce5)
                    "17:50 - 19:20" -> Color(0x59ffbbc1)
                    else -> Color(0xFFFFFFFF)
                }
            )
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        val textcolor: Color
        val headerdotcolor: Color
        when (schedule.time) {
            "9:00 - 10:30" -> textcolor = Color(161, 255, 209)
            "10:40 - 12:10" -> textcolor = Color(163, 251, 251)
            "12:20 - 13:50" -> textcolor = Color(186, 192, 255)
            "14:30 - 16:00" -> textcolor = Color(220, 183, 255)
            "16:10 - 17:40" -> textcolor = Color(255, 188, 229)
            "17:50 - 19:20" -> textcolor = Color(255, 187, 193)
            else -> textcolor = Color(0xFFFFFFFF)
        }
        Row(modifier = Modifier.fillMaxWidth()){
            Text(text = schedule.time, color = textcolor, fontSize = 14.sp)
            Spacer(modifier = Modifier.weight(1f))
            Text(text = schedule.classroom, color = textcolor, fontSize = 14.sp)
        }
        Text(text = schedule.name, color = textcolor, fontSize = 14.sp)
        Spacer(modifier = Modifier.size(8.dp))
        Text(text = schedule.teacher, color = textcolor, fontSize = 14.sp)
        Text(text = schedule.date, color = textcolor, fontSize = 14.sp)
    }
}

@Composable
fun NewsCard(title: String, subtitle: String) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        color = Color(0xFF252525),
        modifier = Modifier
            .clickable { }
            .fillMaxWidth()
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
                .align(Alignment.CenterVertically))
        }
    }
}

@Composable
fun SectionCard(title: String, cardcolor: Color, icon: ImageVector, onClick: () -> Unit) {
    Surface(
        color = Color(0xFF252525),
        modifier = Modifier
            .clickable { onClick }
            .size(100.dp, 130.dp)
            .clip(RoundedCornerShape(15.dp))
    ) {
        Column(modifier = Modifier.padding(15.dp), verticalArrangement = Arrangement.Center) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                Surface(modifier = Modifier.size(33.dp).clip(RoundedCornerShape(12.dp)), color = cardcolor){
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
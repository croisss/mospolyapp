package ru.mospolytech.mospolyapp

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mospolytech.mospolyapp.screens.montserrat

@Composable
fun TextComponent(value: String){
    Text(
        text = value,
        modifier = Modifier,
        style = TextStyle(
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Normal,
            fontFamily = montserrat
        )
    )
}

@Composable
fun BoldTextComponent(value: String){
    Text(
        text = value,
        modifier = Modifier,
        style = TextStyle(
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Normal,
            fontFamily = montserrat
        )
    )
}

@Composable
fun LightTextComponent(value: String){
    Text(
        text = value,
        modifier = Modifier,
        style = TextStyle(
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Normal,
            fontFamily = montserrat
        ),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
}

@Composable
fun HeadingTextComponent(value: String){
    Text(
        text = value,
        style = TextStyle(
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Normal,
            fontFamily = montserrat
        ),
        textAlign = TextAlign.Center
    )
}

@Composable
fun TextFieldComponent(labelValue:String, onTextSelected: (String) -> Unit, errorStatus: Boolean = false){
    var text = remember { mutableStateOf("") }

    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = text.value,
        onValueChange = {
            text.value = it
            onTextSelected(it)
        },
        label = { Text(text = labelValue, fontFamily = montserrat) },
        colors = TextFieldDefaults.colors(
            //setting the text field background when it is focused
            focusedContainerColor = Color(0xFF3a3a3a),
            //setting the text field background when it is unfocused or initial state
            unfocusedContainerColor = Color(0xFF3a3a3a),
            //setting the text field background when it is disabled
            disabledContainerColor = Color(0xFF3a3a3a),
            errorContainerColor = Color(0xFF3a3a3a)
        ),
        keyboardOptions = KeyboardOptions.Default,
        isError = !errorStatus
        //maybe make a leading icon, take it in as an argument in TextFieldComponent()

    )
}

@Composable
fun PasswordFieldComponent(labelValue:String, onTextSelected: (String) -> Unit, errorStatus: Boolean = false){
    var password = remember { mutableStateOf("") }

    TextField(
        modifier = Modifier
            .fillMaxWidth(),
        value = password.value,
        onValueChange = {
            password.value = it
            onTextSelected(it)
        },
        label = { Text(text = labelValue, fontFamily = montserrat)},
        colors = TextFieldDefaults.colors(
            //setting the text field background when it is focused
            focusedContainerColor = Color(0xFF3a3a3a),
            //setting the text field background when it is unfocused or initial state
            unfocusedContainerColor = Color(0xFF3a3a3a),
            //setting the text field background when it is disabled
            disabledContainerColor = Color(0xFF3a3a3a),
            errorContainerColor = Color(0xFF3a3a3a)
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done // Optional: Specify the IME action
        ),
        visualTransformation =  PasswordVisualTransformation(),
        isError = !errorStatus
    )
}

@Composable
fun ButtonComponent(value: String, onButtonClicked : () -> Unit){
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(48.dp),
        onClick = {
            onButtonClicked.invoke()
        },
        contentPadding = PaddingValues(),
        colors = ButtonDefaults.buttonColors(Color.Transparent),
        shape = RoundedCornerShape(10.dp)
    ){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(48.dp)
                .background(
                    Color(0xFF90b3e7),
                    shape = RoundedCornerShape(10.dp)
                ),
                contentAlignment = Alignment.Center
        )
        {
            Text(text = value,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = montserrat,
                color = Color.White
                )
        }
    }
}
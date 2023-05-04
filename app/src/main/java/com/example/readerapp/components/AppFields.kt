package com.example.readerapp.components

import android.opengl.Visibility
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun EmailField(
    modifier: Modifier = Modifier,
    emailState: MutableState<String>,
    imeAction: ImeAction = ImeAction.Next,
    labelId: String = "Email",

    enabled: Boolean = true,
    onAction: KeyboardActions = KeyboardActions.Default,


    ) {
    val passwordVisibility = rememberSaveable {
        mutableStateOf(false)
    }
    CustomTextField(
        modifier = modifier,
        valueState = emailState,
        labelId = labelId,
        isPassword = false,
        enabled = enabled,
        keyboardType = KeyboardType.Email,
        onAction = onAction,
        imeAction = imeAction,
        isPasswordVisibility = passwordVisibility


    )
}

@Composable
fun PasswordField(
    modifier: Modifier = Modifier,
    passwordState: MutableState<String>,
    imeAction: ImeAction = ImeAction.Done,
    labelId: String = "Password",
    enabled: Boolean = true,
    onAction: KeyboardActions = KeyboardActions.Default,
//    passwordVisibility: MutableState<Boolean>


    ) {
    val passwordVisibility = rememberSaveable {
        mutableStateOf(true)
    }
    CustomTextField(
        modifier = modifier,
        valueState = passwordState,
        labelId = labelId,
        enabled = enabled,
        isPassword = true,
        keyboardType = KeyboardType.Password,
        onAction = onAction,
        imeAction = imeAction,
        isPasswordVisibility = passwordVisibility

    )
}
package com.example.readerapp.components


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    valueState: MutableState<String>,
    labelId: String,
    enabled: Boolean,
    isPasswordVisibility: MutableState<Boolean> = mutableStateOf(false),
    isSingleLine: Boolean = true,
    isPassword: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    onAction: KeyboardActions = KeyboardActions.Default,

    ) {
    val visualTransformation =
        if (isPasswordVisibility.value) PasswordVisualTransformation()  else VisualTransformation.None
    OutlinedTextField(
        value = valueState.value,

        onValueChange = {
            valueState.value = it
        },
        enabled = enabled,
        label = {
            Text(text = labelId)
        },
        singleLine = isSingleLine,
        textStyle = TextStyle(
            fontSize = 15.sp,
            color = MaterialTheme.colors.onBackground,

            ),
        modifier = modifier
            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth(),
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType, imeAction = imeAction
        ),
        keyboardActions = onAction,
        visualTransformation =  visualTransformation,
        trailingIcon = {
           if (isPassword) PasswordVisibility(passwordVisibility = isPasswordVisibility)
        }


    )
}

@Composable
fun PasswordVisibility(passwordVisibility: MutableState<Boolean>) {
    val visible = passwordVisibility.value
    IconButton(onClick = {
        passwordVisibility.value = !visible
    },

    ) {
       Icon(imageVector = Icons.Rounded.Close, contentDescription = "Close Icon")
    }
}

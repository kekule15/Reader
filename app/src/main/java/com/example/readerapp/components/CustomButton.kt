package com.example.readerapp.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CustomButton(
    title: String,
    isLoading: MutableState<Boolean>,
    validInputs: Boolean,
    onclick: () -> Unit,
) {
    Button(
        onClick = onclick,
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(),
        enabled = !isLoading.value && validInputs,
        shape = RoundedCornerShape(size = 10.dp)
    ) {

        if (isLoading.value) CircularProgressIndicator(
            modifier = Modifier.size(25.dp)
        ) else Text(text = title, modifier = Modifier.padding(5.dp))
    }

}
package com.example.readerapp.screens.auth

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.readerapp.components.AppLogo
import com.example.readerapp.components.CustomButton
import com.example.readerapp.components.CustomTextField
import com.example.readerapp.components.EmailField
import com.example.readerapp.components.PasswordField
import com.example.readerapp.navigation.ReaderScreens

@Composable
fun RegisterScreen(navController: NavController, authViewModel: AuthViewModel = viewModel()) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            AppLogo()
            UserRegisterForm(
                navController = navController,
                isLoading = authViewModel.loading,
            ) { email, password, name ->
                Log.d("User Form", "LoginScreen:$email and $password ")
                authViewModel.createUser(email = email, password = password, name = name) {
                    navController.navigate(ReaderScreens.HomeScreen.name)
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Preview
@Composable
fun UserRegisterForm(
    navController: NavController = NavController(context = LocalContext.current),
    isLoading: MutableState<Boolean> = mutableStateOf(false),
    onDone: (String, String, String) -> Unit = { _, _, _ -> },

    ) {
    val fullName = rememberSaveable {
        mutableStateOf("")
    }
    val email = rememberSaveable {
        mutableStateOf("")
    }
    val password = rememberSaveable {
        mutableStateOf("")
    }

    val passwordFocusRequest = FocusRequester.Default
    val keyBoardController = LocalSoftwareKeyboardController.current
    val valid = rememberSaveable(email.value, password.value) {
        fullName.value.trim().isNotEmpty() && email.value.trim()
            .isNotEmpty() && password.value.trim().isNotEmpty()
    }

    val modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colors.background)
        .verticalScroll(
            rememberScrollState()
        )

    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Please enter a valid email and password that is at least 6 characters long",
            modifier = Modifier.padding(20.dp),
            textAlign = TextAlign.Center
        )
        CustomTextField(
            valueState = fullName,
            labelId = "Full Name",
            enabled = !isLoading.value,
            isPassword = false,
            keyboardType = KeyboardType.Text,
            onAction = KeyboardActions {
                if (fullName.value.trim().isEmpty()) return@KeyboardActions else

                    keyBoardController?.hide()
            },
            imeAction = ImeAction.Done,

            )

        EmailField(emailState = email,
            enabled = !isLoading.value,
            onAction = KeyboardActions {
                passwordFocusRequest.requestFocus()
            })
        PasswordField(
            modifier = Modifier.focusRequester(
                passwordFocusRequest
            ),
            passwordState = password,
            enabled = !isLoading.value,
            onAction = KeyboardActions {
                if (!valid) return@KeyboardActions else
                    onDone(
                        email.value.trim(), password.value.trim(), fullName.value.trim()
                    )
                keyBoardController?.hide()
            },
        )
        CustomButton(
            title = "Create Account",
            isLoading = isLoading,
            validInputs = valid
        ) {
            onDone(
                email.value.trim(), password.value.trim(), fullName.value.trim()
            )
            keyBoardController?.hide()
        }
        Spacer(modifier = Modifier.height(15.dp))

        Row(
            modifier = Modifier.padding(15.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Already have account?")

            Text(
                text = "Login Here", modifier = Modifier
                    .clickable {
                        navController.popBackStack()
                    }
                    .padding(start = 5.dp),
                fontWeight = FontWeight.Bold, color = MaterialTheme.colors.secondaryVariant
            )
        }


    }
}






package com.example.readerapp.screens.auth

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import java.lang.Exception


class AuthViewModel : ViewModel() {
    private val _loading = mutableStateOf(false)

    val loading: MutableState<Boolean> = _loading

    private val auth: FirebaseAuth = Firebase.auth

    private val db = FirebaseFirestore.getInstance().collection("Users")

    fun login(email: String, password: String, next: () -> Unit = {}) = viewModelScope.launch {
        try {
            _loading.value = true
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("Firebase", "Login: Logging successfully ${task.result.toString()}")
                    // TODO("Taker User to Home Screen")
                    _loading.value = false
                    next()
                } else {
                    _loading.value = false
                    Log.d("Firebase", "Login: error Logging ${task.result.toString()}")
                }
            }

        } catch (exc: Exception) {
            _loading.value = false
            Log.d("Firebase", "Login: ${exc.message}")
        }
    }

    fun createUser(email: String, password: String, name: String, next: () -> Unit = {}) =
        viewModelScope.launch {
            try {
                if (!_loading.value) {
                    _loading.value = true
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {

                                saveUserToDB(
                                    email = email, name = name,
                                )
                                _loading.value = false

                                next()
                                Log.d(
                                    "Firebase",
                                    "createUser: user created successfully ${task.result}"
                                )
                            } else {
                                _loading.value = false
                                Log.d("Firebase", "createUser: error creating user ${task.result}")
                            }
                        }
                }


            } catch (error: Exception) {
                _loading.value = false
                Log.d("Firebase", "createUser: ")
            }
        }

    private fun saveUserToDB(email: String, name: String,) {

        val user = mutableMapOf<String, Any>()
        user["id"] = db.id
            user["name"] = name
        user["email"] = email

        db.add(user)



    }
}
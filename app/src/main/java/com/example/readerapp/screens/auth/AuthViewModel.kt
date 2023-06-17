package com.example.readerapp.screens.auth

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.readerapp.model.MUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class AuthViewModel : ViewModel() {
    private val _loading = mutableStateOf(false)

    val loading: MutableState<Boolean> = _loading

    private val auth: FirebaseAuth = Firebase.auth

    private val db = FirebaseFirestore.getInstance().collection("Users")

    init {


    }
    fun loadData() =  viewModelScope.launch(Dispatchers.IO){

    }

    fun login(email: String, password: String, context: Context, next: () -> Unit = {}) = viewModelScope.launch {
        try {
            _loading.value = true
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("Firebase", "Login: Logging successfully")
                    // TODO("Take User to Home Screen")
                    _loading.value = false
                   next()
                } else {
                    Log.w("Firebase", "Error Logging ")
                    _loading.value = false
                    Toast.makeText(
                        context,
                        "Authentication failed.",
                        Toast.LENGTH_LONG,
                    ).show()

                }
            }

        } catch (exc: Exception) {
            _loading.value = false
            Log.d("Firebase", "Exception Error ${exc.message}")
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
                                Log.d(
                                    "Firebase",
                                    "createUser: user created successfully ${task.result}"
                                )
                                next()

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

    private fun saveUserToDB(email: String, name: String) {
        val id: String = db.document().id
        val user = MUser(
            userId = id,
            fullName = name,
            email = email,
            quote = "",
            avatar = "",
            profession = "Mobile Engineer"
        ).toMap()


        db.document(id).set(user)


    }
}
package com.example.readerapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.readerapp.ui.theme.ReaderAppTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {


            ReaderAppTheme {
//                val db = FirebaseFirestore.getInstance()
//                var user: MutableMap<String, Any> = hashMapOf()
//                user["firstName"] = "Joe"
//                user["lastName"] = "Augustus"
//
//                db.collection("Users").add(user).addOnSuccessListener {
//                    Log.d("Document", "onCreate: ${it.id}")
//                }.addOnFailureListener {
//                    Log.d("Document", "onCreate: $it")
//                }
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ReaderAppTheme {
        Greeting("Android")
    }
}
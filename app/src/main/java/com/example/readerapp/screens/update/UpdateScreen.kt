package com.example.readerapp.screens.update

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.AlertDialog
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.readerapp.components.CustomTextField
import com.example.readerapp.components.RatingBar
import com.example.readerapp.components.ReaderAppBar
import com.example.readerapp.components.RoundedButton
import com.example.readerapp.components.showToast
import com.example.readerapp.data.BookItem
import com.example.readerapp.data.DataOrException
import com.example.readerapp.data.Resource
import com.example.readerapp.model.BookModel
import com.example.readerapp.screens.detail.BookInfoWidget
import com.example.readerapp.screens.home.HomeViewModel
import com.example.readerapp.utils.formatDate
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import okhttp3.internal.wait

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun UpdateScreen(
    navController: NavHostController,
    bookId: String,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    Log.d("Book Google ID", "UpdateScreen: $bookId")
    Log.d("Book Data", "UpdateScreen: ${viewModel.data.value}")
    Scaffold(topBar = {
        ReaderAppBar(
            title = "Update Book",
            navController = navController,
            showProfile = false,
            icon = Icons.Default.ArrowBack
        ) {
            navController.popBackStack()
        }
    }) {


        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(3.dp)
        ) {
            ShowBookUpdate(
                bookInfo = viewModel.data.value,
                bookId = bookId,
                navController,
                viewModel
            )
        }
    }
}

@Composable
fun ShowBookUpdate(
    bookInfo: DataOrException<List<BookModel>, Boolean, Exception>,
    bookId: String,
    navController: NavController,
    viewModel: HomeViewModel,
) {

    if (bookInfo.loading == true) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp)
            )
        }
    }
    if (!bookInfo.data.isNullOrEmpty()) {

        val book = bookInfo.data!!.first { book ->
            book.googleBookId == bookId
        }

        Column() {
            BookInfoWidget(bookInfo = book)
            ShowSimpleForm(bookInfo = book, navController = navController, viewModel)
        }
    }

}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ShowSimpleForm(bookInfo: BookModel, navController: NavController, viewModel: HomeViewModel) {
    val defaultValue: String =
        if (bookInfo.notes.toString().isNotEmpty()) bookInfo.notes.toString() else {
            "No thoughts available"
        }
    val notesText = remember {
        mutableStateOf("")
    }
    val isStartedReading = remember {
        mutableStateOf(false)
    }
    val isFinishedReading = remember {
        mutableStateOf(false)
    }

    val ratingVal = remember {
        mutableStateOf(0)
    }

    val context = LocalContext.current

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        SimpleForm(defaultValue = defaultValue) { note ->
            notesText.value = note
        }
        Row(
            modifier = Modifier.padding(4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(
                onClick = { isStartedReading.value = true },
                enabled = bookInfo.startedReading == null
            ) {
                if (bookInfo.startedReading == null) {
                    if (!isStartedReading.value) {
                        Text(text = "Start Reading")
                    } else {
                        Text(
                            text = "Started Reading",
                            modifier = Modifier.alpha(0.6f),
                            color = Color.Red.copy(alpha = 0.5f)
                        )
                    }
                } else {
                    Text(text = "Start Reading on : ${formatDate(bookInfo.startedReading!!)}") // TODO: Format date
                }
            }
            Spacer(modifier = Modifier.width(20.dp))
            TextButton(
                onClick = { isFinishedReading.value = true },
                enabled = bookInfo.finishedReading == null
            ) {
                if (bookInfo.finishedReading == null) {
                    if (!isFinishedReading.value) {
                        Text(text = "Mark as Read ")
                    } else {
                        Text(
                            text = "Finished Reading",
                            modifier = Modifier.alpha(0.6f),
                            color = Color.Red.copy(alpha = 0.5f)
                        )
                    }
                } else {
                    Text(text = "Finished Reading on : ${formatDate(bookInfo.finishedReading!!)}") // TODO: Format date
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        Text(text = "Rating", modifier = Modifier.padding(bottom = 3.dp))
        Spacer(modifier = Modifier.height(10.dp))
        bookInfo.rating?.toInt().let {
            RatingBar(rating = it!!) { rating ->
                ratingVal.value = rating
            }
        }
        Spacer(modifier = Modifier.height(15.dp))
        Row(horizontalArrangement = Arrangement.SpaceBetween) {
            val changedNotes = bookInfo.notes != notesText.value
            val changedRating = bookInfo.rating?.toInt() != ratingVal.value
            val isFinishedTimestamp =
                if (isFinishedReading.value) Timestamp.now() else bookInfo.finishedReading
            val isStartedTimestamp =
                if (isStartedReading.value) Timestamp.now() else bookInfo.startedReading

            val bookUpdate =
                changedNotes || changedRating || isFinishedReading.value || isStartedReading.value

            val dataToUpdate = hashMapOf(
                "finished_reading_at" to isFinishedTimestamp,
                "started_reading_at" to isStartedTimestamp,
                "rating" to ratingVal.value,
                "notes" to notesText.value

            ).toMap()
            RoundedButton(label = "Update") {
                if (bookUpdate) {
                    FirebaseFirestore.getInstance().collection("books").document(bookInfo.id!!)
                        .update(dataToUpdate).addOnCompleteListener {
                            showToast(context, "Book Updated Successfully")
                            navController.popBackStack()
                            // Log.d("Complete", "ShowSimpleForm: ${it.result.toString()}")
                        }.addOnFailureListener {
                            showToast(context, "Error Updating Book")
                            //Log.d("Error", "Error Updating document , $it")
                        }
                }
            }
            Spacer(modifier = Modifier.width(100.dp))
            val openDialog = remember {
                mutableStateOf(false)
            }
            if (openDialog.value) {
                ShowAlertDialog(
                    message = "Are you sure you want to delete this book",
                    openDialog = openDialog
                ) {


                    FirebaseFirestore.getInstance().collection("books").document(bookInfo.id!!).delete()
                        .addOnCompleteListener {
                            if (it.isSuccessful) {

                                openDialog.value = false
                                navController.popBackStack()
                                viewModel.launchDataLoad()

                            }
                        }.addOnFailureListener{
                            showToast(context = context, "Error Deleting Book $it")
                        }
                }
            }
            RoundedButton(label = "Delete") {
                openDialog.value = true
            }
        }
    }
}

@Composable
fun ShowAlertDialog(
    message: String,
    openDialog: MutableState<Boolean>,
    onYesPressed: () -> Unit = {},
) {
    if (openDialog.value) {
        AlertDialog(onDismissRequest = {}, title = {
            Text(text = "Delete Book")
        }, text = {
            Text(text = message)
        }, buttons = {
            Row(
                modifier = Modifier.padding(8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                TextButton(onClick = { onYesPressed.invoke() }) {
                    Text(text = "Yes")
                }
                TextButton(onClick = { openDialog.value = false }) {
                    Text(text = "No")
                }
            }
        })
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SimpleForm(
    modifier: Modifier = Modifier,
    loading: Boolean = false,
    defaultValue: String = "Great Book",
    onSearch: (String) -> Unit = {},
) {
    val textFieldValue = rememberSaveable {
        mutableStateOf(defaultValue)
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    val valid = remember(textFieldValue.value) {
        textFieldValue.value.trim().isNotEmpty()
    }

    CustomTextField(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .padding(3.dp)
            .background(Color.Transparent, CircleShape)
            .padding(horizontal = 20.dp, vertical = 12.dp),
        valueState = textFieldValue,
        labelId = "Enter Your thoughts",
        enabled = true,
        onAction = KeyboardActions {
            if (!valid) return@KeyboardActions
            onSearch(textFieldValue.value.trim())
            keyboardController?.hide()
        }
    )
}

@Composable
fun BookInfoWidget(bookInfo: BookModel) {
    Surface(
        modifier = Modifier
            .padding(3.dp)
            .fillMaxWidth(),
        shape = CircleShape,
        elevation = 4.dp
    ) {
        Row {
            Spacer(modifier = Modifier.width(45.dp))
            Column(modifier = Modifier.padding(4.dp), verticalArrangement = Arrangement.Center) {
                CardListItem(book = bookInfo,
                    onPressed = {})
            }
        }
    }

}

@Composable
fun CardListItem(book: BookModel, onPressed: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(start = 4.dp, top = 4.dp, bottom = 8.dp, end = 4.dp)
            .clip(
                RoundedCornerShape(20.dp)
            )
            .clickable { }, elevation = 8.dp
    ) {
        Row {
            AsyncImage(
                model = book.photoUrl,
                contentDescription = "Book Image",
                modifier = Modifier
                    .height(100.dp)
                    .width(120.dp)
                    .padding(4.dp)
                    .clip(
                        RoundedCornerShape(
                            topStart = 120.dp, topEnd = 20.dp, bottomEnd = 0.dp, bottomStart = 0.dp
                        )
                    ),

                )
            Column {
                Text(
                    text = book.title!!,
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier
                        .padding(start = 8.dp, end = 8.dp)
                        .width(120.dp),
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = book.authors!!,
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp),

                    )
                Text(
                    text = book.publishedDate!!,
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp),

                    )
            }
        }
    }
}

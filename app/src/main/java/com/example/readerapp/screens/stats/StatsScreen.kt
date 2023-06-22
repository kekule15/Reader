package com.example.readerapp.screens.stats

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.sharp.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.readerapp.components.ReaderAppBar
import com.example.readerapp.components.TitleSection
import com.example.readerapp.data.BookItem
import com.example.readerapp.model.BookModel
import com.example.readerapp.navigation.ReaderScreens
import com.example.readerapp.screens.home.HomeViewModel
import com.example.readerapp.screens.search.BookRow
import com.example.readerapp.screens.update.ShowBookUpdate
import com.example.readerapp.utils.formatDate
import com.google.firebase.auth.FirebaseAuth

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun StatsScreen(navController: NavHostController, viewModel: HomeViewModel = hiltViewModel()) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    var books: List<BookModel>

    Scaffold(topBar = {
        ReaderAppBar(
            title = "Book Stats",
            navController = navController,
            showProfile = false,
            icon = Icons.Default.ArrowBack
        ) {
            navController.popBackStack()
        }
    }) {
        books = if (!viewModel.user.collectAsState().value.data.isNullOrEmpty()) {
            viewModel.user.collectAsState().value.data!!.filter { book ->
                book.userId == currentUser?.uid!!
            }
        } else {
            emptyList()
        }

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {


            Column() {
                Row() {
                    Box(
                        modifier = Modifier
                            .size(45.dp)
                    ) {
                        Icon(imageVector = Icons.Sharp.Person, contentDescription = "Person Icon")
                    }
                    Text(text = "Hi ${currentUser?.email.toString().split("@")[0].uppercase()}")
                }
                StatsBoxWidget(books = books)
                Spacer(modifier = Modifier.height(5.dp))
                TitleSection(label = "Read Books")
                Spacer(modifier = Modifier.height(5.dp))
                ReadBooks(books = books, navController = navController)
            }


        }
    }
}

@Composable
fun StatsBoxWidget(books: List<BookModel>) {
    val readerBooksList = if (!books.isNullOrEmpty()) books.filter { book ->
        book.finishedReading != null
    } else {
        emptyList()
    }
    val readingBooks = if (!books.isNullOrEmpty()) books.filter { book ->
        book.startedReading != null && book.finishedReading == null
    } else {
        emptyList()
    }
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RectangleShape,
        elevation = 5.dp
    ) {

        Column(
            modifier = Modifier.padding(start = 25.dp, top = 4.dp, bottom = 4.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(text = "Your Stats", style = MaterialTheme.typography.h5)
            Divider()
            Text(
                text = "Your are reading: ${readingBooks.size} books",
                style = MaterialTheme.typography.caption
            )
            Text(
                text = "You've read: ${readerBooksList.size} books",
                style = MaterialTheme.typography.caption
            )
            Spacer(modifier = Modifier.height(10.dp))


        }
    }
}

@Composable
fun ReadBooks(books: List<BookModel>, navController: NavController) {
    val readBooks: List<BookModel> = if (!books.isNullOrEmpty()) {
        books.filter { book ->
            book.startedReading != null && book.finishedReading != null
        }
    } else {
        emptyList()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        //contentPadding = PaddingValues(16.dp)
    ) {
        if (books.isNotEmpty()) {

            items(items = readBooks) { book ->
                ReadBookRow(book = book, navController = navController)
            }
        }
    }
}

@Composable
fun ReadBookRow(book: BookModel, navController: NavController) {

    val url =
        "http://books.google.com/books/content?id=LY1FDwAAQBAJ&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api"

    val imageURl =
        if (book.photoUrl == null
        ) url else {

            book.photoUrl
        }

    Card(modifier = Modifier
        .clickable {
//            navController.navigate(ReaderScreens.DetailsScreen.name + "/${book.id}")
        }
        .fillMaxWidth()
        .height(100.dp)
        .padding(top = 10.dp),
        shape = RectangleShape,
        elevation = 7.dp) {
        Row(
            modifier = Modifier.padding(0.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {

            Surface(
                modifier = Modifier
                    .padding(0.dp)
                    .fillMaxHeight()
                    .width(100.dp),
                color = Color.LightGray,
                shape = RectangleShape
            ) {
                AsyncImage(
                    model = imageURl,
                    contentDescription = "Book Image",
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth()
                        .padding(0.dp),
                    contentScale = ContentScale.Crop,

                    )
            }
            Column(modifier = Modifier.padding(5.dp), verticalArrangement = Arrangement.Center) {
                Row() {
                    Text(text = book.title.toString(),
                        softWrap = true,
                        overflow = TextOverflow.Ellipsis)
                    if (book.rating!!.toInt() >= 4) {
                        Spacer(modifier = Modifier.fillMaxWidth(0.8f))
                        Icon(
                            imageVector = Icons.Default.ThumbUp,
                            contentDescription = "Thumbs Up",
                            tint = Color.Green.copy(
                                alpha = 0.5f
                            )
                        )
                    }
                }
                Text(
                    text = "Authors:  ${book.authors}",
                    overflow = TextOverflow.Clip,

                    style = MaterialTheme.typography.caption,
                    fontStyle = FontStyle.Italic
                )
                Text(
                    text = "Started:  ${formatDate(book.startedReading!!)}",
                    overflow = TextOverflow.Clip,
                    softWrap = true,
                    style = MaterialTheme.typography.caption,
                    fontStyle = FontStyle.Italic
                )
                Text(
                    text = "Finished:  ${formatDate(book.finishedReading!!)}",
                    overflow = TextOverflow.Clip,
                    softWrap = true,
                    style = MaterialTheme.typography.caption,
                    fontStyle = FontStyle.Italic
                )
            }

        }
    }

}

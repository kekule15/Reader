package com.example.readerapp.screens.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.readerapp.components.ListCard
import com.example.readerapp.components.ReaderAppBar
import com.example.readerapp.components.TitleSection
import com.example.readerapp.model.BookModel
import com.example.readerapp.navigation.ReaderScreens
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Preview
@Composable
fun HomeScreen(
    navController: NavController = NavController(context = LocalContext.current),
    viewModel: HomeViewModel = hiltViewModel(),
) {
    Scaffold(topBar = {
        ReaderAppBar(title = "A. Reader", navController = navController)
    }, floatingActionButton = {
        FABContent {
            navController.navigate(ReaderScreens.SearchScreen.name)
        }
    }) {
        Surface(modifier = Modifier.fillMaxSize()) {
            HomeContent(navController = navController, viewModel = viewModel)
        }
    }
}


@Composable
fun HomeContent(navController: NavController, viewModel: HomeViewModel) {
//    val isLoggedIn = FirebaseAuth.getInstance().currentUser != null
//    val userName: String = if (isLoggedIn) FirebaseAuth.getInstance().currentUser?.email?.split("@")
//        ?.get(0).toString() else
//        "N/A"
    Column(
        modifier = Modifier.padding(start = 20.dp),
        verticalArrangement = Arrangement.Top,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),

            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TitleSection(label = "Your reading \n" + "activity right now...")

            Column(modifier = Modifier.padding(10.dp), horizontalAlignment = Alignment.End) {
                Icon(imageVector = Icons.Filled.AccountCircle,
                    contentDescription = "Profile",
                    modifier = Modifier
                        .clickable {
                            navController.navigate(ReaderScreens.StatsScreen.name)
                        }
                        .size(45.dp),
                    tint = MaterialTheme.colors.secondaryVariant)
                Text(
                    text = "Name",
                    modifier = Modifier.padding(2.dp),
                    style = MaterialTheme.typography.overline,
                    color = Color.Red,
                    fontSize = 15.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Clip
                )

            }

        }
        var currentUser = FirebaseAuth.getInstance().currentUser
        val listOfBooks: List<BookModel>
        if (!viewModel.user.collectAsState().value.data.isNullOrEmpty()) {
            listOfBooks = viewModel.user.collectAsState().value.data!!

            ReadingNowWidget(books = listOfBooks, navController = navController)
            TitleSection(label = "Reading List")

            BookListArea(books = listOfBooks, navController = navController)
        } else {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .align(alignment = Alignment.CenterHorizontally),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(modifier = Modifier.size(40.dp))
                Text(text = "Loading...")
            }
        }


    }
}

@Composable
fun ReadingNowWidget(books: List<BookModel>, navController: NavController) {
    val readingNowList = books.filter { book ->
        book.startedReading != null && book.finishedReading == null
    }

    if (readingNowList.isNullOrEmpty()) {
        Column() {
            Spacer(modifier = Modifier.height(50.dp))
            Text(text = "No books in this collection...")
            Text(text = "Start reading books to get this collection updated")
            Spacer(modifier = Modifier.height(100.dp))
        }
    } else {
        HorizontalScrollableComponent(readingNowList) { bookId ->
            navController.navigate(ReaderScreens.UpdateScreen.name + "/$bookId")
        }
    }


}

@Composable
fun BookListArea(books: List<BookModel>, navController: NavController) {
    val bookList = books.filter { book ->
        book.startedReading == null && book.finishedReading == null
    }
    if (bookList.isNullOrEmpty()) {
        Column() {
            Spacer(modifier = Modifier.height(100.dp))
            Text(text = "No books in this collection...")
            Text(text = "Add books to get this collection updated")
        }
    } else {
        HorizontalScrollableComponent(bookList) { bookId ->
            navController.navigate(ReaderScreens.UpdateScreen.name + "/$bookId")
        }
    }


}

@Composable
fun HorizontalScrollableComponent(books: List<BookModel>, onTap: (String) -> Unit) {
    val scrollState = rememberScrollState()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp)
            .horizontalScroll(scrollState)
    ) {
        for (book in books) {
            ListCard(bookModel = book) {
                onTap(book.googleBookId.toString())
            }
        }
    }
}


@Composable
fun FABContent(onTap: () -> Unit) {
    FloatingActionButton(
        onClick = { onTap() },
        modifier = Modifier.size(50.dp),
        shape = CircleShape,
        backgroundColor = Color(0xFF92CBDF)
    ) {
        Icon(
            imageVector = Icons.Default.Add, contentDescription = "Add Icon", tint = Color.White
        )

    }

}

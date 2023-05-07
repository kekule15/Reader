@file:OptIn(ExperimentalComposeUiApi::class)

package com.example.readerapp.screens.search

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.readerapp.components.CustomTextField
import com.example.readerapp.components.ReaderAppBar
import com.example.readerapp.data.BookModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Preview
@Composable
fun SearchScreen(navController: NavController = NavController(context = LocalContext.current)) {
    Scaffold(topBar = {
        ReaderAppBar(
            title = "Search Screen",
            navController = navController,
            showProfile = false,
            icon = Icons.Default.ArrowBack
        ) {
            navController.popBackStack()
        }
    }) {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SearchForm {

                }
                BookList(navController = navController)
            }
        }
    }
}

@Composable
fun BookList(navController: NavController) {
    LazyColumn(modifier = Modifier.fillMaxWidth(), contentPadding = PaddingValues(16.dp)) {
        val bookListItems = listOf(
            BookModel(
                id = "1",
                title = "Title",
                note = "Notes",
                author = "Augustus"
            ),
            BookModel(
                id = "1",
                title = "Title",
                note = "Notes",
                author = "Augustus"
            ),
            BookModel(
                id = "1",
                title = "Title",
                note = "Notes",
                author = "Augustus"
            ),
            BookModel(
                id = "1",
                title = "Title",
                note = "Notes",
                author = "Augustus"
            ),
            BookModel(
                id = "1",
                title = "Title",
                note = "Notes",
                author = "Augustus"
            ),
            BookModel(
                id = "1",
                title = "Title",
                note = "Notes",
                author = "Augustus"
            ),
        )
        items(items = bookListItems) { item ->
            BookRow(book = item, navController = navController)
        }

    }
}

@Composable
fun BookRow(book: BookModel, navController: NavController) {
    Card(
        modifier = Modifier
            .clickable { }
            .fillMaxWidth()
            .height(100.dp)
            .padding(top = 10.dp),
        shape = RectangleShape,
        elevation = 7.dp
    ) {
        Row(
            modifier = Modifier.padding(0.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            val imageURl =
                "http://books.google.com/books/content?id=LY1FDwAAQBAJ&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api"
            Surface(
                modifier = Modifier
                    .padding(0.dp)
                    .fillMaxHeight()
                    .width(100.dp), color = Color.LightGray, shape = RectangleShape
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
                Text(text = book.title, overflow = TextOverflow.Ellipsis)
                Text(
                    text = "Authors:  ${book.author}",
                    overflow = TextOverflow.Clip,
                    style = MaterialTheme.typography.caption
                )
            }

        }
    }

}

@Composable
fun SearchForm(
    modifier: Modifier = Modifier,
    loading: Boolean = false,
    hint: String = "Search",
    onSearch: (String) -> Unit = {},
) {
    Column {
        val searchQueryState = rememberSaveable {
            mutableStateOf("")
        }
        val keyboardController = LocalSoftwareKeyboardController.current
        val valid = remember(searchQueryState.value) {
            searchQueryState.value.trim().isNotEmpty()
        }


        CustomTextField(
            valueState = searchQueryState,
            labelId = hint,
            enabled = true,
            onAction = KeyboardActions {
                if (!valid) return@KeyboardActions
                onSearch(searchQueryState.value.trim())
                searchQueryState.value = ""
                keyboardController?.hide()
            })
    }
}
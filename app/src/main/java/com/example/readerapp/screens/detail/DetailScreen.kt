package com.example.readerapp.screens.detail

import android.annotation.SuppressLint
import android.widget.Space
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.readerapp.components.ReaderAppBar
import com.example.readerapp.data.BookItem
import com.example.readerapp.data.Resource

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun DetailScreen(
    navController: NavController,
    bookId: String,
    viewModel: DetailsViewModel = hiltViewModel(),
) {
    Scaffold(topBar = {
        ReaderAppBar(
            title = "Book Details",
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
            Column(
                modifier = Modifier.padding(top = 12.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                val bookInfo = produceState<Resource<BookItem>>(initialValue = Resource.Loading()) {
                    value = viewModel.getBook(bookId)
                }.value

                if (bookInfo.data == null) {
                    CircularProgressIndicator()
                } else {
                    BookInfoWidget(data = bookInfo, navController = navController)
                }

            }
        }
    }
}

@Composable
fun BookInfoWidget(data: Resource<BookItem>, navController: NavController) {
    val bookData = data.data?.volumeInfo
    val cleanDescription =
        HtmlCompat.fromHtml(bookData!!.description, HtmlCompat.FROM_HTML_MODE_LEGACY)

    val deviceHSize = LocalContext.current.resources.displayMetrics
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        Card(modifier = Modifier.padding(34.dp), shape = CircleShape, elevation = 4.dp) {

            AsyncImage(
                model = bookData?.imageLinks!!.smallThumbnail,
                contentDescription = "Book Image",
                modifier = Modifier
                    .size(90.dp)
                    .padding(1.dp),
                contentScale = ContentScale.Crop,

                )

        }
        Text(
            text = bookData!!.title,
            style = MaterialTheme.typography.h6,
            overflow = TextOverflow.Ellipsis,
            maxLines = 14

        )
        Text(
            text = "Authors: ${bookData.authors}",
            style = MaterialTheme.typography.subtitle1,

            )
        Text(
            text = "Page Count: ${bookData.pageCount}",
            style = MaterialTheme.typography.subtitle1,

            )
        Text(
            text = "Categories: ${bookData.categories}",
            style = MaterialTheme.typography.subtitle1,

            )
        Text(
            text = "Published: ${bookData.publishedDate}",
            style = MaterialTheme.typography.subtitle1,

            )
        Spacer(modifier = Modifier.padding(bottom = 20.dp))


        Surface(
            modifier = Modifier
                .height(deviceHSize.heightPixels.dp.times(0.09f))
                .padding(4.dp),
            shape = RectangleShape
        ) {
            LazyColumn(modifier = Modifier.padding(5.dp)) {
                item {
                    Text(
                        text = "Description: $cleanDescription",
                        style = MaterialTheme.typography.subtitle1,

                        )
                }
            }

        }


    }
}

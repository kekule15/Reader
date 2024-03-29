package com.example.readerapp.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.readerapp.model.BookModel


@Composable
fun ListCard(
    bookModel: BookModel,
    onTap: (String) -> Unit = {},
) {
    val context = LocalContext.current
    val resources = context.resources

    val displayWidth = resources.displayMetrics
    val screenWidth = displayWidth.widthPixels / displayWidth.density

    val spacing = 10.dp

    val isReading = remember {
        mutableStateOf(false)
    }

    Card(
        shape = RoundedCornerShape(29.dp),
        elevation = 6.dp,

//        backgroundColor = Color.White,
        modifier = Modifier
            .padding(start = 0.dp, end = 16.dp, top = 16.dp, bottom = 16.dp)
            .height(240.dp)
            .width(210.dp)
            .clickable { onTap.invoke(bookModel.title.toString()) },
        border = BorderStroke(1.dp, color = Color.LightGray)
    ) {
        Column(
            modifier = Modifier.width(screenWidth.dp - (spacing * 2)),
            horizontalAlignment = Alignment.Start
        ) {
            Row(horizontalArrangement = Arrangement.SpaceBetween) {

                Surface(
                    modifier = Modifier
                        .height(150.dp)
                        .width(140.dp)
                        .padding(end = 10.dp),


                    ) {
                    AsyncImage(
                        model = bookModel.photoUrl,
                        contentDescription = "Book Image",
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(topStartPercent = 26, bottomEndPercent = 24)),
                        contentScale = ContentScale.Crop,
                    )
                }
                Column(
                    modifier = Modifier.padding(top = 25.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.FavoriteBorder,
                        contentDescription = "Fav Icon",
                        modifier = Modifier.padding(bottom = 1.dp)
                    )
                    BookRating(score = bookModel.rating!!.toDouble())

                }
            }
            Text(
                text = bookModel.title!!,
                modifier = Modifier.padding(4.dp),
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "Authors: ${bookModel.authors}",
                modifier = Modifier.padding(4.dp),
                style = MaterialTheme.typography.caption
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.Bottom,
            ) {
                isReading.value = bookModel.startedReading != null
                RoundedButton(label = if (isReading.value) "Reading" else "Not yet", radius = 70)
            }
        }
    }
}

//@Preview
@Composable
fun RoundedButton(label: String = "Reading", radius: Int = 29, onTap: () -> Unit = { }) {
    Surface(
        modifier = Modifier.clip(
            shape = RoundedCornerShape(
                topStartPercent = radius, bottomEndPercent = radius
            ),
        ), color = Color(0xFF92CBDF)
    ) {
        Column(
            modifier = Modifier
                .width(90.dp)
                .height(40.dp)
                .clickable { onTap() },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label, style = TextStyle(
                    color = Color.White, fontSize = 15.sp
                )
            )
        }
    }
}

@Composable
fun BookRating(score: Double) {
    Surface(
        modifier = Modifier
            .height(70.dp)
            .padding(4.dp),
        shape = RoundedCornerShape(56.dp),
        elevation = 10.dp,
        //color = Color.White
        border = BorderStroke(1.dp, color = Color.LightGray)
    ) {
        Column(
            modifier = Modifier.padding(4.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = "Star Icon",
                modifier = Modifier.padding(3.dp)
            )
            Text(text = score.toString(), style = MaterialTheme.typography.subtitle1)
        }
    }
}
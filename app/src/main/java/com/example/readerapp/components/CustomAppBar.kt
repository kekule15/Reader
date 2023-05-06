package com.example.readerapp.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.readerapp.R
import com.example.readerapp.navigation.ReaderScreens
import com.google.firebase.auth.FirebaseAuth

@Composable
fun TitleSection(label: String) {
    Text(text = label, style = MaterialTheme.typography.h6)
}

@Composable
fun ReaderAppBar(
    title: String,
    icon: ImageVector? = null,
    showProfile: Boolean = true,
    navController: NavController,
    onBackArrowClicked: () -> Unit = {},
) {
    TopAppBar(title = {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (icon != null) {
                IconButton(onClick = { onBackArrowClicked.invoke() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Arrow back",
                        tint = Color.Red.copy(alpha = 0.7f)
                    )
                }
            }
            Spacer(modifier = Modifier.width(40.dp))

            if (showProfile) {
                Icon(
                    imageVector = Icons.Default.AccountBox,
                    contentDescription = "Logo Icon",
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .scale(0.9f)
                )
            }
            Text(
                text = title, color = Color.Red.copy(alpha = 0.7f), style = TextStyle(
                    fontWeight = FontWeight.Bold, fontSize = 23.sp
                )
            )
        }
    }, actions = {
        if (showProfile) {
            IconButton(onClick = {
                FirebaseAuth.getInstance().signOut()
                    .run { navController.navigate(ReaderScreens.LoginScreen.name) }
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.logout),
                    contentDescription = "Logout Icon",
                    modifier = Modifier.size(30.dp),
                    tint = Color.Red
                )
            }
        }

    }, backgroundColor = Color.Transparent, elevation = 0.dp)

}

package com.example.threadsappclone.item_view

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.threadsappclone.R
import com.example.threadsappclone.model.AddThreadModel
import com.example.threadsappclone.model.UserModel
import com.example.threadsappclone.navigation.Routes
import com.example.threadsappclone.screens.OtherUsers
import com.example.threadsappclone.utils.SharedPref
import kotlin.random.Random

@Composable
fun UserItem(userModel : UserModel ,
               navController : NavHostController){
    val context = LocalContext.current
    val lightColors = listOf(
        Color(0xFFFFC1CC), // Light Pink
        Color(0xFFFFF5BA), // Light Yellow
        Color(0xFFB2F2BB), // Light Green
        Color(0xFFCCE5FF), // Light Blue
        Color(0xFFD4C4FB), // Light Purple
        Color(0xFFFFE4C4), // Light Beige
        Color(0xFFFFD1DC), // Light Rose
        Color(0xFFFFF3E0)  // Light Peach
    )

    fun randomLightGradient(): Brush {
        val colors = List(5) { lightColors.random() }
        return Brush.linearGradient(colors)
    }
    Column() {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
                .clickable {
                    val routes = Routes.OtherUsers.routes.replace("{data}",userModel.uid)
                    navController.navigate(routes)
                }
        ){
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxSize()
                    .background(brush = randomLightGradient())
                    .padding(16.dp)
            ) {
                val (userImage , userName, name) = createRefs()


                Image(painter = rememberAsyncImagePainter(model = userModel.imageContent) ,
                    contentDescription = null ,
                    modifier = Modifier
                        .constrainAs(userImage) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                        }
                        .size(40.dp)
                        .clip(CircleShape) ,
                    contentScale = ContentScale.Crop
                )

                Text(text = userModel.userName , style = TextStyle(
                    fontWeight = FontWeight.ExtraBold ,
                    fontSize = 23.sp,
                    fontFamily = FontFamily.SansSerif
                ) , modifier = Modifier
                    .padding(start = 13.dp)
                    .constrainAs(userName) {
                        start.linkTo(userImage.end)
                        top.linkTo(parent.top)
                    }
                )

                Text(text = userModel.name , style = TextStyle(
                    fontWeight = FontWeight.Light ,
                    fontSize = 20.sp,
                    fontFamily = FontFamily.Cursive
                ) , modifier = Modifier
                    .padding(start = 13.dp)
                    .constrainAs(name) {
                        start.linkTo(userName.start)
                        top.linkTo(userName.bottom)
                    }
                )

            }
        }
        Divider(
            color = Color.LightGray,
            thickness = 3.dp
        )
    }
}
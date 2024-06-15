package com.example.threadsappclone.item_view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.example.threadsappclone.utils.SharedPref
import kotlin.random.Random

@Preview(showBackground = true)
@Composable
fun ThreadItem(thread : AddThreadModel , userModel : UserModel ,
               navController : NavHostController , userId : String){
    val context = LocalContext.current
    val randomColor = Color(
        red = Random.nextFloat(),
        green = Random.nextFloat(),
        blue = Random.nextFloat(),
        alpha = 0.5f
    )
    Column() {
        Card(
            modifier = Modifier.fillMaxSize().padding(10.dp)
        ){
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxSize()
                    .background(randomColor)
                    .padding(16.dp)
            ) {
                val (userImage , userName , date , time , title , image) = createRefs()


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
                    fontSize = 25.sp,
                    fontFamily = FontFamily.SansSerif
                ) , modifier = Modifier
                    .padding(start = 13.dp)
                    .constrainAs(userName) {
                        start.linkTo(userImage.end)
                        top.linkTo(parent.top)
                    }
                )

                Text(text = thread.thread , style = TextStyle(
                    fontFamily = FontFamily.Cursive ,
                    fontSize = 22.sp
                ) , modifier = Modifier
                    .padding(top = 3.dp , start = 13.dp)
                    .constrainAs(title) {
                        start.linkTo(userName.start)
                        top.linkTo(userName.bottom)
                    }
                    .width(250.dp)
                )

                if (thread.imageLink != "") {
                    Image(
                        painter = rememberAsyncImagePainter(model = thread.imageLink) ,
                        contentDescription = null ,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                            .constrainAs(image) {
                                top.linkTo(title.bottom , margin = 3.dp)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                            .background(color = Color.DarkGray, RoundedCornerShape(20.dp))
                    )
                }

            }
        }
        Divider(
            color = Color.LightGray,
            thickness = 3.dp
        )
    }
}
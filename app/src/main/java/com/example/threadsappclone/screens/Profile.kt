package com.example.threadsappclone.screens

import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ElevatedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.threadsappclone.navigation.Routes
import com.example.threadsappclone.viewmodel.AuthViewModel
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.ViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import coil.compose.rememberAsyncImagePainter
import com.example.threadsappclone.item_view.ThreadItem
import com.example.threadsappclone.model.UserModel
import com.example.threadsappclone.model.UserViewModel
import com.example.threadsappclone.utils.SharedPref
import com.google.firebase.auth.FirebaseAuth

@Composable
fun Profile(navController: NavHostController) {

    val authViewModel: AuthViewModel = viewModel()
    val firebaseUser by authViewModel.firebaseUser.observeAsState()
    val context = LocalContext.current
    val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid

    val userViewModel: UserViewModel = viewModel()
    val threads by userViewModel.threads.observeAsState()

    val followerList by userViewModel.followerList.observeAsState(null)
    val followingList by userViewModel.followingList.observeAsState(null)

    if(currentUserId!="") {
        userViewModel.getFollowers(currentUserId)
        userViewModel.getFollowing(currentUserId)
    }

    val user = UserModel(
        name = SharedPref.getName(context),
        imageContent = SharedPref.getImage(context),
        userName = SharedPref.getUserName(context)
    )

    if(firebaseUser!=null) {
        userViewModel.fetchThread(firebaseUser!!.uid)
    }

    LaunchedEffect(key1 = firebaseUser) {
        if (firebaseUser == null) {
            navController.navigate(Routes.Login.routes){
                popUpTo(navController.graph.findStartDestination().id){
                    saveState = true
                }
                launchSingleTop = true
            }
        }
    }

    LazyColumn(){
        item {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                val (text , logo , userName , bio , followers , following, button) = createRefs()

                Text(text = SharedPref.getName(context) , style = TextStyle(
                    fontWeight = FontWeight.ExtraBold ,
                    fontSize = 25.sp
                ) , modifier = Modifier
                    .constrainAs(text) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                    }
                )

                Text(text = SharedPref.getUserName(context) , style = TextStyle(
                    fontWeight = FontWeight.SemiBold ,
                    fontSize = 20.sp
                ) , modifier = Modifier
                    .constrainAs(userName) {
                        start.linkTo(text.start)
                        top.linkTo(text.bottom)
                    }
                    .padding(top = 7.dp)
                )
                Text(text = SharedPref.getBio(context) , style = TextStyle(
                    fontWeight = FontWeight.Light ,
                    fontSize = 16.sp
                ) , modifier = Modifier
                    .constrainAs(bio) {
                        start.linkTo(userName.start)
                        top.linkTo(userName.bottom)
                    }
                    .width(250.dp)
                    .padding(top = 10.dp)
                )

                Image(painter = rememberAsyncImagePainter(model = SharedPref.getImage(context)) ,
                    contentDescription = null ,
                    modifier = Modifier
                        .constrainAs(logo) {
                            top.linkTo(parent.top)
                            end.linkTo(parent.end)
                        }
                        .size(100.dp)
                        .clip(CircleShape) ,
                    contentScale = ContentScale.Crop
                )

                Text(text = "${followerList!!.size} Followers" , style = TextStyle(
                    fontWeight = FontWeight.Light ,
                    fontSize = 16.sp
                ) , modifier = Modifier
                    .constrainAs(followers) {
                        start.linkTo(bio.start)
                        top.linkTo(bio.bottom)
                    }
                    .width(250.dp)
                    .padding(top = 10.dp)
                )

                Text(text = "${followingList!!.size} Following" , style = TextStyle(
                    fontWeight = FontWeight.Light ,
                    fontSize = 16.sp
                ) , modifier = Modifier
                    .constrainAs(following) {
                        start.linkTo(followers.start)
                        top.linkTo(followers.bottom)
                    }
                    .width(250.dp)
                    .padding(top = 10.dp)
                )

                ElevatedButton(onClick = { authViewModel.logout() },
                    modifier = Modifier
                        .constrainAs(button){
                            top.linkTo(following.bottom, margin  = 3.dp)
                            start.linkTo(following.start)
                        }
                ) {
                    Text(text = "LogOut")
                }
            }
        }
        items(threads?:emptyList()){threads ->
            ThreadItem(thread = threads, userModel = user, navController, userId = FirebaseAuth.getInstance().currentUser!!.uid)
        }
    }
}


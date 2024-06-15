package com.example.threadsappclone.screens

import android.widget.Toast
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
fun OtherUsers(navController: NavHostController, userId : String) {

    val authViewModel: AuthViewModel = viewModel()
    val firebaseUser by authViewModel.firebaseUser.observeAsState()
    val context = LocalContext.current

    val userViewModel: UserViewModel = viewModel()
    val threads by userViewModel.threads.observeAsState(null)
    val gUser by userViewModel.user.observeAsState(null)
    val followerList by userViewModel.followerList.observeAsState(null)
    val followingList by userViewModel.followingList.observeAsState(null)


    userViewModel.fetchThread(userId)
    userViewModel.fetchUser(userId)
    userViewModel.getFollowers(userId)
    userViewModel.getFollowing(userId)
    // M B R S B M S R

    if(gUser!=null) {
        LazyColumn() {
            item {
                ConstraintLayout(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    val (text , logo , userName , bio , followers , following , button) = createRefs()

                    Text(text = gUser?.name ?: "Loading.." , style = TextStyle(
                        fontWeight = FontWeight.ExtraBold ,
                        fontSize = 25.sp
                    ) , modifier = Modifier
                        .constrainAs(text) {
                            start.linkTo(parent.start)
                            top.linkTo(parent.top)
                        }
                    )

                    Text(text = gUser?.userName ?: "Loading.." , style = TextStyle(
                        fontWeight = FontWeight.SemiBold ,
                        fontSize = 20.sp
                    ) , modifier = Modifier
                        .constrainAs(userName) {
                            start.linkTo(text.start)
                            top.linkTo(text.bottom)
                        }
                        .padding(top = 7.dp)
                    )
                    Text(text = gUser?.bio ?: "Loading.." , style = TextStyle(
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

                    Image(painter = rememberAsyncImagePainter(model = gUser?.imageContent) ,
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

                    Text(text = "${followerList?.size} Followers" , style = TextStyle(
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

                    Text(text = "${followingList?.size} Following" , style = TextStyle(
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
                    ElevatedButton(onClick = {
                        if(FirebaseAuth.getInstance().currentUser!=null) {
                            userViewModel.followUsers(
                                userId ,
                                FirebaseAuth.getInstance().currentUser !!.uid
                            )
                        }
                    },
                        modifier = Modifier
                            .constrainAs(button){
                                top.linkTo(following.bottom, margin  = 3.dp)
                                start.linkTo(following.start)
                            }
                    ) {
                        Text(text = if(followerList!=null && followerList!!.isNotEmpty() && followerList!!.contains(FirebaseAuth.getInstance().currentUser!!.uid)) "Following" else "Follow")
                    }
                }
            }
            if (threads != null) {
                items(threads ?: emptyList()) { threads ->
                    ThreadItem(
                        thread = threads ,
                        userModel = gUser !! ,
                        navController ,
                        userId = FirebaseAuth.getInstance().currentUser !!.uid
                    )
                }
            }
        }
    }
}


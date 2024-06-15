package com.example.threadsappclone.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.threadsappclone.navigation.Routes
import com.example.threadsappclone.viewmodel.AuthViewModel


@Composable
fun Login(navController : NavHostController){
    var email by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }

    val context = LocalContext.current
    val authViewModel : AuthViewModel = viewModel()
    val firebaseUser by authViewModel.firebaseUser.observeAsState()
    val error by authViewModel.error.observeAsState()

    LaunchedEffect(key1 = firebaseUser){
        if(firebaseUser!=null){
            navController.navigate(Routes.BottomNav.routes){
                popUpTo(navController.graph.startDestinationId)
                launchSingleTop = true
            }
        }
    }

    error?.let {
        Toast.makeText(context,it,Toast.LENGTH_SHORT).show();
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        Text(text = "Login Page", style = TextStyle(
            fontWeight = FontWeight.ExtraBold,
            fontSize = 50.sp
        )
        )

        Spacer(modifier = Modifier.height(30.dp));

        OutlinedTextField(value = email , onValueChange = {email = it}, label = {
            Text(text = "Email")
        }, keyboardOptions =  KeyboardOptions(
            keyboardType = KeyboardType.Email
        ), singleLine = true, modifier = Modifier.fillMaxWidth())

        OutlinedTextField(value = password , onValueChange = {password = it}, label = {
            Text(text = "Password")
        }, keyboardOptions =  KeyboardOptions(
            keyboardType = KeyboardType.Password
        ), singleLine = true, modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(30.dp));

        ElevatedButton(onClick = {
            if(email.isEmpty() || password.isEmpty()){
                Toast.makeText(context,"Please provide all details",Toast.LENGTH_SHORT).show()}
            else{
                authViewModel.login(email,password,context)
            }
        }, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Log In", style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 39.sp,
            )
            )
        }

        Spacer(modifier = Modifier.height(20.dp));

        TextButton(onClick = {
            navController.navigate(Routes.Register.routes){
                popUpTo(navController.graph.startDestinationId)
                launchSingleTop = true
            }
        }) {
            Text(text = "New User? Create a new Account", fontSize = 18.sp,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    color = Color.Blue
                )
            )
        }
    }
}
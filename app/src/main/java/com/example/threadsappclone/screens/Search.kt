package com.example.threadsappclone.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import com.example.threadsappclone.item_view.ThreadItem
import com.example.threadsappclone.item_view.UserItem
import com.example.threadsappclone.viewmodel.HomeViewModel
import com.example.threadsappclone.viewmodel.SearchViewModel
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Search(navController : NavHostController){
    val searchViewModel : SearchViewModel = viewModel()
    val usersList  by searchViewModel.users.observeAsState()
    var text by remember {
        mutableStateOf("");
    }

    Column() {
        Text(text = "Search",style = TextStyle(
            fontWeight = FontWeight.ExtraBold,
            fontSize = 24.sp,
        ), modifier = Modifier.padding(top = 16.dp, start = 16.dp)
        )

        OutlinedTextField(value = text , onValueChange = {text = it}, label = {
            Text("Search User ...")
        }, keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text
        ), singleLine = true,
            modifier = Modifier.fillMaxWidth().padding(start = 10.dp,end = 10.dp),
            leadingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = null)
            })

        LazyColumn() {
            if(usersList!=null && usersList !!.isNotEmpty()) {
                val filterItems = usersList !!.filter { it.name.contains(text , ignoreCase = true) }
                items(filterItems ?: emptyList()) { userModel ->
                    UserItem(userModel , navController)
                }
            }
        }
    }
}

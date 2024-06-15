package com.example.threadsappclone.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.threadsappclone.item_view.ThreadItem
import com.example.threadsappclone.viewmodel.HomeViewModel
import androidx.compose.foundation.lazy.items
import androidx.navigation.NavHostController
import com.example.threadsappclone.utils.SharedPref
import com.google.firebase.auth.FirebaseAuth

@Composable
fun Home(navController: NavHostController) {
    val homeViewModel: HomeViewModel = viewModel()
    val threadAndUsers by homeViewModel.threadsAndUsers.observeAsState()
    val context = LocalContext.current

    // Shuffle the list to make the order random
    val shuffledThreadAndUsers = threadAndUsers?.shuffled()

    LazyColumn() {
        items(shuffledThreadAndUsers ?: emptyList()) { pair ->
            ThreadItem(
                thread = pair.first,
                userModel = pair.second,
                navController,
                FirebaseAuth.getInstance().currentUser!!.uid
            )
        }
    }
}

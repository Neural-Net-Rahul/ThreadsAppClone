package com.example.threadsappclone.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.threadsappclone.screens.AddThreads
import com.example.threadsappclone.screens.BottomNav
import com.example.threadsappclone.screens.Home
import com.example.threadsappclone.screens.Login
import com.example.threadsappclone.screens.Notification
import com.example.threadsappclone.screens.OtherUsers
import com.example.threadsappclone.screens.Profile
import com.example.threadsappclone.screens.Register
import com.example.threadsappclone.screens.Search
import com.example.threadsappclone.screens.Splash

@Composable
fun NavGraph(navController : NavHostController){
    // here we have three things, Controller, NavHost and Graph
    NavHost(navController = navController , startDestination = Routes.Splash.routes) {
        composable(route = Routes.Splash.routes){
            Splash(navController)
        }
        composable(Routes.Home.routes){
            Home(navController)
        }
        composable(Routes.Search.routes){
            Search(navController)
        }
        composable(Routes.Notification.routes){
            Notification()
        }
        composable(Routes.AddThread.routes){
            AddThreads(navController)
        }
        composable(Routes.Profile.routes){
            Profile(navController)
        }
        composable(Routes.BottomNav.routes){
            BottomNav(navController)
        }
        composable(Routes.Login.routes){
            Login(navController)
        }
        composable(Routes.Register.routes){
            Register(navController)
        }
        composable(Routes.OtherUsers.routes){
            val data = it.arguments!!.getString("data");
            OtherUsers(navController,data!!)
        }
    }
}
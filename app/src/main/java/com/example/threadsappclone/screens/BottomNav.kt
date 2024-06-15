package com.example.threadsappclone.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.threadsappclone.model.BottomNavItem
import com.example.threadsappclone.navigation.Routes

@Composable
fun BottomNav(navController : NavHostController){
    val navController1 = rememberNavController()
    Scaffold(bottomBar = {MyBottomBar(navController1)}) {
        innerPadding ->
        NavHost(
            navController = navController1,
            startDestination = Routes.Home.routes,
            modifier = Modifier.padding(innerPadding)
        ){
            composable(Routes.Home.routes){
                Home(navController1)
            }
            composable(Routes.Search.routes){
                Search(navController)
            }
            composable(Routes.Notification.routes){
                Notification()
            }
            composable(Routes.AddThread.routes){
                AddThreads(navController1)
            }
            composable(Routes.Profile.routes){
                Profile(navController1)
            }
        }
    }
}

@Composable
fun MyBottomBar(navController1 : NavHostController){
    val backStackEntry = navController1.currentBackStackEntry;
    val list = listOf(
        BottomNavItem("Home", Routes.Home.routes, Icons.Rounded.Home),
        BottomNavItem("Search", Routes.Search.routes, Icons.Rounded.Search),
        BottomNavItem("Add Threads", Routes.AddThread.routes, Icons.Rounded.Add),
        BottomNavItem("Notification", Routes.Notification.routes, Icons.Rounded.Notifications),
        BottomNavItem("Profile", Routes.Profile.routes, Icons.Rounded.AccountCircle),
    )
    BottomAppBar {
        list.forEach{
            val selected = it.route == backStackEntry?.destination?.route
            NavigationBarItem(
                selected = selected ,
                onClick = { navController1.navigate(it.route){
                    popUpTo(navController1.graph.findStartDestination().id){
                        saveState = true
                    }
                    launchSingleTop = true
                } } ,
                icon = {Icon(imageVector = it.icon, contentDescription = null, modifier = Modifier.size(30.dp))}
            )
        }
    }
}

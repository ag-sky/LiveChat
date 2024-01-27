package com.example.livechat.Screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.livechat.LCViewModel

@Composable
fun ProfileScreen(navController: NavController, vm : LCViewModel) {
    Text(text = "Profile")
    BottomNavigationMenu(selectedItem =BottomNavigationItem.PROFILE , navController = navController)
}
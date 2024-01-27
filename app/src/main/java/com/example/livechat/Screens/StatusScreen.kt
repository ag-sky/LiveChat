package com.example.livechat.Screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.livechat.LCViewModel

@Composable
fun StatusScreen( navController: NavController, vm: LCViewModel) { Text(text = "StatusList")
    BottomNavigationMenu(selectedItem =BottomNavigationItem.STATUSLIST , navController = navController)

}
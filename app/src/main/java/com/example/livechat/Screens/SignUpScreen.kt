package com.example.livechat.Screens

import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.livechat.DestinationScreen
import com.example.livechat.LCViewModel


@Composable
fun SignUpScreen (navController: NavController, vm: LCViewModel){

    Text(text = "Navigate to other screen", modifier = Modifier.clickable {
        navController.navigate(DestinationScreen.Login.route)
    })
}
package com.example.livechat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.compose.composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.livechat.Screens.ChatListScreen
import com.example.livechat.Screens.LoginScreen
import com.example.livechat.Screens.ProfileScreen
import com.example.livechat.Screens.SignUpScreen
import com.example.livechat.Screens.SingleChatScreen
import com.example.livechat.Screens.SingleStatusScreen
import com.example.livechat.Screens.StatusScreen
import com.example.livechat.ui.theme.LiveChatTheme
import dagger.hilt.android.AndroidEntryPoint


sealed class DestinationScreen(var route: String) {
    object Signup : DestinationScreen("signup")
    object Login : DestinationScreen("login")
    object Profile : DestinationScreen("profile")
    object ChatList : DestinationScreen("chatList")
    object SingleChat : DestinationScreen("singleChat/{chatId}") {
        fun createRoute(id: String) = "singlechat/$id"
    }

    object StatusList : DestinationScreen("StatusList")
    object SingleStatus : DestinationScreen("singleStatus/{userId") {
        fun createRoute(userId: String) = "singleStatus/$userId"
    }

}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LiveChatTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ChatAppNavigatin()
                }
            }
        }
    }

    @Composable
    fun ChatAppNavigatin() {

        val navController = rememberNavController()
        var vm = hiltViewModel<LCViewModel>()
        NavHost(navController = navController, startDestination = DestinationScreen.Signup.route) {
            composable(DestinationScreen.Signup.route) {
                SignUpScreen(navController, vm)

            }
            composable(DestinationScreen.Login.route) {
                LoginScreen(vm, navController)
            }

            composable(DestinationScreen.ChatList.route) {
                ChatListScreen(navController = navController, vm = vm)
            }
            composable(DestinationScreen.SingleChat.route) {
                var chatId = it.arguments?.getString("chatId")
                chatId?.let {
                    SingleChatScreen(navController = navController, vm = vm, chatId = chatId)

                }
            }
            composable(DestinationScreen.StatusList.route) {
                StatusScreen(navController = navController, vm = vm)
            }
            composable(DestinationScreen.Profile.route) {
                ProfileScreen(navController = navController, vm = vm)
            }
            composable(DestinationScreen.SingleStatus.route) {
                val userId = it.arguments?.getString("userId")
                userId?.let {
                    SingleStatusScreen(navController, vm, userId = it)
                }
            }
        }
    }

}


